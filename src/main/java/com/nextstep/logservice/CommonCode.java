package com.nextstep.logservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonCode {

    private static final Logger log = LoggerFactory.getLogger(CommonCode.class);

    private static String time1;
    private static String date1;
    private static String timer;
    private static String serverAddress;
    private static String txnNodeId;

    // ---------------------- SERVER MAPS ----------------------
    public static final Map<String, String> SERVER_IP = new HashMap<String, String>() {{
        put("91", "152");
        put("92", "153");
        put("93", "172");
        put("94", "72");
        put("95", "155");
        put("96", "169");
        put("97", "69");
        put("98", "11");
        put("99", "71");
    }};

    public static final Map<String, String> NODE_ID = new HashMap<String, String>() {{
        put("91", "UAT42 APP1  192.168.50.152");
        put("92", "UAT42 APP2  192.168.50.153");
        put("93", "UAT42 APP3  192.168.50.172");
        put("94", "UAT42 APP4  192.168.50.72");
        put("95", "STG42 APP1  192.168.50.155");
        put("96", "PFX42 APP1  192.168.50.169");
        put("97", "DEV42 APP1  192.168.50.69");
        put("98", "UAT51 APP1  192.168.106.11");
        put("99", "STGCRT APP1  192.168.50.71");
    }};

    // ----------------------------------------------------------

    /**
     * Extracts Date, Time, Node from Transaction ID.
     */
    public static String date_time(String txnId) {

        try {
            if (txnId.length() != 18) {
                log.error("❌ INVALID TXN ID LENGTH = {} | TxnID={}", txnId.length(), txnId);
                return txnId;
            }

            log.info("🔍 Processing TxnID: {}", txnId);

            // NODE ID (2nd and 3rd digit)
            txnNodeId = txnId.substring(1, 3);
            serverAddress = "192.168.50." + SERVER_IP.get(txnNodeId);
            setServerAddress(serverAddress);

            log.info("➡ NodeID={} | Server IP={}", txnNodeId, serverAddress);

            // YEAR
            int year = Integer.parseInt(txnId.substring(3, 5));
            String fullYear = "20" + year;

            // DAY OF YEAR (Julian date)
            int dayOfYear = Integer.parseInt(txnId.substring(5, 8));
            LocalDate logDate = Year.of(Integer.parseInt(fullYear)).atDay(dayOfYear);

            // Timestamp (milliseconds from midnight)
            long millis = Long.parseLong(txnId.substring(8, 16));

            String hms = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            );

            String inputDate = logDate + " " + hms;
            log.debug("🕒 Parsed Input DateTime: {}", inputDate);

            // Convert to formatted date
            Date parsed = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inputDate);
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa").format(parsed);

            // Set internal values
            time1 = inputDate.substring(11, 13);
            date1 = inputDate.substring(0, 10);
            timer = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(parsed);

            log.info("📌 Final DateTime: {}", formattedDate);
            log.info("🌐 Node Server: {}", NODE_ID.get(txnNodeId));

            return txnId;

        } catch (Exception e) {
            log.error("❌ Error parsing TxnID {}", txnId, e);
            return null;
        }
    }

    /**
     * Extracts UUID from line
     */
    public static String ExactID(String line) {
        try {
            String pattern = "\\[([a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12})";
            Matcher m = Pattern.compile(pattern).matcher(line);

            if (m.find()) {
                String id = m.group(1);
                log.debug("🆔 Extracted UUID: {}", id);
                return id;
            } else {
                log.warn("⚠ No UUID found in line: {}", line);
            }
        } catch (Exception e) {
            log.error("❌ Error extracting ID", e);
        }
        return null;
    }

    // ---------------------------------------------------
    // SETTERS
    // ---------------------------------------------------

    private static void setServerAddress(String addr) {
        CommonCode.serverAddress = addr;
    }

    // Getters
    public static String getDate1() { return date1; }
    public static String getTime1() { return time1; }
    public static String getTimer() { return timer; }
    public static String getSevAdd() { return serverAddress; }

}
