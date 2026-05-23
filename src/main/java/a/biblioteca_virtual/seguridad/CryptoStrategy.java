package a.biblioteca_virtual.seguridad;

/**
 * Define el contrato para los algoritmos de cifrado dentro del sistema.
 *
 */
public interface CryptoStrategy {

    /**
     * Transforma una cadena de texto en una versión cifrada y protegida.
     *
     * @param datos El texto original que se desea ocultar.
     * @return Una cadena de texto cifrada.
     * @throws Exception Si ocurre un fallo en el proceso de cifrado o inicialización del algoritmo.
     */
    String encrypt(String datos) throws Exception;

    /**
     * Revierte el proceso de cifrado para recuperar la información original.
     *
     * @param datosCifrados La cadena protegida que se desea descifrar.
     * @return El texto original en formato legible.
     * @throws Exception Si la llave es incorrecta o los datos están corruptos.
     */
    String decrypt(String datosCifrados) throws Exception;
}
