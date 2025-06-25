import src.Game;
import src.AsciiTool;

public class Main {
    public static void main(String[] args) throws Exception {
        AsciiTool.main(args);
        return;
        
        Game game = new Game();
        game.run();
    }
}
