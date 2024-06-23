package br.app.precojusto.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidadorCPF implements ConstraintValidator<CPF, String> {

    @Override
    public void initialize(CPF constraintAnnotation) {}

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.length() != 11 || isSequencia(cpf)) {
            return false;
        }

        String cpfParcial = cpf.substring(0, 9);
        String digitoVerificador1 = gerarDigitoVerificador(cpfParcial);
        String digitoVerificador2 = gerarDigitoVerificador(cpfParcial + digitoVerificador1);

        return cpf.equals(cpfParcial + digitoVerificador1 + digitoVerificador2);
    }

    private boolean isSequencia(String cpf) {
        return cpf.chars().allMatch(c -> c == cpf.charAt(0));
    }

    private String gerarDigitoVerificador(String cpfParcial) {
        int soma = 0;
        int peso = cpfParcial.length() + 1;
        for (int i = 0; i < cpfParcial.length(); i++) {
            soma += Character.getNumericValue(cpfParcial.charAt(i)) * (peso - i);
        }
        int resto = soma % 11;
        return (resto < 2) ? "0" : String.valueOf(11 - resto);
    }
}
