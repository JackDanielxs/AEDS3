package util;

public class CampoObrigatorio {
    public static String require(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Não pode ser null ou vazio");
        }
        return value;
    }

    public static boolean isValid(final String value) {
        return !(value == null || value.isBlank());
    }

    public static boolean isNotValid(final String value) {
        return (value == null || value.isBlank());
    }
}