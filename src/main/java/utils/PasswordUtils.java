package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe che fornisce metodi per l'hashing delle password
 */


public class PasswordUtils {

    /**
     * Esegue l'hashing delle password, utilizzando l'algoritmo {@code SHA-256}
     *
     * @param password
     * @return La password 'hashed'
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
