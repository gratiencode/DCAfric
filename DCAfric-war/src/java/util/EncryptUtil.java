/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author PC
 */
public class EncryptUtil {

    private final String iv = "fe5c8a9w7s5d321a";
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    private final String secretKey = "a123d5s7w9a8c5ef";
    public static final String PRIVATE_KEY ="MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCky3whpYASDAUXVl0MhMksvaSX0QpKpamaCV67kXxs1WsOtJ8V6bRemnEkdHL0JUDUspzD0JlFHV4UCy5W4O7z2SvMXfc9BJrv9rAaxRmFmvdAzhHIzX6TWAiyV2zEAkJdZ0RB+oxLhVda1ojkdl/Kpglu2TMetTbiey6DMLaBL+J4YjSaaLx6fs8Lsyc94DfkjeomSjonbi42+pyGAN7fVapfkNYtztyzE/XF2fBnoXgupsCnAaQWEb9848Q4xhNasnhpYlqSizdFopQCTYQIvuKD1jVhbUXm5fsgInCNoKrNMT7x9fV1tnPCTTlI8OTWv6UWse5hZFFCyipywrc/AgMBAAECggEAH2gQbjEmaUoVJk91SE9+L6Ks3aofNf7AhfTHMyQU/IDn+FOABiMW\nFvmEGph+1/zJkD/CFKeKrltcdKF04WXY5Gw9JqO53M0l4hm3kkKCDK5t9NHRHd3cA6TBc4up1OlBUJ+/qHthhXsSHOPKDxBoTSy7m36VeYK7xQoI4UPFZHU2/U751CR391gcvDTFmUTTIlRqTz3AlN86n+T7K2IgCsKn9nDUQQXmQ+JxAPQMCQaLM4/jAt+bkeAFtvb3SMamRdetwWjAD1sDPWr7ao9vMN8NaiDDpviPCNRpaUMtNPytsPFEQ8YqiXEWV98KyrJgHqy8rnzHzy/1e4u3Zt6aMQKBgQDmOGzdN9f8WCrNPTDqXQeNgwC4p+8X5JX+c7CP1jXcKbaMitWGVXswApLb0dx+XQDaUeo9YV0njvBeZ0GsJD15RuJZkAodvqI1JukuM2AcoAwuByaeeG3fYAb92tNL7soUTwGdyvG9L37vMfr5nxWOyovy0Iozq3HXHH56BLBjuwKBgQC3P4yU2e4rvnfVSC96M+OaNMvwsOYaYLDtAOjVdkDq6l1uZnxfq9i19VEUouz8\nsrocCTD2KMVag7ICneuWy1JjTQo3LSohsDl3BcvJTqS6k7xKnpVRT0+0jHUZmnrp6llsRzHw/CuRCB7YNdzfavMtXH1AKn7/Ndl1MxoRh6aoTQKBgGtOc5etFtQ8+D5PvkQO9p1EKYroY648UpmWrP5uNw6WtsrNT3dc0p1vJaqmJ+MSUHpyYf8YKcZtfqZR9K9a+PhVTN24IK76kzq5F0j8k1jkVKkDlkGZGoVaige6/m/PLovGw5cb6A9pWv38yuGc//xddlvIThKFX/uHAYC4PMtPAoGAOoAFJzfzeu+RXftvfLm3/XDBcpNmwiEAGolhy1O3Ice0EByTtA8uXdh1C/t/YKTGGVEt2kflySGl4IP9w+Qg5yafDkJS4vJZRhAJoLiEijPDtqat0IlbTq5Qa94PQ0HXTmPkI6S9W82V2zxGyORhwad3LNbkOCE+zNWFivcdUzkCgYB474o0VxKDvkBQ78KWlVK0eIBsoipqYjFayJQYF96xXxfO78axfClANdsLM4c4QX+hYrwXLRdqVcHCz9xxMYYNXqyI/qilaMlLeDjAPGEEBHfigp2PQ4huQUOidb4UVHWJTMNa+nzUYvoYntRRq4UsKz6JgEVg1UzaYcGcMXBL2w==";
    public static final String SERVER_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApMt8IaWAEgwFF1ZdDITJLL2kl9EKSqWpmgleu5F8bNVrDrSfFem0XppxJHRy9CVA1LKcw9CZRR1eFAsuVuDu89krzF33PQSa7/awGsUZhZr3QM4RyM1+k1gIsldsxAJCXWdEQfqMS4VXWtaI5HZfyqYJbtkzHrU24nsugzC2gS/ieGI0mmi8en7PC7MnPeA35I3qJko6J24uNvqchgDe31WqX5DWLc7csxP1xdnwZ6F4LqbApwGkFhG/fOPEOMYTWrJ4aWJakos3RaKUAk2ECL7ig9Y1YW1F5uX7ICJwjaCqzTE+8fX1dbZzwk05SPDk1r+lFrHuYWRRQsoqcsK3PwIDAQAB";
//    public static final String CLIENT_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArOsZ8cdd9tKSEIuGAER/lEepBqJhU/LJ"
//                                                + "zDdnx0krfN1SCx5k6FYQnD7i+uozYxkSV322cmrCdn5Dcb5TN2QQYMakmfF9x1OoPynEaiT3h2cz"
//                                                + "ysU4G4arosMuUX3YpsTooJ9Jro7Yg9YuNlaJLAq84Ah3TD57TJO4Fe3te8y+Gf8oWfPjfjr+zXfE"
//                                                + "9but+vinPexjxxklLiRMu+x035m8ThQpEVPNuebRtx7aMHN+esww6Va4fwG0oIUSSj21hVg9WHSz"
//                                                + "tvC+T6HPczU3RP0/ikVBLGqRTSXBX5H4OBB+IQkEoyRF4SzTkJOuDiROGJRKVERGJ9JibkezhkkA"
//                                                + "sx8DgwIDAQAB";

