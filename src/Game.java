package src;

public class Game extends Console {
    protected void arrow_press(String direction) {
    }

    protected void onKeyPress(int key, boolean isArrowKey) {
    }

    protected void draw(boolean init) {
        if (!init) {
            return;
        }
        
        setChar('O', 10, 10);
    }
}
