package blockchain.entities;

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
public class Message {
    private String author;
    private String message;

    public Message(String data) throws Exception {
        this.author = Thread.currentThread().getName();
        this.message = data;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return author + ": " +  message;
    }
}
