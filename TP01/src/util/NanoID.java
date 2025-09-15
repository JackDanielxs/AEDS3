package util;
import java.security.SecureRandom;

public class NanoID {
    private static final String ALPHABET = "useandom-26T198340PX75pxJACKVERYMINDBUSHWOLF_GQZbfghjklqvwyzrict";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String nanoid(int length) {
        StringBuilder sb = new StringBuilder(length);
        byte[] buffer = new byte[length];

        RANDOM.nextBytes(buffer);

        for (int i = 0; i < length; i++) {
            int index = buffer[i] & 63;
            sb.append(ALPHABET.charAt(index));
        }

        return sb.toString();
    }

    public static String nanoid() { return nanoid(10); }
}
