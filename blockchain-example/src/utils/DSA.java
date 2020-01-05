package utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class DSA {
	private static byte[] signMessage(String message, PrivateKey privkey) throws Exception {
		Signature sign = Signature.getInstance("SHA256withDSA");
		sign.initSign(privkey);
		sign.update(message.getBytes());
		return sign.sign();
	}

	private static boolean verifySignature(String message, PublicKey pubKey, byte[] signature) throws Exception {
		Signature sign = Signature.getInstance("SHA256withDSA");
		sign.initVerify(pubKey);
		sign.update(message.getBytes());
		return sign.verify(signature);
	}

	public static void main(String[] args) throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
		KeyPair pair = keyPairGen.generateKeyPair();
		PrivateKey privKey = pair.getPrivate();
		PublicKey pubKey = pair.getPublic();

		String message = "Top Secret";
		byte[] signature = DSA.signMessage(message, privKey);
		System.out.println(message + " ==> " + new String(signature));
		boolean verification = DSA.verifySignature(message, pubKey, signature);
		System.out.println("Verification: " + verification);

		// Tampering message
		String wrongMessage = "Not a secret";
		boolean falseVerification = DSA.verifySignature(wrongMessage, pubKey, signature);
		System.out.println("Malicious Verification #1: " + falseVerification);

		// Tampering message + signing it
		pair = keyPairGen.generateKeyPair();
		PrivateKey attackerPrivKey = pair.getPrivate();
		byte[] attackerSignature = DSA.signMessage(wrongMessage, attackerPrivKey);
		boolean falseVerification2 = DSA.verifySignature(wrongMessage, pubKey, attackerSignature);
		System.out.println("Malicious Verification #2: " + falseVerification2);	
	}
}
