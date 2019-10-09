package com.huch.common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

public class Keys {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * 生产RSA 秘钥对
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Object> keyMap;
		try {
			keyMap = initKey();
			String publicKey =  getPublicKey(keyMap);
			System.out.println("public_key");
			System.out.println(publicKey);
			String privateKey =  getPrivateKey(keyMap);
			System.out.println("private_key");
			System.out.println(privateKey);
		} catch (Exception e) { 
			e.printStackTrace();
		}	 
	}
	
	 
	/**
	 * 取得公钥，并转化为String类型
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY); 
		byte[] publicKey = key.getEncoded(); 
		return encryptBASE64(key.getEncoded());
	}
	
	
	/**
	 * 取得私钥，并转化为String类型
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY); 
		byte[] privateKey =key.getEncoded(); 
		return encryptBASE64(key.getEncoded());
	}  

	public static byte[] decryptBASE64(String key) throws Exception {               
		return (new BASE64Decoder()).decodeBuffer(key);               
	}                                 

	public static String encryptBASE64(byte[] key) throws Exception {               
		return (new BASE64Encoder()).encodeBuffer(key);               
	}       

	/**
	 * 初始化秘钥
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		
		/*密钥的初始化长度为1024位，密钥的长度越长，安全性就越好，但是加密解密所用的时间就会越多。
		而一次能加密的密文长度也与密钥的长度成正比。一次能加密的密文长度为：密钥的长度/8-11。所
		以1024bit长度的密钥一次可以加密的密文为1024/8-11=117bit。所以非对称加密一般都用于加密
		对称加密算法的密钥，而不是直接加密内容。对于小文件可以使用RSA加密，但加密过程仍可能会使用
		分段加密。*/
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		
		//公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		//私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}
}