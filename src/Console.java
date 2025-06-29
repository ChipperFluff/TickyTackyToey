package src;

import console.SideConsole;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public abstract class Console {
    protected InputStream in = System.in;
    protected PrintStream out = System.out;
    protected TerminalSize size = new TerminalSize();
    protected int[][] display;

    private boolean active = true;
    private boolean initial_render = true;
    private boolean mutated = true;
    private boolean screenChanged = true;

    private static final String BLACK_TEXT = "\u001B[30m";
    private static final String WHITE_BG = "\u001B[47m";
    private static final String RESET = "\u001B[0m";

    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";

    protected void logOut(String level, String message) {
        SideConsole.sendLog(level, message);
    }

    protected void exit() {
        SideConsole.sendLog("WARN", "EXIT GAME requested");
        active = false;
    }

    protected static void invert_out(String text) {
        System.out.print(WHITE_BG + BLACK_TEXT + text + RESET);
        System.out.flush();
    }

    protected boolean setChar(char set, int x, int y) {
        if (x < 0 || x >= size.width || y < 0 || y >= size.height) {
            return false;
        }
        display[y][x] = set;
        mutated = true;
        return true;
    }

    protected boolean invertChar(char set, int x, int y) {
        if (x < 0 || x >= size.width || y < 0 || y >= size.height) {
            return false;
        }
        display[y][x] = 256 + set; // Inverted marker
        mutated = true;
        return true;
    }

    private static void clear() {
        System.out.print("\u001B[H");
        System.out.flush();
    }

    abstract protected void arrow_press(String direction);
    abstract protected void onKeyPress(int key, boolean isArrowKey);
    abstract protected void draw(boolean init);

    private void checkDisplay() {
        if (!screenChanged) {
            return;
        }

        final int CONSOLE_WIDTH = size.width;
        final int CONSOLE_HEIGHT = size.height;

        if (display != null && display.length == CONSOLE_HEIGHT && display[0].length == CONSOLE_WIDTH) {
            return;
        }

        display = new int[CONSOLE_HEIGHT][CONSOLE_WIDTH];
        for (int i = 0; i < CONSOLE_HEIGHT; i++) {
            for (int j = 0; j < CONSOLE_WIDTH; j++) {
                display[i][j] = 32;
            }
        }

        mutated = true;
    }

    private boolean parseArrowKeys(int first) {
        if (first != 27) {
            return false;
        }

        int second, third;

        try {
            second = in.read();
            third = in.read();
        } catch (IOException e) {
            return false;
        }

        if (second != 91) {
            return false;
        }

        switch (third) {
            case 65 -> arrow_press(UP);
            case 66 -> arrow_press(DOWN);
            case 67 -> arrow_press(RIGHT);
            case 68 -> arrow_press(LEFT);
            default -> {
                return false;
            }
        }

        return true;
    }

    private void parseInput() {
        int input;

        try {
            input = in.read();
            SideConsole.sendLog("INFO", "INPUT: " + input);
        } catch (IOException e) {
            SideConsole.sendLog("ERROR", "Failed to read input: " + e.getMessage());
            return;
        }

        boolean arrowParseSuccess = parseArrowKeys(input);
        SideConsole.sendLog("DEBUG", "Arrow key parse success: " + arrowParseSuccess);
        onKeyPress(input, arrowParseSuccess);
    }

    private void render() {
        draw(initial_render);
        initial_render = false;
        if (!mutated) {
            return;
        }

        clear();

        for (int i = 0; i < size.height; i++) {
            for (int j = 0; j < size.width; j++) {
                int val = display[i][j];
                if (val >= 256) {
                    char c = (char) (val - 256);
                    out.print(WHITE_BG + BLACK_TEXT + c + RESET);
                } else {
                    out.print((char) val);
                }
            }
            out.println();
        }
        out.flush();
    }

    public void run() {
        while (active) {
            screenChanged = size.updateTerminalSize();
            size.height = size.height - 1;
            SideConsole.sendLog("INFO", "Terminal size updated to " + size.width + "x" + size.height);
            checkDisplay();
            render();
            parseInput();
        }
    }
}
