package com.nextstep.logservice;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;

public class SshConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(SshConnectionManager.class);

    public static Session openScpSession(
            String user, String host, String pass,
            int localPort, String remoteHost, int remotePort) throws Exception {

        log.info("==============================================");
        log.info("🔐 Connecting to SCP Server");
        log.info("➡ Host        : {}", host);
        log.info("➡ User        : {}", user);
        log.info("➡ Remote Host : {}", remoteHost);
        log.info("➡ Remote Port : {}", remotePort);
        log.info("➡ Local Port  : {}", localPort);
        log.info("➡ Password    : {}", (pass));     // MASKED
        log.info("==============================================");

        JSch jsch = new JSch();
        Session scpSession = jsch.getSession(user, host, 22);
        scpSession.setPassword(pass);
        scpSession.setConfig("StrictHostKeyChecking", "no");

        scpSession.connect(30000);
        log.info("✅ Connected to SCP server {}", host);

        while (isPortInUse(localPort)) {
            log.warn("⚠ Port {} in use, trying next...", localPort);
            localPort++;
        }

        log.info("➡ Port Forwarding: LOCAL {} → {}:{}", localPort, remoteHost, remotePort);

       
       	 log.info("✅ SCP server connection status {}",scpSession.isConnected() );
        scpSession.setPortForwardingL(localPort, remoteHost, remotePort);

        return scpSession;
    }


    public static Session openMainSession(String user, String pass, int localPort) throws Exception {

        log.info("==============================================");
        log.info("🔐 Connecting to MAIN Server");
        log.info("➡ Localhost Port : {}", localPort);
        log.info("➡ User           : {}", user);
        log.info("➡ Password       : {}", (pass));   // MASKED
        log.info("==============================================");

        JSch jsch = new JSch();
        Session mainSession = jsch.getSession(user, "localhost", localPort);
        mainSession.setPassword(pass);
        mainSession.setConfig("StrictHostKeyChecking", "no");

        mainSession.connect(30000);

   	 log.info("✅ Main server connection status {}",mainSession.isConnected() );
        log.info("✅ Connected to MAIN server through tunnel");

        return mainSession;
    }


    private static boolean isPortInUse(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    // Mask passwords like *****25!
    private static String mask(String pass) {
        if (pass == null || pass.length() <= 2) return "**";
        return "*".repeat(pass.length() - 2) + pass.substring(pass.length() - 2);
    }
}
