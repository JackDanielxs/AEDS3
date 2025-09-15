package util;

public class IsNumber {
    public static boolean validate(final String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) { return false; }
    }
}
