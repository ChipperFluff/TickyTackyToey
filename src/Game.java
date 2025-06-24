package src;

public class Game extends Console {
    protected void arrow_press(String direction) {
        out.println(direction);
    }

    protected void onKeyPress(int key, boolean isArrowKey) {
        out.println(key + " " + isArrowKey);
    }
}
