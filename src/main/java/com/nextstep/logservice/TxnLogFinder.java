package com.nextstep.logservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Session;

import java.util.Map;

public class TxnLogFinder {

    private static final Logger log = LoggerFactory.getLogger(TxnLogFinder.class);
    private static final CommonCode common = new CommonCode();

    
    public static void main(String[] args) {
    	getCCTResponse("192253253925498888");
	}
    public static Map<String, String> getCCTResponse(String txnId) {

        log.info("🔎 Searching logs for TxnID={}", txnId);

        txnId = CommonCode.date_time(txnId);

        if (txnId.length() != 18) {
            log.error("❌ Invalid Txn ID length : {}", txnId.length());
            return null;
        }

        try {
            // ==================== LOAD CONFIG + TRIM ====================
            String scpHost = ConfigLoader.get("scp.host");
            String scpUser = ConfigLoader.get("scp.user");
            String scpPass = ConfigLoader.get("scp.password");

            String mainUser = ConfigLoader.get("main.user");
            String mainPass = ConfigLoader.get("main.password");

            int mainPort = Integer.parseInt(ConfigLoader.get("main.port").trim());
            int localPort = Integer.parseInt(ConfigLoader.get("local.port").trim());

            String serverAddr = common.getSevAdd().trim();

            log.info("🌐 SCP Host={} User={} ServerAddr={}", scpHost, scpUser, serverAddr);

            // ================== SSH CONNECTIONS ==================
            Session scpSession = SshConnectionManager.openScpSession(
                    scpUser, scpHost, scpPass, localPort, serverAddr, mainPort);

            Session mainSession = SshConnectionManager.openMainSession(
                    mainUser, mainPass, localPort);

            // ================== COMMAND BUILD ====================
            Map<String, String> cmds = CommandBuilder.buildCommands(
                    common.getTimer(), txnId);

            String[] cmdList = {
                    cmds.get("grep"),
                    cmds.get("zgrep.gz"),
                    cmds.get("zgrep.zip"),
                    cmds.get("zgrep.*")
            };

            Map<String, String> result = null;

            for (String cmd : cmdList) {
                log.info("▶ Running command: {}", cmd);
                result = CommandExecutor.execute(mainSession, cmd, txnId);

                if (result.get("uniqueId") != null) {
                    log.info("✅ Unique ID found, stopping further search");
                    break;
                }
            }

            scpSession.disconnect();
            mainSession.disconnect();

            log.info("✨ Log Search Completed");

            return result;

        } catch (Exception e) {
            log.error("❌ Exception while processing txn {}", txnId, e);
            return null;
        }
    }
}
