package console;

import java.io.*;
import java.net.*;

public class SideConsole {

    public static final int PORT = 56666;
    private static Process terminalProcess = null;
    private static final File heartbeat = new File("/tmp/squirrel_alive");

    public static void launch() throws IOException {
        // Ensure heartbeat file exists
        if (!heartbeat.exists()) {
            heartbeat.createNewFile();
        }

        String javaBin = System.getProperty("java.home") + "/bin/java";
        String classpath = new File("out").getAbsolutePath();
        String className = "console.SideConsoleServer";

        String command = String.format(
            "gnome-terminal -- bash -c '%s -cp \"%s\" %s %d %s; exec bash'",
            javaBin, classpath, className, PORT, heartbeat.getAbsolutePath()
        );

        System.out.println("[DEBUG] Launching: " + command);
        terminalProcess = Runtime.getRuntime().exec(new String[] { "sh", "-c", command });

        // Handle JVM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[DEBUG] Main shutdown. Cleaning up.");
            if (heartbeat.exists()) {
                heartbeat.delete();
            }
            if (terminalProcess != null && terminalProcess.isAlive()) {
                terminalProcess.destroy();
            }
        }));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[DEBUG] Launch interrupted: " + e.getMessage());
        }
    }

    public static void sendLog(String level, String message) {
        try (Socket socket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(level + ": " + message);
        } catch (IOException e) {
            System.err.println("[MAIN -> CONSOLE] Failed to send log: " + e.getMessage());
        }
    }
}