    public static String getBase64InitialPrivateKey() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            PrivateKey privateKey = kp.getPrivate();
            String result = Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    public EncryptUtil() {
        //AES/CBC/PKCS5Padding
        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(secretKey.getBytes(), "AES");
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getBase64InitialPublicKey() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            PublicKey publicKey = kp.getPublic();
            String result = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getKey(String filename) throws IOException {
        // Read key from file
        String strKeyPEM = "";
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM += line + "\n";
        }
        br.close();
        return strKeyPEM;
    }

    /**
     * Constructs a private key (RSA) from the given file
     *
     * @param filename PEM Private Key
     * @return RSA Private Key
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static RSAPrivateKey getPrivateKey(String filename) throws IOException, GeneralSecurityException {
        String privateKeyPEM = getKey(filename);
        return getPrivateKeyFromString(privateKeyPEM);
    }

    /**
     * Constructs a private key (RSA) from the given string
     *
     * @param key PEM Private Key
     * @return RSA Private Key
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static RSAPrivateKey getPrivateKeyFromString(String key) throws IOException, GeneralSecurityException {
        String privateKeyPEM = key;

        // Remove the first and last lines
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");

        // Base64 decode data
        byte[] encoded = Base64.decode(privateKeyPEM, Base64.DEFAULT);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));
        return privKey;
    }

    /**
     * Constructs a public key (RSA) from the given file
     *
     * @param filename PEM Public Key
     * @return RSA Public Key
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static RSAPublicKey getPublicKey(String filename) throws IOException, GeneralSecurityException {
        String publicKeyPEM = getKey(filename);
        return getPublicKeyFromString(publicKeyPEM);
    }

    /**
     * Constructs a public key (RSA) from the given string
     *
     * @param key PEM Public Key
     * @return RSA Public Key
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static RSAPublicKey getPublicKeyFromString(String key) throws IOException, GeneralSecurityException {
        String publicKeyPEM = key;

        // Remove the first and last lines
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

        // Base64 decode data
        byte[] encoded = Base64.decode(publicKeyPEM, Base64.DEFAULT);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        return pubKey;
    }
    
    
    public static String encryptToString(String rawText, RSAPublicKey publicKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeToString(cipher.doFinal(rawText.getBytes("UTF-8")), Base64.DEFAULT);
    }
    
    public static byte[] decrypt(String cipherText, RSAPrivateKey privateKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT));
    
    }
     public static String decryptString(String cipherText, RSAPrivateKey privateKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT)),"UTF-8");
    }

    /**
     * @param privateKey
     * @param message
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    public static String sign(PrivateKey privateKey, String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(message.getBytes("UTF-8"));
        return new String(Base64.decode(new String(sign.sign()), Base64.DEFAULT), "UTF-8");
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }

    /**
     * @param publicKey
     * @param message
     * @param signature
     * @return
     * @throws SignatureException
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    public static boolean verify(PublicKey publicKey, String message, String signature) throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(publicKey);
        sign.update(message.getBytes("UTF-8"));
        return sign.verify(Base64.decode(new String(signature.getBytes("UTF-8")), Base64.DEFAULT));
    }

    /**
     * Encrypts the text with the public key (RSA)
     *
     * @param rawText Text to be encrypted
     * @param publicKey
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static String encrypt(String rawText, PublicKey publicKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeToString(cipher.doFinal(rawText.getBytes("UTF-8")), Base64.DEFAULT);
    }

    /**
     * Decrypts the text with the private key (RSA)
     *
     * @param cipherText Text to be decrypted
     * @param privateKey
     * @return Decrypted text (Base64 encoded)
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static String decrypt(String cipherText, PrivateKey privateKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT)), "UTF-8");
    }

    public byte[] encrypt(String text) throws Exception {
        if (text == null || text.length() == 0) {
            throw new Exception("Empty string");
        }

        byte[] encrypted = null;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            encrypted = cipher.doFinal(padString(text).getBytes());
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }

        return encrypted;
    }

    public byte[] decrypt(String code) throws Exception {
        if (code == null || code.length() == 0) {
            throw new Exception("Empty string");
        }

        byte[] decrypted = null;
        String s = new String(Base64.decode(code, Base64.DEFAULT));
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
//hexToBytes(s)
            decrypted = cipher.doFinal(Base64.decode(code, Base64.DEFAULT));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public static String bytesToHex(byte[] b) {
        StringBuilder buf = new StringBuilder();
        int len = b.length;
        for (int j = 0; j < len; j++) {
            buf.append(byteToHex(b[j]));
        }
        return buf.toString();
    }

    public static String byteToHex(byte b) {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] a = {hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f]};
        return new String(a);
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    private static String padString(String source) {
        char paddingChar = 0;
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }

        return source;
    }
}
