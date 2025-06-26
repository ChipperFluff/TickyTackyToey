package src;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Runtime {
    private final Logger logger = new Logger();

    public void start(Console game) {
        logger.log("Launching game");
        if (!System.getProperty("skipConsoleInit", "false").equals("true")) {
            try {
                String[] cmd = {
                    "gnome-terminal",
                    "--window-with-profile=TinyFont",
                    "--",
                    "java",
                    "-DskipConsoleInit=true",
                    "-cp",
                    System.getProperty("java.class.path"),
                    "Main"
                };

                java.lang.Runtime.getRuntime().exec(cmd);
                logger.log("Spawned new terminal for game");

                // Silence logs in the current console
                System.setOut(new PrintStream(OutputStream.nullOutputStream()));
                System.setErr(new PrintStream(OutputStream.nullOutputStream()));

                System.exit(0);
            } catch (IOException e) {
                logger.error("Failed to spawn terminal", e);
                System.out.println("Press Enter to exit...");
                try { System.in.read(); } catch (IOException ex) {}
                System.exit(1);
            }
        }
        game.setLogger(logger);
        game.run();
    }
}
