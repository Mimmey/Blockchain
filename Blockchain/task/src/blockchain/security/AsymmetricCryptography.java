//package blockchain.security;
//
//import javax.crypto.Cipher;
//import javax.crypto.NoSuchPaddingException;
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.security.*;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//
//public class AsymmetricCryptography {
//    private Cipher cipher;
//
//    public AsymmetricCryptography() throws NoSuchAlgorithmException, NoSuchPaddingException {
//        this.cipher = Cipher.getInstance("RSA");
//    }
//
//    public PrivateKey getPrivate(String filename) throws Exception {
//        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePrivate(spec);
//    }
//
//    public PublicKey getPublic(String filename) throws Exception {
//        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
//        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePublic(spec);
//    }
//
//    public String encrypt(String input, PrivateKey key)
//            throws IOException, GeneralSecurityException {
//        this.cipher.init(Cipher.ENCRYPT_MODE, key);
//        return new String(this.cipher.doFinal(input.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
//    }
//
//    public String decrypt(String input, PublicKey key)
//            throws IOException, GeneralSecurityException {
//        this.cipher.init(Cipher.DECRYPT_MODE, key);
//        return new String(this.cipher.doFinal(input.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
//    }
//
//    public String sign(String data, String keyFile) throws Exception {
//        Signature rsa = Signature.getInstance("SHA1withRSA");
//        rsa.initSign(this.getPrivate(keyFile));
//        rsa.update(data.getBytes());
//        return new String(rsa.sign(), StandardCharsets.UTF_8);
//    }
//
//    public void generate() {
//
//    }
//}