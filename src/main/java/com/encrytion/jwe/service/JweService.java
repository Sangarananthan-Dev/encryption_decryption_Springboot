package com.encrytion.jwe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JweService {

    private final RSAKey rsaJwk;
    private final ObjectMapper objectMapper;

    public <T> T decrypt(String compactJwe, Class<T> payloadType) {
        try {
            JWEObject jweObject = JWEObject.parse(compactJwe);
            jweObject.decrypt(new RSADecrypter(rsaJwk.toRSAPrivateKey()));
            return objectMapper.readValue(jweObject.getPayload().toString(), payloadType);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to decrypt JWE payload", exception);
        }
    }

    public String encrypt(Object payload) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            JWEObject jweObject = new JWEObject(buildHeader(), new Payload(jsonPayload));
            jweObject.encrypt(new RSAEncrypter(rsaJwk.toRSAPublicKey()));
            return jweObject.serialize();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to encrypt payload", exception);
        }
    }

    public String currentKeyId() {
        return rsaJwk.getKeyID();
    }

    private JWEHeader buildHeader() throws JOSEException {
        return new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                .keyID(rsaJwk.getKeyID())
                .contentType("application/json")
                .build();
    }
}
