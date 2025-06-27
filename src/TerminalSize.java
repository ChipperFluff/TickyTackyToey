package src;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TerminalSize {
    public int width = 100;
    public int height = 50;

    // returns true if size changed
    public boolean updateTerminalSize() {
        int oldWidth = width;
        int oldHeight = height;

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "stty size < /dev/tty"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();

            if (line != null && !line.trim().isEmpty()) {
                String[] parts = line.trim().split("\\s+");
                height = Integer.parseInt(parts[0]);
                width = Integer.parseInt(parts[1]);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to get terminal size: " + e.getMessage());
            // Keep previous values
        }

        return (width != oldWidth || height != oldHeight);
    }
}
