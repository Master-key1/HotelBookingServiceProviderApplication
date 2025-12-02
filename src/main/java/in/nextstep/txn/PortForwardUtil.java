package in.nextstep.txn;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.net.ServerSocket;

public class PortForwardUtil {

    public static int setupPortForwarding(Session session,
                                          int startingPort,
                                          String bindAddress,
                                          String remoteHost,
                                          int remotePort) {

        int port = startingPort;

        // Find free port
        while (isPortInUse(port)) {
            System.out.println("Port " + port + " is already in use. Trying next...");
            port++;
        }

        try {
            // Set port forwarding using available port
            session.setPortForwardingL(bindAddress, port, remoteHost, remotePort);
            System.out.println("✅ Port forwarding established on: " + bindAddress + ":" + port);

        } catch (Exception e) {
            System.err.println("❌ Failed to bind port " + port + ": " + e.getMessage());
            e.printStackTrace();
        }

        return port;
    }

    // Private helper method
    private static boolean isPortInUse(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            socket.setReuseAddress(true);
            return false; // free
        } catch (IOException e) {
            return true; // busy
        }
    }
}
