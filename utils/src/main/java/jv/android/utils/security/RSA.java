package jv.android.utils.security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class RSA {

	public static PublicKey getPublicKeyFromFile(String keyFileName) throws IOException {
		InputStream in = new FileInputStream(keyFileName);
		ObjectInputStream oin =	new ObjectInputStream(new BufferedInputStream(in));

		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);

			return pubKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}

	/**
	 * Creates an X509Certificate from a public key String. The key string must 
	 * look like
	 * <code>
	 * -----BEGIN CERTIFICATE-----<br />
	 * ... content encoded in base 64 ...<br />
	 * -----END CERTIFICATE-----<br />
	 * </code>
	 */
	public static PublicKey getPublicKeyFromString(String key) throws Exception {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream( key.getBytes()));
		PublicKey pk = cert.getPublicKey();

		return pk;
	} 

	public static PrivateKey getPrivateKeyFromString(String key) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedPrivateKey = key.getBytes();
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		
		return keyFactory.generatePrivate(privateKeySpec);
	}

	public static void GenerateKeyPair(int keySize, String publicKeyFileName, String privateKeyFileName)
	{       
		try{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(keySize); //(2048);
			KeyPair kp = kpg.genKeyPair();

			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
			RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);

			saveToFile(publicKeyFileName, pub.getModulus(), pub.getPublicExponent());
			saveToFile(privateKeyFileName, priv.getModulus(), priv.getPrivateExponent());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	private static void saveToFile(String fileName,	BigInteger mod, BigInteger exp) throws Exception {
		ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
		try {
			oout.writeObject(mod);
			oout.writeObject(exp);
		} catch (Exception e) {
			throw new Exception("error", e);
		} finally {
			oout.close();
		}
	}

	/**
	 * RSA encrypt function (RSA / ECB / PKCS1-Padding)
	 * 
	 * @param original
	 * @param key
	 * @return
	 */
	public static byte[] rsaEncrypt(byte[] original, PublicKey key)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			return cipher.doFinal(original);
		}
		catch(Exception e)
		{
			//  Logger.e(e.toString());
		}

		return null;
	}

	public static byte[] rsaEncrypt(byte[] original, String base64PublicKey) throws Exception {
		PublicKey publicKey = getPublicKeyFromString(base64PublicKey);

		return rsaEncrypt (original, publicKey);
	}

	/**
	 * RSA decrypt function (RSA / ECB / PKCS1-Padding)
	 * 
	 * @param encrypted
	 * @param key
	 * @return
	 */
	public static byte[] rsaDecrypt(byte[] encrypted, PrivateKey key)
	{
		try
		{ 		
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);

			return cipher.doFinal(encrypted);
		}
		catch(Exception e)
		{
			//  Logger.e(e.toString());
		}

		return null;
	}

	public static byte[] rsaDecrypt(byte[] encrypted, String base64PublicKey)  throws Exception {
		PrivateKey privateKey = getPrivateKeyFromString(base64PublicKey);

		return rsaDecrypt (encrypted, privateKey);
	}
}
