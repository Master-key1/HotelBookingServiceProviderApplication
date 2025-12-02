package com.nextstep.logservice;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import in.nextstep.txn.CommonCode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandExecutor {

    private static final CommonCode commonCode = new CommonCode();

    /**
     * Executes a shell command on remote server over SSH
     * Extracts:
     *  - Unique ID (UUID from SPO.*)
     *  - All logs
     */
    public static Map<String, String> execute(Session mainSession, String cmd, String txnId) {
        Map<String, String> response = new HashMap<>();

        try {
            ChannelExec channel = (ChannelExec) mainSession.openChannel("exec");
            channel.setCommand(cmd);

            InputStream input = channel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            channel.connect();

            String line;
            String uniqueId = null;
            StringBuilder logs = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                logs.append(line).append("\n");

                // Regex for UUID in SPO.<UUID>
                Matcher matcher = Pattern.compile("SPO\\.([a-fA-F0-9\\-]{36})").matcher(line);

                // If line contains the original txnID → extract Unique ID
                if (line.contains(txnId)) {
                    uniqueId = commonCode.ExactID(line);
                }
                // If SPO.<UUID> is found
                else if (matcher.find()) {
                    uniqueId = matcher.group(1);
                }
            }

            channel.disconnect();

            // Save logs
            response.put("logs", logs.toString());

            // Save uniqueId only if found
            if (uniqueId != null) {
                response.put("uniqueId", uniqueId);
            }

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Command execution failed: " + e.getMessage());
            return response;
        }
    }
}
