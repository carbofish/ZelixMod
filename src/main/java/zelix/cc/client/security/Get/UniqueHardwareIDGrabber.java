package zelix.cc.client.security.Get;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

 public class UniqueHardwareIDGrabber {
    public static String getMD5() {
        return getMD5((System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name")).trim());
    }

    public static String getSHA1() {
        return getSHA1((System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name")).trim());
    }

    public static String getSHA256() {
        return getSHA256((System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name")).trim());
    }

    public static String getSHA512() {
        return getSHA512((System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name")).trim());
    }

    private static String getMD5(String input) {
        String res = "";
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(input.getBytes());
            byte[] md5 = algorithm.digest();
            String tmp = "";
            for (int i = 0; i < md5.length; i++) {
                tmp = Integer.toHexString(0xFF & md5[i]);
                if (tmp.length() == 1) {
                    res = res + "0" + tmp;
                } else {
                    res = res + tmp;
                }
            }
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
        return res;
    }

    private static String getSHA1(String input) {
        String res = "";
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA");
            algorithm.reset();
            algorithm.update(input.getBytes());
            byte[] sha1 = algorithm.digest();
            String tmp = "";
            for (int i = 0; i < sha1.length; i++) {
                tmp = Integer.toHexString(0xFF & sha1[i]);
                if (tmp.length() == 1) {
                    res = res + "0" + tmp;
                } else {
                    res = res + tmp;
                }
            }
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
        return res;
    }

    private static String getSHA256(String input) {
        String res = "";
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            algorithm.reset();
            algorithm.update(input.getBytes());
            byte[] sha256 = algorithm.digest();
            String tmp = "";
            for (int i = 0; i < sha256.length; i++) {
                tmp = Integer.toHexString(0xFF & sha256[i]);
                if (tmp.length() == 1) {
                    res = res + "0" + tmp;
                } else {
                    res = res + tmp;
                }
            }
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
        return res;
    }

    private static String getSHA512(String input) {
        String res = "";
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(input.getBytes());
            byte[] sha512 = algorithm.digest();
            String tmp = "";
            for (int i = 0; i < sha512.length; i++) {
                tmp = Integer.toHexString(0xFF & sha512[i]);
                if (tmp.length() == 1) {
                    res = res + "0" + tmp;
                } else {
                    res = res + tmp;
                }
            }
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
        return res;
    }
}
