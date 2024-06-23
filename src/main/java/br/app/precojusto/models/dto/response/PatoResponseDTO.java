package br.app.precojusto.models.dto.response;

import br.app.precojusto.models.Pato;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PatoResponseDTO {

    private Long id;
    private String mae;
    private String nome;
    private Double preco;

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
