package com.nextstep.logservice;

import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class PortForwardUtil {

    private static final Logger log = LoggerFactory.getLogger(PortForwardUtil.class);

    /**
     * Sets up port forwarding with auto port search.
     *
     * @param session      Active SSH session
     * @param startingPort Port to try first
     * @param bindAddress  Local bind address (usually "127.0.0.1")
     * @param remoteHost   Remote server address
     * @param remotePort   Remote server port
     * @return The local port that was successfully forwarded
     */
    public static int setupPortForwarding(Session session,
                                          int startingPort,
                                          String bindAddress,
                                          String remoteHost,
                                          int remotePort) {

        int port = startingPort;

        log.info("🔍 Checking available port starting from {}", startingPort);

        // Find free port
        while (isPortInUse(port)) {
            log.warn("⚠ Port {} is already in use. Trying next...", port);
            port++;
        }

        log.info("➡ Attempting to bind local {}:{} → {}:{}", bindAddress, port, remoteHost, remotePort);

        try {
            session.setPortForwardingL(bindAddress, port, remoteHost, remotePort);
            log.info("✅ Port forwarding established on {}:{}", bindAddress, port);
        } catch (Exception e) {
            log.error("❌ Failed to bind port {}: {}", port, e.getMessage(), e);
        }

        return port;
    }

    /**
     * Checks if a TCP port is already in use.
     *
     * @param port The port number to check
     * @return true if port is in use, false if port is free
     */
    private static boolean isPortInUse(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            socket.setReuseAddress(true);
            return false; // port is free
        } catch (IOException e) {
            return true; // port busy
        }
    }
}
