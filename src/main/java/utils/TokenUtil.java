package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TokenUtil {

    private static final String SECRET_KEY = "your_secret_key_here";

    public static String generateToken(String username) throws NoSuchAlgorithmException {
        long timestamp = System.currentTimeMillis();
        String data = username + ":" + timestamp;

        // Generate a simple signature
        String signature = createSignature(data);

        return Base64.getEncoder().encodeToString(signature.getBytes(StandardCharsets.UTF_8));
    }

    private static String createSignature(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update((data + SECRET_KEY).getBytes(StandardCharsets.UTF_8));
        byte[] hash = digest.digest();
        return Base64.getEncoder().encodeToString(hash);
    }

    public static boolean validateToken(String token) throws NoSuchAlgorithmException {
        // Decode the token
        String decodedToken = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
        String[] parts = decodedToken.split(":");

        if (parts.length != 3) {
            return false; // Invalid token structure
        }

        String username = parts[0];
        String timestamp = parts[1];
        String receivedSignature = parts[2];

        // Re-create the signature
        String data = username + ":" + timestamp;
        String expectedSignature = createSignature(data);

        // Check if the signatures match
        return receivedSignature.equals(expectedSignature);
    }
}
