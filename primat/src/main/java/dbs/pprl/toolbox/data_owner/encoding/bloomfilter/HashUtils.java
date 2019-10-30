package dbs.pprl.toolbox.data_owner.encoding.bloomfilter;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.BitSet;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * Utility class for calculating hash values.
 * 
 * @author mfranke
 */
public class HashUtils {
	public enum HashingAlgorithm{
		MD2("MD2"),
		MD5("MD5"),
		SHA1("SHA-1"),
		SHA256("SHA-256"),
		SHA384("SHA-384"),
		SHA512("SHA-512");
		
		private String name;
		
		HashingAlgorithm(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
	}
		
	private static final String MD5 = "MD5";
	private static final String SHA = "SHA";
	
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
	
	private HashUtils(){
		throw new RuntimeException();
	}
	
	public static int getHashInt(String data, HashingAlgorithm algorithm) {
		return getDigest(data, algorithm.toString());
	}
	
	public static String getHashHex(String data, HashingAlgorithm algorithm) {
		return getDigestHex(data, algorithm.toString());
	}
	
	
	/**
	 * 
	 * @param data
	 * @param algorithm i.e. md2, md5, sha-1, sha-256, sha-384, sha-512
	 * @return
	 */
	public static int getDigest(String data, String algorithm) {
		System.out.println("DATA: " + data + "..." + algorithm);
		final byte[] digest = DigestUtils.getDigest(algorithm).digest(StringUtils.getBytesUtf8(data));
		final BigInteger val = new BigInteger(digest);
		return val.intValue();
	}
	
	public static String getDigestHex(String data, String algorithm) {
		final byte[] digest = DigestUtils.getDigest(algorithm).digest(StringUtils.getBytesUtf8(data));
		return Hex.encodeHexString(digest);
	}
	
	//TODO: Clean up the following 4 methods ??
	public static String calculateHmacSHA1(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException{
		final SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
		final Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		final byte[] bytes = mac.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(bytes);
	}
	
	
	public static String calculateHmacSHA256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException{
		final SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);
		final Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
		mac.init(signingKey);
		final byte[] bytes = mac.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(bytes);
	}
	
	public static long getHmacSHA1(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException{
		final SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
		final Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		final byte[] bytes = mac.doFinal(data.getBytes());
		final BigInteger bigInt = new BigInteger(1, bytes);
		return bigInt.longValue();
	}
	
	
	public static long getHmacSHA256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException{
		final SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);
		final Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
		mac.init(signingKey);
		final byte[] bytes = mac.doFinal(data.getBytes());
		final BigInteger bigInt = new BigInteger(1, bytes);
		return bigInt.longValue();
	}
	
	/**
	 * Calculates the MD5 hash for a string input. 
	 * @param input a String value. 
	 * @return the {@link Int} representation of the MDH5 hash value.
	 */
	public static int getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance(MD5);
			byte[] messageDigest = md.digest(input.getBytes());            
			BigInteger number = new BigInteger(1, messageDigest);
			return number.intValue();
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Calculates the SHA hash for a string input.
	 * @param input a String value.
	 * @return the {@link Int} representation of the SHA hash value.
	 */
	public static int getSHA(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance(SHA);
			byte[] messageDigest = md.digest(input.getBytes());            
			BigInteger number = new BigInteger(1, messageDigest);
			return number.intValue();
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Calculates the SHA hash for a string input.
	 * @param input a String value.
	 * @return the {@link long} representation of the SHA hash value.
	 */	
	public static long getSHALongHash(String input){
		try{
			MessageDigest md = MessageDigest.getInstance(SHA);
			byte[] messageDigest = md.digest(input.getBytes());            
			BigInteger number = new BigInteger(1, messageDigest);
			return number.longValue();
		}
		catch (NoSuchAlgorithmException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Calculates the MD5 hash for a BitSet input.
	 * @param input a {@link BitSet} object.
	 * @return the {@link BitSet} representation of the MD5 hash value.
	 */
	public static int getMD5(BitSet input) {
		try {
			MessageDigest md = MessageDigest.getInstance(MD5);
			byte[] messageDigest = md.digest(input.toByteArray());            
			BigInteger number = new BigInteger(1, messageDigest);
			return number.intValue();
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Calculates the SHA hash for a BitSet input.
	 * @param input a {@link BitSet} object.
	 * @return the {@link Int} representation of the SHA hash value.
	 */
	public static int getSHA(BitSet input) {
		try {
			MessageDigest md = MessageDigest.getInstance(SHA);
			byte[] messageDigest = md.digest(input.toByteArray());
			BigInteger number = new BigInteger(1, messageDigest);
			return number.intValue();
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}