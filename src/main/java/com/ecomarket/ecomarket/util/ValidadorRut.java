package com.ecomarket.ecomarket.util;

    public class ValidadorRut {

    public static boolean esRutValido(String numeroRut, String dv) {
        if (numeroRut == null || dv == null) {
            return false;
        }
        // Eliminar puntos y espacios
        numeroRut = numeroRut.replace(".", "").trim();
        dv = dv.trim().toUpperCase();

        if (numeroRut.isEmpty() || dv.isEmpty()) {
            return false;
        }

        try {
            int rutNumerico = Integer.parseInt(numeroRut);
            char dvEsperado = calcularDv(rutNumerico);
            return dv.equals(String.valueOf(dvEsperado));
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static char calcularDv(int rut) {
        int m = 0, suma = 1;
        while (rut != 0) {
            suma = (suma + rut % 10 * (9 - m++ % 6)) % 11;
            rut /= 10;
        }
        return (suma == 0) ? 'K' : (char) (suma + 47);  // 1 → '1', 10 → '0'
    }
}
