import java.io.IOException;

import console.SideConsole;
import src.Game;

public class Main {
    public static void main(String[] args) {
        try {
            SideConsole.launch();
            SideConsole.sendLog("INFO", "Started logger");
        } catch (IOException e) {
            System.err.println("Side console failed");
        }

        try {

            Game game = new Game();
            game.run();
        } catch (Exception e) {
            System.err.println("=== ERROR OCCURRED ===");
            e.printStackTrace();
            SideConsole.sendLog("ERROR", "Exception: " + e.toString());
            System.err.println("\nPress Enter to exit...");
            try {
                System.in.read();
            } catch (Exception ignored) {}
        } 

        SideConsole.sendLog("CRITICAL", "Game ending!");
        System.exit(0);
    }
}
