package util;

public final class CampoObrigatorio {
    public static String requireNonBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Campo obrigatório.");
        }
        return value;
    }
    public static boolean isNotBlank(final String value) {
        return !(value == null || value.isBlank());
    }
    public static boolean isBlank(final String value) {
        return (value == null || value.isBlank());
    }
    public static String requireMinSize(final String value, final int minSize) {
        if (value == null || value.isBlank() || value.length() < minSize) {
            throw new IllegalArgumentException("Campo não atende ao tamanho mínimo de " + minSize + " caracteres.");
        }
        return value;
    }
}
