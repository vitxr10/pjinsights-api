package br.com.santander.pjinsight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@SpringBootApplication
@EnableFeignClients
public class PjinsightApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(PjinsightApplication.class, args);

		String publicKeyPem ="-----BEGIN PUBLIC KEY-----\nMIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEA3ncrcmy+4riX0jVky/BA\nwkCOp3E/DHUQABZg1eKNBsjwdkjHAu+qDyqoSw2i2BSdS1tqWW+ynJ/2lFdfOsuC\nsz0wOxFxatFkJ6ti/21uC6v8jIZC67R77lGoJk8IMS2zrGtDxQ+3WSkcoQRE1PUs\nbLlHoxoPkVroDje/DmdkdBDpOT7I1Dd1XTuhhcEwYmT5rvdKAYB83U8Zoyf1GMcG\n7cGlQ6QRrBFH1ugv0wP91bVxWfg16+Ci2D1wmS2C6FG3ZlJSLbAj7Qf4Lf73eaPz\nCIgfVbiJ1jtPxGObd+TNyex/T2py+mp85olWH5Nz3ZjsHtivwwbSJan3KsPqp/N9\nTzgDOsrwSehWylNgHotjmwUPs420IzaE0T+Vl2xrcR1rpD4s8FTasxeaF17ldjAN\na7c8OsgboTsuZ1Pya+i2pnGWQ/Q6Vzyd+HOkg6me6a3x4fbrefEs/6kq/1cNymgA\ny/gZY2otg2CCzXS6tA3bwxTwogC9ZDQUwcZvaQ+Owl1jAgMBAAE=\n-----END PUBLIC KEY-----\n";

		// limpar PEM corretamente
		String clean = publicKeyPem
				.replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "")
				.replaceAll("\\s", ""); // precisa escapar

		byte[] decoded = Base64.getDecoder().decode(clean);
		var keySpec = new X509EncodedKeySpec(decoded);
		var publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);

		// JSON que você quer encriptar
		String json = "{\"login\":\"carlosponchis@santander.com.br\",\"password\":\"WXxrTthuZOg5JKYE\"}";

		// Encriptar com RSA-OAEP-SHA256
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] encryptedBytes = cipher.doFinal(json.getBytes());
		String encryptedB64 = Base64.getEncoder().encodeToString(encryptedBytes);

		// JSON pronto pra mandar no Swagger
		System.out.println("{\"encrypted\": \"" + encryptedB64 + "\"}");
	}
}