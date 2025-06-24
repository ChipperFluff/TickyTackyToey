package src;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public abstract class Console {
    protected InputStream in = System.in;
    protected PrintStream out = System.out;
    private boolean active = true;

    protected void exit() {
        active = false;
    }

    abstract protected void arrow_press(String direction);
    abstract protected void onKeyPress(int key, boolean isArrowKey);

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
                arrow_press("up");
                break;
            case 66:
                arrow_press("down");
                break;
            case 67:
                arrow_press("right");
                break;
            case 68:
                arrow_press("left");
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
            parseInput();
       }
            
    }
}


