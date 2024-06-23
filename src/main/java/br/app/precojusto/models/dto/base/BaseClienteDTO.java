package br.app.precojusto.models.dto.base;

import br.app.precojusto.utils.validation.CPF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseClienteDTO {

    @NotBlank
    private String nome;

    @NotBlank
    @CPF(message = "CPF inválido")
    private String cpf;

    @NotBlank
    @Email(message = "Email inválido")
    private String email;
}
