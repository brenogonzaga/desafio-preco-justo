package br.app.precojusto.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TipoTelefone tipo;

    @Min(value = 10, message = "O DDD deve ter exatamente 2 dígitos.")
    @Max(value = 99, message = "O DDD deve ter exatamente 2 dígitos.")
    private Integer ddd;

    @Pattern(regexp = "\\d{8,9}", message = "O número de telefone deve ter entre 8 e 9 dígitos.")
    private String numero;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public enum TipoTelefone {
        RESIDENCIAL,
        COMERCIAL,
        CELULAR,
    }
}
