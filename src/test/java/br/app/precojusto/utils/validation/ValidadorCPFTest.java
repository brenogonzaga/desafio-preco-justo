package br.app.precojusto.utils.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidadorCPFTest {

    private final ValidadorCPF validador = new ValidadorCPF();

    @Test
    void cpfNuloDeveSerInvalido() {
        assertFalse(validador.isValid(null, null));
    }

    @Test
    void cpfComTamanhoIncorretoDeveSerInvalido() {
        assertFalse(validador.isValid("1234567890", null)); // Menos de 11 caracteres
        assertFalse(validador.isValid("123456789012", null)); // Mais de 11 caracteres
    }

    @Test
    void cpfComSequenciaDeveSerInvalido() {
        assertFalse(validador.isValid("11111111111", null));
    }

    @Test
    void cpfValidoDeveSerValido() {
        assertTrue(validador.isValid("52998224725", null)); // Exemplo de CPF válido
    }

    @Test
    void cpfInvalidoDeveSerInvalido() {
        assertFalse(validador.isValid("52998224724", null)); // Dígito verificador inválido
    }

    @Test
    void cpfComCaracteresNaoNumericosDeveSerInvalido() {
        assertFalse(validador.isValid("5299822472a", null)); // Contém letra
    }
}