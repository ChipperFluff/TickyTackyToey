package console;

import java.io.*;
import java.net.*;

public class SideConsoleServer {

    // ANSI escape codes for color
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String MAGENTA = "\u001B[35m";

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        String heartbeatPath = args.length > 1 ? args[1] : null;
        File heartbeat = heartbeatPath != null ? new File(heartbeatPath) : null;

        System.out.println(GREEN + "[SIDE CONSOLE] Listening on port " + port + RESET);
        System.out.println(GREEN + "[SIDE CONSOLE] Watching heartbeat: " + heartbeat + RESET);

        try (ServerSocket server = new ServerSocket(port)) {
            server.setSoTimeout(1000); // Check every 1s for shutdown

            while (true) {
                try {
                    Socket client = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String input = in.readLine();

                    if (input != null) {
                        System.out.println(CYAN + "[RECV] " + input + RESET);
                        if (input.toUpperCase().contains("CRITICAL")) {
                            System.out.println(RED + "[!!!] CRITICAL EVENT DETECTED" + RESET);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    // No client? Check heartbeat
                    if (heartbeat != null && !heartbeat.exists()) {
                        System.out.println(YELLOW + "[SIDE CONSOLE] Heartbeat lost. Shutting down." + RESET);
                        new ProcessBuilder("bash", "-c", "kill $(ps -o ppid= -p " + ProcessHandle.current().pid() + ")").start();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(MAGENTA + "[FATAL] Could not bind to port: " + e.getMessage() + RESET);
        }
    }
}
