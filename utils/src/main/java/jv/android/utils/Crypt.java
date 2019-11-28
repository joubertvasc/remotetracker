// This code is based on site: http://exampledepot.com/egs/javax.crypto/PassKey.html

package jv.android.utils;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.AlgorithmParameterSpec;

public class Crypt {

	private Cipher ecipher;
	private Cipher dcipher;

    // 8-byte Salt
	private byte[] salt = {
        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
        (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
    };

    // Iteration count
	private int iterationCount = 19;

    public Crypt (String passPhrase) {
        try {
            // Create the key
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (java.security.InvalidAlgorithmParameterException e) {
        	Logs.errorLog("Crype", e);
        } catch (java.security.spec.InvalidKeySpecException e) {
        	Logs.errorLog("Crype", e);
        } catch (javax.crypto.NoSuchPaddingException e) {
        	Logs.errorLog("Crype", e);
        } catch (java.security.NoSuchAlgorithmException e) {
        	Logs.errorLog("Crype", e);
        } catch (java.security.InvalidKeyException e) {
        	Logs.errorLog("Crype", e);
        }
    }

    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return Base64.encodeBytes(enc);
        } catch (javax.crypto.BadPaddingException e) {
        	Logs.errorLog("Crype.encrypt", e);
        } catch (IllegalBlockSizeException e) {
        	Logs.errorLog("Crype.encrypt", e);
        } catch (UnsupportedEncodingException e) {
        	Logs.errorLog("Crype.encrypt", e);
        }
        return null;
    }

    public String decrypt(String str) {
        try {
            // Decode base64 to get bytes
            byte[] dec = Base64.decode(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
        	Logs.errorLog("Crype.decrypt", e);
        } catch (IllegalBlockSizeException e) {
        	Logs.errorLog("Crype.decrypt", e);
        } catch (UnsupportedEncodingException e) {
        	Logs.errorLog("Crype.decrypt", e);
        } catch (java.io.IOException e) {
        	Logs.errorLog("Crype.decrypt", e);
        }

        return null;
    }
}

/* Example
// Here is an example that uses the class
        try {
            // Create encrypter/decrypter class
            Crypt encrypter = new Crypt("My Pass Phrase!");

            // Encrypt
            String encrypted = encrypter.encrypt("Don't tell anybody!");
    		Toast.makeText(this, encrypted, Toast.LENGTH_LONG).show(); 

            // Decrypt
            String decrypted = encrypter.decrypt(encrypted);
    		Toast.makeText(this, decrypted, Toast.LENGTH_LONG).show(); 
        } catch (Exception e) {
        }
/**/