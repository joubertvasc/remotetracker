package jv.android.utils.random;

import java.util.Locale;
import java.util.Random;

/**
 * Created by joubert on 06/01/18.
 */

public class KeyGenerator {

    private static String chars = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String generateAlphaNumeric(int size) {
        String result = "";

        Random randomGenerator = new Random();
        for (int i = 1; i <= size; i++) {
            int r = randomGenerator.nextInt(36);

            result += chars.substring(r, r+1);
        }

        return result;
    }

    public static String generateAlphaNumericUpperCase(int size) {
        return generateAlphaNumeric(size).toUpperCase(Locale.getDefault());
    }

    public static String generateAlphaNumericLowerCase(int size) {
        return generateAlphaNumeric(size).toLowerCase(Locale.getDefault());
    }

    public static String generate(int size) {
        String result = "";

        Random randomGenerator = new Random();

        for (int i = 1; i <= size; i++) {
            int r = randomGenerator.nextInt(25) + 65;

            result += (char)r;
        }

        return result;
    }

    public static String generateUppercase(int size) {
        return generate(size).toUpperCase(Locale.getDefault());
    }

    public static String generateLowercase(int size) {
        return generate(size).toLowerCase(Locale.getDefault());
    }
}
