# 產生AES KEY

```java
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.tomcat.util.codec.binary.Base64;

//......

public static String genAesSecret() {
    try {
        KeyGenerator kg = KeyGenerator.getInstance("AES");

        //下面調用方法的参數決定了生成密鑰的長度，可以修改為128, 192或256
        kg.init(128);

        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        String secret = Base64.encodeBase64String(b);
        return secret;
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        throw new RuntimeException("没有此算法");
    }
}
```