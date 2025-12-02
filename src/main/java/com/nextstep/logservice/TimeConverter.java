package com.nextstep.logservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TimeConverter {

    private static final Logger log = LoggerFactory.getLogger(TimeConverter.class);

    /**
     * Builds grep/zgrep commands based on EST timestamp and TxnId.
     */
    public static Map<String, String> cmdBuilder(String estTimestamp, String txnId) {

        Map<String, String> cmdMap = new HashMap<>();

        try {
            log.info("⏳ Converting EST Timestamp: {}", estTimestamp);

            // -------------------------- PARSE EST --------------------------
            DateTimeFormatter estFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime estDateTime = LocalDateTime.parse(estTimestamp, estFormatter);

            // -------------------------- CONVERT TO IST --------------------------
            ZonedDateTime istDateTime = estDateTime
                    .atZone(ZoneId.of("America/New_York"))
                    .withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

            String istFormatted = istDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
            log.info("📌 IST Timestamp: {}", istFormatted);

            // Extract date & hour for log filename
            String date1 = istDateTime.toLocalDate().toString();        // yyyy-MM-dd
            String hour1 = String.format("%02d", istDateTime.getHour()); // HH

            log.info("🗂 Log File Date: {} Hour: {}", date1, hour1);

            // -------------------------- PREPARE COMMANDS --------------------------
            String basePath = "/opt/auruspay_switch/log/auruspay/auruspay.log";

            cmdMap.put("grep",
                    "grep --color --text -C10 \"" + txnId + "\" " + basePath);

            cmdMap.put("zgrep.gz",
                    "zgrep --color --text -C10 \"" + txnId + "\" " + basePath + "-" + date1 + "-" + hour1 + ".gz");

            cmdMap.put("zgrep.zip",
                    "zgrep --color --text -C10 \"" + txnId + "\" " + basePath + "-" + date1 + "-" + hour1 + ".zip");

            cmdMap.put("zgrep.*",
                    "zgrep --color --text -C10 \"" + txnId + "\" " + basePath + "-" + date1 + "-" + hour1 + ".*");

            log.debug("📜 Built Commands:");
            cmdMap.forEach((k, v) -> log.debug("{} → {}", k, v));

        } catch (Exception e) {
            log.error("❌ Error building command for timestamp: {}", estTimestamp, e);
        }

        return cmdMap;
    }
}
