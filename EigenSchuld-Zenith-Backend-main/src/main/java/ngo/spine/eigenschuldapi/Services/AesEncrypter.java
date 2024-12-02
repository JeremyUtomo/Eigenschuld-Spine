package ngo.spine.eigenschuldapi.Services;

import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

public class AesEncrypter implements AttributeConverter<Object, String> {

    @Value("${aes.encryption.key}")
    private String encryptionKey;
    private final String encryptionCipher = "AES";

    private Key key;
    private Cipher cipher;

    private Key getKey() {
        if (this.key == null){
            this.key = new SecretKeySpec(this.encryptionKey.getBytes(), this.encryptionCipher);
        }
        return this.key;
    }

    private Cipher getCipher() throws GeneralSecurityException {
        if (this.cipher == null){
            this.cipher = Cipher.getInstance(this.encryptionCipher);
        }
        return this.cipher;
    }

    private void initCipher(int encryptionMode) throws GeneralSecurityException {
        getCipher().init(encryptionMode, getKey());
    }

    @Override
    public String convertToDatabaseColumn(Object object) {
        if (object == null) {
            return null;
        }
        try {
            initCipher(Cipher.ENCRYPT_MODE);
            byte[] bytes = SerializationUtils.serialize(object);
            return Base64.getEncoder().encodeToString(getCipher().doFinal(bytes));

        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object convertToEntityAttribute(String string) {
        if (string == null) {
            return null;
        }
        try {
            initCipher(Cipher.DECRYPT_MODE);
            byte[] bytes = getCipher().doFinal(Base64.getDecoder().decode(string));
            return SerializationUtils.deserialize(bytes);

        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
    
}
