package src;

public class Logger {
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }

    public void error(String message, Throwable t) {
        System.err.println("[ERROR] " + message);
        if (t != null) {
            t.printStackTrace();
        }
    }
}
