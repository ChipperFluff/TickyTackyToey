package src;

public class Game extends Runtime {
    public Game() {
        super();
    }

    protected void arrow_press(String direction) {
        logger.log("Arrow pressed: " + direction);
    }

    protected void onKeyPress(int key, boolean isArrowKey) {
        // Escape key to exit
        if (key == 27 && !isArrowKey) {
            exit();
        }
    }

    protected void draw() {
        clearDisplay();
        putString(0, 0, "Hello World");
    }
}
