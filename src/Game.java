package src;

import console.SideConsole;

public class Game extends Console {
    private final char[][] board = new char[3][3];
    private int selectedX = 0;
    private int selectedY = 0;
    private boolean isXTurn = true;
    private boolean gameOver = false;
    private char winner = '\0';

    public Game() {
        SideConsole.sendLog("INFO", "Tic Tac Toe started");
    }

    @Override
    protected void arrow_press(String direction) {
        if (gameOver) return;

        switch (direction) {
            case UP -> selectedY = (selectedY + 2) % 3;
            case DOWN -> selectedY = (selectedY + 1) % 3;
            case LEFT -> selectedX = (selectedX + 2) % 3;
            case RIGHT -> selectedX = (selectedX + 1) % 3;
        }

        SideConsole.sendLog("INPUT", "Moved to " + selectedX + "," + selectedY);
    }

    @Override
    protected void onKeyPress(int key, boolean isArrowKey) {
        if (gameOver || isArrowKey) return;

        if (key == '\n' || key == 10 || key == 13) {
            if (board[selectedY][selectedX] == '\0') {
                board[selectedY][selectedX] = isXTurn ? 'X' : 'O';
                SideConsole.sendLog("MOVE", "Placed " + board[selectedY][selectedX] + " at " + selectedX + "," + selectedY);
                isXTurn = !isXTurn;
                checkWinner();
            } else {
                SideConsole.sendLog("WARN", "Cell already taken");
            }
        }
    }

    @Override
    protected void draw(boolean init) {
        int cellWidth = Math.max(5, size.width / 10);
        int cellHeight = Math.max(3, size.height / 6);

        int gridWidth = cellWidth * 3;
        int gridHeight = cellHeight * 3;

        int startX = (size.width - gridWidth) / 2;
        int startY = (size.height - gridHeight) / 2;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int cx = startX + x * cellWidth;
                int cy = startY + y * cellHeight;

                // Clear cell
                for (int dy = 0; dy < cellHeight; dy++) {
                    for (int dx = 0; dx < cellWidth; dx++) {
                        setChar(' ', cx + dx, cy + dy);
                    }
                }

                // Draw border
                for (int dx = 0; dx < cellWidth; dx++) {
                    setChar('-', cx + dx, cy);
                    setChar('-', cx + dx, cy + cellHeight - 1);
                }
                for (int dy = 0; dy < cellHeight; dy++) {
                    setChar('|', cx, cy + dy);
                    setChar('|', cx + cellWidth - 1, cy + dy);
                }

                // Draw symbol
                char c = board[y][x];
                if (c != '\0') {
                    drawSymbol(c, cx + 1, cy + 1, cellWidth - 2, cellHeight - 2);
                }

                // Highlight selected cell
                if (x == selectedX && y == selectedY && !gameOver) {
                    for (int dx = 1; dx < cellWidth - 1; dx++) {
                        invertChar(' ', cx + dx, cy + 1);
                        invertChar(' ', cx + dx, cy + cellHeight - 2);
                    }
                    for (int dy = 1; dy < cellHeight - 1; dy++) {
                        invertChar(' ', cx + 1, cy + dy);
                        invertChar(' ', cx + cellWidth - 2, cy + dy);
                    }
                }
            }
        }

        if (gameOver) {
            String msg = (winner != '\0') ? (winner + " wins!") : "Draw!";
            int gx = (size.width - msg.length()) / 2;
            int gy = startY + gridHeight + 1;
            for (int i = 0; i < msg.length(); i++) {
                setChar(msg.charAt(i), gx + i, gy);
            }
        }
    }

    private void drawSymbol(char c, int x, int y, int w, int h) {
        if (c == 'X') {
            for (int i = 0; i < Math.min(w, h); i++) {
                setChar('X', x + i, y + i);
                setChar('X', x + (w - 1 - i), y + i);
            }
        } else if (c == 'O') {
            for (int i = 0; i < w; i++) {
                setChar('O', x + i, y);
                setChar('O', x + i, y + h - 1);
            }
            for (int j = 0; j < h; j++) {
                setChar('O', x, y + j);
                setChar('O', x + w - 1, y + j);
            }
        }
    }

    private void checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (eq(board[i][0], board[i][1], board[i][2])) {
                winner = board[i][0];
                gameOver = true;
                SideConsole.sendLog("GAME", "Row " + i + " winner: " + winner);
                return;
            }
            if (eq(board[0][i], board[1][i], board[2][i])) {
                winner = board[0][i];
                gameOver = true;
                SideConsole.sendLog("GAME", "Col " + i + " winner: " + winner);
                return;
            }
        }

        if (eq(board[0][0], board[1][1], board[2][2])) {
            winner = board[0][0];
            gameOver = true;
            SideConsole.sendLog("GAME", "Diagonal winner: " + winner);
            return;
        }

        if (eq(board[0][2], board[1][1], board[2][0])) {
            winner = board[0][2];
            gameOver = true;
            SideConsole.sendLog("GAME", "Antidiagonal winner: " + winner);
            return;
        }

        boolean full = true;
        for (char[] row : board) {
            for (char c : row) {
                if (c == '\0') full = false;
            }
        }
        if (full) {
            gameOver = true;
            SideConsole.sendLog("GAME", "Draw detected");
        }
    }

    private boolean eq(char a, char b, char c) {
        return a != '\0' && a == b && b == c;
    }
}
