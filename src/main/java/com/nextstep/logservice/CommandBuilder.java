package com.nextstep.logservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CommandBuilder {

    private static final Logger log = LoggerFactory.getLogger(CommandBuilder.class);

    public static Map<String, String> buildCommands(String timer, String txnID) {
        log.info("🛠 Building commands for TxnID={} Timer={}", txnID, timer);

        Map<String, String> cmds = TimeConverter.cmdBuilder(timer, txnID);

        log.debug("➡ grep       : {}", cmds.get("grep"));
        log.debug("➡ zgrep.gz   : {}", cmds.get("zgrep.gz"));
        log.debug("➡ zgrep.zip  : {}", cmds.get("zgrep.zip"));
        log.debug("➡ zgrep.*    : {}", cmds.get("zgrep.*"));

        return cmds;
    }
}
