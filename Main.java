import src.Game;

public class Main {
    public static void main(String[] args) {
        try {
            Game game = new Game();
            game.run();
        } catch (Exception e) {
            System.err.println("=== ERROR OCCURRED ===");
            e.printStackTrace();
            System.err.println("\nPress Enter to exit...");
            try {
                System.in.read();
            } catch (Exception ignored) {}
        }
    }
}
