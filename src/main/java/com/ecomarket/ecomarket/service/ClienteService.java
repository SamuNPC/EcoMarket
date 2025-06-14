package com.ecomarket.ecomarket.service;

import com.ecomarket.ecomarket.model.Cliente;
import com.ecomarket.ecomarket.repository.ClienteRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public class ValidadorRut {
    /**
     * @param numeroRut Número del RUT, puede venir con puntos.
     * @param dv Dígito verificador (puede ser mayúscula o minúscula).
     * @return true si el RUT es válido, false si no.
     */
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

    /**
     * @param rut Número del RUT (sin puntos ni dígito verificador).
     * @return Dígito verificador como carácter ('0'–'9' o 'K').
     */
    private static char calcularDv(int rut) {
        int m = 0, suma = 1;
        while (rut != 0) {
            suma = (suma + rut % 10 * (9 - m++ % 6)) % 11;
            rut /= 10;
        }
        return (suma == 0) ? 'K' : (char) (suma + 47);  // 1 → '1', 10 → '0'
    }
}

}
