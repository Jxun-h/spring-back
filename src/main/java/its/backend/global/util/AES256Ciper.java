package its.backend.global.util;

import org.apache.commons.lang3.ObjectUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class AES256Ciper {
    private static volatile AES256Ciper INSTANCE;

    final static String SECRET_KEY = "1q2w3e4r!@#$";
    static String IV = "";
    final static int ITERATION = 2;

    public static AES256Ciper getINSTANCE() {
        if (ObjectUtils.isEmpty(INSTANCE)) {
            synchronized (AES256Ciper.class) {
                if (ObjectUtils.isEmpty(INSTANCE)) {
                    INSTANCE = new AES256Ciper();
                }
            }
        }
        return INSTANCE;
    }

    private AES256Ciper() {
        IV = SECRET_KEY.substring(0, 16);
    }

    /**
     * 암호화
     * @param str
     * @return String 암호화 문자열
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String encrypt(String str)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        String salt = UUID.randomUUID().toString();
        byte[] keydata = SECRET_KEY.substring(16).getBytes();
        SecretKey key = new SecretKeySpec(keydata, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes()));

        String valtoEnc;
        String eVal = str;

        for (int i = 0; i < ITERATION; i++) {
            valtoEnc = salt + eVal;
            byte[] encrypted = cipher.doFinal(valtoEnc.getBytes(StandardCharsets.UTF_8));
            eVal = new String(Base64.getEncoder().encode(encrypted));
        }
        return eVal;
    }

    public static String decrypt(String str)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        String salt = UUID.randomUUID().toString();
        byte[] keydata = SECRET_KEY.substring(16).getBytes();
        SecretKey key = new SecretKeySpec(keydata, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes()));

        String dVal;
        String valToDec = str;

        for (int i = 0; i < ITERATION; i++) {
            byte[] decrypted = Base64.getDecoder().decode(valToDec.getBytes());
            dVal = new String(cipher.doFinal(decrypted), StandardCharsets.UTF_8).substring(salt.length());
            valToDec = dVal;
        }
        return valToDec;
    }
}
