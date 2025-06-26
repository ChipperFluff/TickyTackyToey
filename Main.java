import src.Game;
import src.Runtime;

public class Main {
    public static void main(String[] args) {
        try {
            Runtime runtime = new Runtime();
            runtime.start(new Game());
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
