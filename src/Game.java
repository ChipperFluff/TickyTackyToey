package src;

public class Game extends Console {
    private int selection = 1;
    private int size = 120;
    private int width = 6;
    private final int halfWidth = width/2;


    protected void arrow_press(String direction) {
        if (direction.equals(RIGHT)) {
            if (selection + halfWidth < size - 1) {
                selection++;
            }
            return;
        }

        if (direction.equals(LEFT)) {
            if (selection - halfWidth > 0) {
                selection--;
            }
            return;
        }
    }


    protected void onKeyPress(int key, boolean isArrowKey) {
    }

    protected void draw() {
        for (int y = 0; y<width; y++) {
            for (int i = 0; i<size; i++) {
            if (selection >= i - halfWidth && selection <= i + halfWidth) {
                    invert_out("X");
                    continue;
                }
                out.print("X");
            }
            out.println();
        }
    }
}
