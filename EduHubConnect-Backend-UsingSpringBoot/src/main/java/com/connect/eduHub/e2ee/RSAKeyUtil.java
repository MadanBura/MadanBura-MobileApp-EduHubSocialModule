package com.connect.eduHub.e2ee;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.springframework.stereotype.Component;

import com.connect.eduHub.model.User;

@Component
public class RSAKeyUtil {

    public KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    public void saveUserKeyPair(User user) throws Exception {
        KeyPair keyPair = generateKeyPair();
        user.setPublicKey(keyPair.getPublic().getEncoded());
        user.setPrivateKey(keyPair.getPrivate().getEncoded());
    }
}
