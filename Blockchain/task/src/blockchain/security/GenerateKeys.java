package blockchain.security;

import blockchain.utils.Privacy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class GenerateKeys {

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keylength);
    }

    public void createKeys() {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.createNewFile();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public Map<Privacy, String> generate() throws IOException {
        String name = Thread.currentThread().getName();
        Map<Privacy, String> map = new HashMap<>();
        this.createKeys();
        String pathPublic = name + "Public";
        String pathPrivate = name + "Private";
        map.put(Privacy.PUBLIC, pathPublic);
        map.put(Privacy.PRIVATE, pathPrivate);
        this.writeToFile(pathPublic, this.getPublicKey().getEncoded());
        this.writeToFile(pathPrivate, this.getPrivateKey().getEncoded());
        return map;
    }
}