package jv.android.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512 {

    public static int PW_HASH_ITERATION_COUNT = 5000;
    private static MessageDigest md;

    public static String sha512(String[] args) {
        String pw = "teüöäßÖst1";
        String salt = "e33ptcbnto8wo8c4o48kwws0g8ksck0";

        try {
            md = MessageDigest.getInstance("SHA-512");

            return hashPw(pw, salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            return "";
        }
    }

    private static String hashPw(String pw, String salt) {
        byte[] bSalt;
        byte[] bPw;

        try {
            bSalt = salt.getBytes("UTF-8");
            bPw = pw.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding", e);
        }

        byte[] digest = run(bPw, bSalt);
        for (int i = 0; i < PW_HASH_ITERATION_COUNT - 1; i++) {
            digest = run(digest, bSalt);
        }

        return Base64.encodeBytes(digest);
    }

    private static byte[] run(byte[] input, byte[] salt) {
        md.update(input);
        return md.digest(salt);
    }

}
