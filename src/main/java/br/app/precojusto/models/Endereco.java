package br.app.precojusto.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;

    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "O CEP deve seguir o padrão 12345-678 ou 12345678")
    private String cep;

    private String pais;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
