package in.nextstep.txn;

import com.jcraft.jsch.*;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;
import java.util.regex.*;

public class TxnLogFinder {

    private static Properties config;

    private static String scpHost;
    private static String scpUser;
    private static String scpPassword;
    private static String mainUser;
    private static String mainPassword;
    private static int mainPort;
    private static int localPort;

    private static String cctResp;
    private static String decryptedCctResp;
    private static Map<String, String> detail;

    private static final CommonCode commonCode = new CommonCode();

    static {
        loadConfig();
    }

    // ✅ Load properties correctly
    private static void loadConfig() {
        try (InputStream input = TxnLogFinder.class
                .getClassLoader()
                .getResourceAsStream("LogConfig.properties")) {

            if (input == null) {
                System.out.println("LogConfig.properties file not found");
                return;
            }

            config = new Properties();
            config.load(input);

            scpHost = config.getProperty("scp.host");
            scpUser = config.getProperty("scp.user");
            scpPassword = config.getProperty("scp.password");

            mainUser = config.getProperty("main.user");
            mainPassword = config.getProperty("main.password");

            mainPort = Integer.parseInt(config.getProperty("main.port"));
            localPort = Integer.parseInt(config.getProperty("local.port"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Main public method
    public static Map<String, String> getCCTResponse(String txnId) {

        String txnID = CommonCode.date_time(txnId);

        if (txnID.length() != 18) {
            System.out.println("Invalid Txn ID length");
            return null;
        }

        Session scpSession = null;
        Session mainSession = null;

        try {
            scpSession = openScpSession();
            mainSession = openMainSession(scpSession);

            Map<String, String> command = buildCommand(txnID);

            String result = executeCommand(mainSession, command.get("grep"), txnID);
            if (result == null) {
                result = executeCommand(mainSession, command.get("zgrep.gz"), txnID);
            }
            if (result == null) {
                result = executeCommand(mainSession, command.get("zgrep.zip"), txnID);
            }
            if (result == null) {
                result = executeCommand(mainSession, command.get("zgrep.*"), txnID);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSessions(scpSession, mainSession);
        }

        return detail;
    }

    private static Session openScpSession() throws JSchException {
        JSch jsch = new JSch();
        Session scpSession = jsch.getSession(scpUser, scpHost, 22);
        scpSession.setPassword(scpPassword);
        scpSession.setConfig("StrictHostKeyChecking", "no");

        System.out.println("Connecting to SCP server...");
        scpSession.connect(30000);
        System.out.println("Connected to SCP server.");

        System.out.println("========= Port Forwarding Debug Info =========");
        System.out.println("Local Port      : " + localPort);
        System.out.println("Remote Host     : " + commonCode.getSevAdd());
        System.out.println("Remote MainPort : " + mainPort);
        System.out.println("==============================================");

        scpSession.setPortForwardingL(localPort, commonCode.getSevAdd(), mainPort);

      //  localPort = 2223;
        // Set up port forwarding to the main server
    //    scpSession.setPortForwardingL(2223, commonCode.getSevAdd(), mainPort);
        return scpSession;
    }

    /**
     * Opens an SSH connection to the main server through local port forwarding.
     * 
     * @param scpSession The established SCP session.
     * @return The established main server session.
     * @throws JSchException If the connection fails.
     */
    private static Session openMainSession(Session scpSession) throws JSchException {
        JSch jsch = new JSch();
        System.out.println("====== Main Session Details ======");
        System.out.println("User        : " + mainUser);
        System.out.println("Host        : localhost");
        System.out.println("Port        : " + localPort);

        // ⚠️ Do NOT print real password in production
        System.out.println("Password set: " + mainPassword);
        System.out.println("===============================");

        
        Session mainSession = jsch.getSession(mainUser, "localhost", localPort);
        mainSession.setPassword(mainPassword);
        mainSession.setConfig("StrictHostKeyChecking", "no");

        System.out.println("Connecting to main server...");
        mainSession.connect(30000);
        System.out.println("Connected to main server.");

        return mainSession;
    }
    // ✅ Port check helper
    private static boolean isPortInUse(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    // ✅ Build command
    private static Map<String, String> buildCommand(String txnID) {
        return TimeConverter.cmdBuilder(commonCode.getTimer(), txnID);
    }

    // ✅ Execute commands
    private static String executeCommand(Session session, String command, String txnID)
            throws Exception {

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);
        InputStream input = channel.getInputStream();
        channel.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        String uniqueId = null;
        StringBuilder logs = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            logs.append(line).append("\n");

            Matcher matcher = Pattern.compile("SPO\\.([a-fA-F0-9\\-]{36})").matcher(line);

            if (line.contains(txnID)) {
                uniqueId = commonCode.ExactID(line);
            } else if (matcher.find()) {
                uniqueId = matcher.group(1);
            }
        }

        channel.disconnect();

        if (uniqueId != null) {
            detail = new HashMap<>();
            detail.put("logs", logs.toString());
        }

        return uniqueId;
    }

    // ✅ Properly close sessions
    private static void closeSessions(Session scp, Session main) {
        if (main != null && main.isConnected()) main.disconnect();
        if (scp != null && scp.isConnected()) scp.disconnect();
    }
}
