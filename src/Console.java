package src;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public abstract class Console {
    protected InputStream in = System.in;
    protected PrintStream out = System.out;
    private boolean active = true;

    private static final String BLACK_TEXT = "\u001B[30m";
    private static final String WHITE_BG = "\u001B[47m";
    private static final String RESET = "\u001B[0m";

    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";

    protected void exit() {
        active = false;
    }

    protected static void invert_out(String text) {
        System.out.print(WHITE_BG + BLACK_TEXT + text + RESET);
        System.out.flush();
    }

    private static void clear() {
        System.out.print("\u001B[2J\u001B[H");
        System.out.flush();
    }


    abstract protected void arrow_press(String direction);
    abstract protected void onKeyPress(int key, boolean isArrowKey);
    abstract protected void draw();

    private boolean parseArrowKeys(int first) {
        if (first != 27) {
            return false;
        }
        
        int second, third;

        try {
            second = in.read();
            third = in.read();
        } catch(IOException e) {
            return false;
        }

        if (second != 91) {
            return false;
        }

        switch (third) {
            case 65:
                arrow_press(UP);
                break;
            case 66:
                arrow_press(DOWN);
                break;
            case 67:
                arrow_press(RIGHT);
                break;
            case 68:
                arrow_press(LEFT);
                break;
            default:
                return false;
        }
        
        return true;
    }

    private void parseInput() {
        int input;

        try {
            input = in.read();
        } catch (IOException e) {
            return;
        }
        
        boolean arrowParseSuccess = parseArrowKeys(input);
        onKeyPress(input, arrowParseSuccess);
    }

    public void run() {
       while (active) {
            clear();
            draw();
            parseInput();
       }
            
    }
}
