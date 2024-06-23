package br.app.precojusto.models.dto.response;

import br.app.precojusto.models.Pato;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class RelatorioResponseDTO {

    private Long id;
    private String nome;
    private String status;
    private String cliente;
    private String tipoCliente;
    private Double valor;
    private List<RelatorioResponseDTO> filhos;
    private int nivel;

    public static RelatorioResponseDTO toPatoDTO(Pato pato) {
        RelatorioResponseDTO dto = new RelatorioResponseDTO();
        dto.setId(pato.getId());
        dto.setNome(pato.getNome() != null && !pato.getNome().isEmpty() ? pato.getNome() : "Sem Nome");
        dto.setStatus(pato.isVendido() ? "Vendido" : "Disponível");
        dto.setCliente(
            pato.getVenda() != null && pato.getVenda().getCliente() != null
                ? pato.getVenda().getCliente().getNome()
                : ""
        );
        if (pato.getVenda() != null && pato.getVenda().getCliente() != null) {
            dto.setTipoCliente(pato.getVenda().getCliente().isElegivelDesconto() ? "Elegível" : "Normal");
            if (pato.isVendido() && pato.getVenda().getCliente().isElegivelDesconto()) {
                dto.setValor(pato.getPreco() * 0.8); // 20% de desconto
            } else {
                dto.setValor(pato.getPreco());
            }
        } else {
            dto.setValor(pato.getPreco());
        }
        dto.setFilhos(pato.getFilhos().stream().map(RelatorioResponseDTO::toPatoDTO).collect(Collectors.toList()));
        return dto;
    }

    public static PatoResponseDTO fromPato(Pato pato) {
        PatoResponseDTO dto = new PatoResponseDTO();
        dto.setId(pato.getId());
        dto.setNome(pato.getNome());
        dto.setPreco(pato.getPreco());
        if (pato.getMae() != null) {
            dto.setMae(pato.getMae().getNome());
        }
        return dto;
    }

    public static Page<PatoResponseDTO> fromPato(Page<Pato> patos) {
        return patos.map(PatoResponseDTO::fromPato);
    }
}
