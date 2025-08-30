package app;

import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PGPMessageEncoder {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String encodeMessage(String message, String publicKeyPath) throws Exception {
        PGPPublicKey encKey = readPublicKey(publicKeyPath);
        ByteArrayOutputStream encOut = new ByteArrayOutputStream();
        OutputStream armoredOut = new org.bouncycastle.bcpg.ArmoredOutputStream(encOut);
        OutputStream encryptedOut = null;
        try {
            PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5)
                    .setWithIntegrityPacket(true)
                    .setSecureRandom(new java.security.SecureRandom())
                    .setProvider("BC")
            );
            encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encKey).setProvider("BC"));
            encryptedOut = encGen.open(armoredOut, new byte[4096]);
            ByteArrayInputStream msgIn = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
            int ch;
            while ((ch = msgIn.read()) >= 0) {
                encryptedOut.write(ch);
            }
        } finally {
            if (encryptedOut != null) encryptedOut.close();
            armoredOut.close();
        }
        return encOut.toString();
    }

    private static PGPPublicKey readPublicKey(String filePath) throws Exception {
        InputStream keyIn = new BufferedInputStream(new FileInputStream(filePath));
        PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(
            PGPUtil.getDecoderStream(keyIn), new JcaKeyFingerprintCalculator());
        for (PGPPublicKeyRing keyRing : pgpPub) {
            for (PGPPublicKey key : keyRing) {
                if (key.isEncryptionKey()) {
                    return key;
                }
            }
        }
        throw new IllegalArgumentException("Can't find encryption key in key ring.");
    }
}
