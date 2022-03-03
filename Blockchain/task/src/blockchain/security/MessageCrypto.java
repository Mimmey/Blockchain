package blockchain.security;

import blockchain.utils.Privacy;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class MessageCrypto {
    private static final Map<String, Map<Privacy, String>> paths = new HashMap<>();
    private String author;
    private String message;
    private Cipher cipher;
    private static final int KEY_LENGTH = 2048;

    public MessageCrypto(String data) throws Exception {
        this.cipher = Cipher.getInstance("RSA");
        String privateKeyFileName = paths.get(Thread.currentThread().getName()).get(Privacy.PRIVATE);
        this.author = Thread.currentThread().getName();
        this.message = encrypt(data, getPrivate(privateKeyFileName));
    }

    public static void addPath(String author, Map<Privacy, String> path) {
        paths.put(author, path);
    }

    public String encrypt(String input, PrivateKey key)
            throws IOException, GeneralSecurityException {
        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        return new String(this.cipher.doFinal(input.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public String decrypt(String input, PublicKey key)
            throws IOException, GeneralSecurityException {
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(this.cipher.doFinal(input.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public PrivateKey getPrivate(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public PublicKey getPublic(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
    public static int getKeyLength() {
        return KEY_LENGTH;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return author + ": " +  decrypt(message, getPublic(paths.get(author).get(Privacy.PUBLIC)));
    }
}
