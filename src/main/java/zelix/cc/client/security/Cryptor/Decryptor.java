package zelix.cc.client.security.Cryptor;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class Decryptor {
    private static final String Algorithm = "AES";

    private static final byte[] keyValue = new byte[] {'S', 'A', 'l', 'L', 'o', 's', 'I', 's', 't', 'O', 'o', 'P', 'f', 'o', 'R', 'u' };

    public static String decrypt(String encryptedData, int messageNumber) throws Exception {
        Key key = generateKey();
        IvParameterSpec ivSpec = createCtrlvForAES(messageNumber);
        Cipher c = Cipher.getInstance("AES/CTR/PKCS5PADDING");
        c.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, Algorithm);
        return key;
    }
    public static IvParameterSpec createCtrlvForAES(int messageNumber) {
        byte[] ivBytes = new byte[16];
        ivBytes[0] = (byte) (messageNumber >> 32);
        ivBytes[1] = (byte) (messageNumber >> 24);
        ivBytes[2] = (byte) (messageNumber >> 16);
        ivBytes[3] = (byte) (messageNumber >> 8);
        ivBytes[4] = (byte) (messageNumber >> 0);
        for (int i = 0; i != 8; i++) {
            ivBytes[8 + i] = 0;
        }
        ivBytes[15] = 1;
        return new IvParameterSpec(ivBytes);
    }
}
