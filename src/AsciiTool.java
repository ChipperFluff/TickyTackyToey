package src;

public class AsciiTool {

    public static int charToAscii(char ch) {
        return (int) ch;
    }

    public static char asciiToChar(int code) {
        if (code < 0 || code > 127) {
            throw new IllegalArgumentException("Invalid ASCII code: " + code);
        }
        return (char) code;
    }

    public static int[] stringToAscii(String input) {
        int[] asciiCodes = new int[input.length()];
        for (int i = 0; i < input.length(); i++) {
            asciiCodes[i] = charToAscii(input.charAt(i));
        }
        return asciiCodes;
    }

    public static String asciiToString(int[] codes) {
        StringBuilder sb = new StringBuilder();
        for (int code : codes) {
            sb.append(asciiToChar(code));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String original = "Jack a";
        int[] asciiArray = stringToAscii(original);
        System.out.print("ASCII codes: ");
        for (int code : asciiArray) {
            System.out.print(code + " ");
        }

        System.out.println("\nRestored: " + asciiToString(asciiArray));
    }
}

