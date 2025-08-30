

package app;
import org.bouncycastle.openpgp.PGPUtil;

public class KeyGen {
	/**
	 * Generates a PGP public/private keypair and returns them as armored strings.
	 * @param identity The identity (usually email or name) for the key.
	 * @param passphrase The passphrase to protect the private key.
	 * @return String array: [0] = armored public key, [1] = armored private key
	 */
	public static String[] generatePGPKeyPair(String identity, String passphrase) throws Exception {
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		java.security.KeyPairGenerator kpg = java.security.KeyPairGenerator.getInstance("RSA", "BC");
		kpg.initialize(2048);
		java.security.KeyPair kp = kpg.generateKeyPair();

		org.bouncycastle.openpgp.PGPKeyPair pgpKeyPair = new org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair(org.bouncycastle.openpgp.PGPPublicKey.RSA_GENERAL, kp, new java.util.Date());
		org.bouncycastle.openpgp.PGPKeyRingGenerator keyRingGen = new org.bouncycastle.openpgp.PGPKeyRingGenerator(
				org.bouncycastle.openpgp.PGPSignature.POSITIVE_CERTIFICATION,
				pgpKeyPair,
				identity,
				new org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder().build().get(PGPUtil.SHA1),
				null,
				null,
				new org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder(org.bouncycastle.openpgp.PGPPublicKey.RSA_GENERAL, PGPUtil.SHA1),
				new org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder(org.bouncycastle.openpgp.PGPEncryptedData.AES_256, new org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder().build().get(PGPUtil.SHA256))
						.setProvider("BC").build(passphrase.toCharArray())
		);

		// Export public key
		java.io.ByteArrayOutputStream pubOut = new java.io.ByteArrayOutputStream();
		try (org.bouncycastle.bcpg.ArmoredOutputStream pubArmored = new org.bouncycastle.bcpg.ArmoredOutputStream(pubOut)) {
			keyRingGen.generatePublicKeyRing().encode(pubArmored);
		}

		// Export private key
		java.io.ByteArrayOutputStream privOut = new java.io.ByteArrayOutputStream();
		try (org.bouncycastle.bcpg.ArmoredOutputStream privArmored = new org.bouncycastle.bcpg.ArmoredOutputStream(privOut)) {
			keyRingGen.generateSecretKeyRing().encode(privArmored);
		}

		// Store keys in a folder for later access
		String keysDirPath = "src/main/resources/keys";
		java.io.File keysDir = new java.io.File(keysDirPath);
		if (!keysDir.exists()) {
			keysDir.mkdirs();
		}

		// Sanitize identity for filename
		String safeIdentity = identity.replaceAll("[^a-zA-Z0-9._-]", "_");
		String pubKeyPath = keysDirPath + "/" + safeIdentity + "_public.asc";
		String privKeyPath = keysDirPath + "/" + safeIdentity + "_private.asc";

		try (java.io.FileWriter pubWriter = new java.io.FileWriter(pubKeyPath)) {
			pubWriter.write(pubOut.toString());
		}
		try (java.io.FileWriter privWriter = new java.io.FileWriter(privKeyPath)) {
			privWriter.write(privOut.toString());
		}

		return new String[] { pubOut.toString(), privOut.toString() };
	}
}
