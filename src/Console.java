package src;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public abstract class Console {
    protected InputStream in = System.in;
    protected PrintStream out = System.out;
    protected TerminalSize size = new TerminalSize();
    protected int[][] display;

    protected Logger logger;

    private boolean active = true;
    private static final String BLACK_TEXT = "\u001B[30m";
    private static final String WHITE_BG = "\u001B[47m";
    private static final String RESET = "\u001B[0m";

    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";

    public Console() {
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }


    protected void exit() {
        active = false;
    }

    protected static void invert_out(String text) {
        System.out.print(WHITE_BG + BLACK_TEXT + text + RESET);
        System.out.flush();
    }

    private static void clear() {
        System.out.print("\u001B[H");
        System.out.flush();
    }


    abstract protected void arrow_press(String direction);
    abstract protected void onKeyPress(int key, boolean isArrowKey);
    abstract protected void draw();

    protected void checkDisplay() {
        final int CONSOLE_WIDTH = size.width;
        final int CONSOLE_HEIGHT = size.height;

        if (display != null && display.length == CONSOLE_HEIGHT && display[0].length == CONSOLE_WIDTH) {
            return;
        }

        display = new int[CONSOLE_HEIGHT][CONSOLE_WIDTH];
        for (int i = 0; i < CONSOLE_HEIGHT; i++) {
            for (int j = 0; j < CONSOLE_WIDTH; j++) {
            display[i][j] = 99; // 32;
            }
        }
    }

    protected void clearDisplay() {
        if (display == null) return;
        for (int i = 0; i < display.length; i++) {
            for (int j = 0; j < display[i].length; j++) {
                display[i][j] = ' ';
            }
        }
    }

    protected void putString(int row, int col, String text) {
        if (display == null) return;
        for (int i = 0; i < text.length() && col + i < display[row].length; i++) {
            display[row][col + i] = text.charAt(i);
        }
    }

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

    private void render() {
        clear();
        draw();

        for (int i = 0; i < size.height; i++) {
            for (int j = 0; j < size.width; j++) {
                out.print((char) display[i][j]);
            }
            out.println();
        }
        out.flush();
    }

    public void run() {
        if (logger != null) logger.log("Console started");
        while (active) {
            size.updateTerminalSize();
            checkDisplay();
            render();
            parseInput();
        }
        if (logger != null) logger.log("Console stopped");
    }
}
