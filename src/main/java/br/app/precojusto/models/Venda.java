package br.app.precojusto.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Entity
@Data
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private Cliente cliente;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVenda;

    @Column(nullable = false)
    private int quantidadePatos;

    private boolean desconto;

    @Column(nullable = false)
    private Double valorTotal;

    @OneToMany(mappedBy = "venda", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Pato> patos;

    @PrePersist
    protected void onCreate() {
        dataVenda = new Date();
    }
}
