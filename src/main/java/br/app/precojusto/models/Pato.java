package br.app.precojusto.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Data
public class Pato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "mae_id", nullable = true)
    private Pato mae;

    private Double preco;

    @OneToMany(mappedBy = "mae")
    @JsonBackReference
    private List<Pato> filhos;

    private boolean vendido;

    @ManyToOne
    @JoinColumn(name = "venda_id")
    @JsonBackReference
    private Venda venda;

    // Método para adicionar um filho, garantindo que não ultrapasse dois filhos
    public void adicionarFilho(Pato filho) {
        if (this.filhos == null) {
            this.filhos = new ArrayList<>();
        }
        if (this.filhos.size() < 2) {
            this.filhos.add(filho);
            filho.setMae(this);
        } else {
            throw new IllegalStateException("Um pato não pode ter mais de dois filhos.");
        }
    }

    // Método para verificar a profundidade da linhagem
    public static int getProfundidadeLinhagem(Pato pato) {
        int profundidade = 0;
        while (pato.getMae() != null) {
            profundidade++;
            pato = pato.getMae();
            if (profundidade >= 2) break;
        }
        return profundidade;
    }

    // Método para definir a mãe, garantindo que a linhagem não ultrapasse dois níveis
    public void setMae(Pato mae) {
        if (getProfundidadeLinhagem(mae) < 2) {
            this.mae = mae;
        } else {
            throw new IllegalStateException("Não é permitido ter uma linhagem maior que 'filho do filho'.");
        }
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    // Método para atualizar o preço do pato com base na quantidade de filhos
    public void atualizarPreco() {
        if (filhos == null || filhos.isEmpty()) {
            this.preco = 70.00;
        } else if (filhos.size() == 1) {
            this.preco = 50.00;
        } else {
            this.preco = 25.00;
        }
    }
}
