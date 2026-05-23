package a.biblioteca_virtual.seguridad;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Implementación de cifrado utilizando el algoritmo AES.
 * Esta clase se encarga de transformar texto en datos cifrados mediante hash y algoritmo AES,
 * utilizando una clave secreta predefinida y derivada mediante SHA-256.
 */
public class CryptoAES implements CryptoStrategy {

    // --- CONSTANTES DE CONFIGURACIÓN ---
    private static final String ALGORITHM = "AES";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String CLAVE_SECRETA = "DiccionarioS3gur0_2026!";
    private static final int KEY_SIZE_BYTES = 16;

    /**
     * Especificación de la llave secreta para el cifrado.
     */
    private SecretKeySpec secretKey;

    /**
     * Constructor que inicializa la configuración de seguridad.
     * Deriva una llave de 128 bits a partir de la constante.
     */
    public CryptoAES() {
        try {
            byte[] claveCodificada = CLAVE_SECRETA.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance(HASH_ALGORITHM);

            byte[] hashBytes = sha.digest(claveCodificada);

            byte[] aesKeyBytes = Arrays.copyOf(hashBytes, KEY_SIZE_BYTES);

            this.secretKey = new SecretKeySpec(aesKeyBytes, ALGORITHM);

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error crítico: El algoritmo de hash no está disponible. " + e.getMessage());
        }
    }

    /**
     * Cifra una cadena de texto utilizando el algoritmo AES.
     *
     * @param data El texto plano que se desea proteger.
     * @return Una cadena en formato Base64 que representa los datos cifrados.
     * @throws Exception Si ocurre un error durante el proceso de cifrado o inicialización del Cipher.
     */
    @Override
    public String encrypt(String data) throws Exception {
        if (data == null) return null;

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = cipher.doFinal(dataBytes);

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Descrifra una cadena previamente cifrada en formato Base64.
     *
     * @param encryptedData La cadena cifrada en formato Base64.
     * @return El texto original en formato plano.
     * @throws Exception Si los datos están corruptos, la llave es incorrecta o el padding falla.
     */
    @Override
    public String decrypt(String encryptedData) throws Exception {
        if (encryptedData == null) return null;

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}