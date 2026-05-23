package a.biblioteca_virtual.seguridad;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad de seguridad para la generación y verificación de textos cifraddos con SHA-256.
 *
 * @author Omar Alejandro Aguayo Sanchez
 */
public class HashUtil {

    // --- CONSTANTES ---
    private static final String ALGORITHM = "SHA-256";

    /**
     * Genera un hash SHA-256 a partir de una cadena de texto.
     *
     * @param base El texto original.
     * @return Una cadena hexadecimal de 64 caracteres, o null si ocurre un error.
     */
    public static String generarSHA256(String base) {
        if (base == null) return null;

        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = digest.digest(base.getBytes(StandardCharsets.UTF_8));

            // Convertimos los bytes a hexadecimal de forma limpia
            return bytesToHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error crítico: Algoritmo de hash no soportado. " + e.getMessage());
            return null;
        }
    }

    /**
     * Compara un texto en formato plano contra un hash previamente almacenado
     * para verificar si el contenido es idéntico (integridad).
     *
     * @param textoOriginal  El texto que se desea validar.
     * @param hashAlmacenado El hash guardado en la base de datos para comparar.
     * @return true si el hash del texto coincide con el almacenado, false en caso contrario.
     */
    public static boolean verificarIntegridad(String textoOriginal, String hashAlmacenado) {
        String hashCalculado = generarSHA256(textoOriginal);
        return hashCalculado != null && hashCalculado.equalsIgnoreCase(hashAlmacenado);
    }

    /**
     * Método auxiliar para convertir un arreglo de bytes a una cadena hexadecimal.
     * Utiliza un formato de flujo para evitar manipulaciones manuales de cadenas.
     *
     * @param bytes El arreglo de bytes generado por el MessageDigest.
     * @return Representación hexadecimal del hash.
     */
    private static String bytesToHex(byte[] bytes) {
        Object[] formattedBytes = new Object[bytes.length];
        String format = "%02x".repeat(bytes.length);

        for (int i = 0; i < bytes.length; i++) {
            formattedBytes[i] = bytes[i];
        }

        return String.format(format, formattedBytes);
    }
}