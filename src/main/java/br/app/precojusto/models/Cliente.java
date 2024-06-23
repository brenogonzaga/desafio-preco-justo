package br.app.precojusto.models;

import br.app.precojusto.utils.validation.CPF;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false)
    private String nome;

    @CPF(message = "CPF inválido")
    @Column(unique = false)
    private String cpf; // TODO: Mudar CPF para ser único

    @Email(message = "E-mail inválido")
    @Column(unique = true)
    private String email;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private boolean elegivelDesconto;

    @Builder.Default
    @Column(nullable = false)
    private boolean ativo = true;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", orphanRemoval = true)
    private Set<Endereco> enderecos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", orphanRemoval = true)
    private Set<Telefone> telefones;

    @OneToMany(mappedBy = "cliente")
    private List<Venda> compras;
}
