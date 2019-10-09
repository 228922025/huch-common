
package com.huch.common.crypto;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA{

	public static final String KEY_ALGORTHM="RSA";//
	public static final String SIGNATURE_ALGORITHM="MD5withRSA";
	
	public static final String PUBLIC_KEY = "RSAPublicKey";//公钥
	public static final String PRIVATE_KEY = "RSAPrivateKey";//私钥
	
	public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";
	
	/**
	* RSA 私钥签名
	* @param content 待签名数据
	* @param privateKey 私钥
	* @param inputCharset 编码格式
	* @return 签名值
	*/
	public static String sign(String content, String privateKey, String inputCharset)
	{
        try 
        {
        	PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec( Base64.decode(privateKey) );
        	KeyFactory keyf 				= KeyFactory.getInstance("RSA");
        	PrivateKey priKey 				= keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update( content.getBytes(inputCharset) );

            byte[] signed = signature.sign();
            
            return Base64.encode(signed);
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
        
        return null;
    }
	
	/**
	* RSA 公钥验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param publicKey 公钥
	* @param inputCharset 编码格式
	* @return 布尔值
	*/
	public static boolean verify(String content, String sign, String publicKey, String inputCharset)
	{
		try 
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        byte[] encodedKey = Base64.decode(publicKey);
	        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

		
			java.security.Signature signature = java.security.Signature
			.getInstance(SIGN_ALGORITHMS);
		
			signature.initVerify(pubKey);
			signature.update( content.getBytes(inputCharset) );
		
			boolean bverify = signature.verify( Base64.decode(sign) );
			return bverify;
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * RSA 公钥加密
	 * @param content 待加密数据
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(String content, String publicKey, String inputCharset)throws Exception{
		//Key pubkey = getPublicKey(publicKey);
		
		//对公钥解密
		byte[] keyBytes = Base64.decode(publicKey);
		//取公钥
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		Key pubkey = keyFactory.generatePublic(x509EncodedKeySpec);
		
		//对数据解密
		Cipher cipher = Cipher.getInstance("RSA");
		//Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubkey);
		
		InputStream ins = new ByteArrayInputStream(content.getBytes());
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        
        writer.write(cipher.doFinal(content.getBytes()));
        
        return cipher.doFinal(content.getBytes());
		
		

//        return new String(cipher.doFinal(content.getBytes()), "utf-8");
	}
	
	/**
	* 私钥解密
	* @param content 密文
	* @param privateKey 私钥
	* @param inputCharset 编码格式
	* @return 解密后的字符串
	*/
	public static String decrypt(String content, String privateKey, String inputCharset) throws Exception {
        PrivateKey prikey = getPrivateKey(privateKey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }
            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), inputCharset);
    }

	
	/**
	* 得到私钥
	* @param key 密钥字符串（经过base64编码）
	* @throws Exception
	*/
	public static PrivateKey getPrivateKey(String key) throws Exception {
		//解密私钥
		byte[] keyBytes = Base64.decode(key);
		
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		
		return privateKey;
	}
	
	/**
	 * 得到公钥
	 * @param key 密钥字符串（经过base64编码）
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		//解密公钥
		byte[] keyBytes = Base64.decode(key);
		
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

		return publicKey;
	}
}
