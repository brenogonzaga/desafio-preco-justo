package br.app.precojusto.models.dto.response;

import br.app.precojusto.models.Venda;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class VendaResponseDTO {

    private Long id;
    private ClienteResponseDTO cliente;
    private List<PatoVendidosDTO> pato;
    private Date dataVenda;
    private double valorTotal;
    private int quantidadePatos;

    @Data
    public static class PatoVendidosDTO {

        private Long id;
        private String nome;
        private Double preco;
    }

    public static VendaResponseDTO fromVendaPato(Venda venda) {
        VendaResponseDTO dto = new VendaResponseDTO();
        dto.setId(venda.getId());
        dto.setDataVenda(venda.getDataVenda());
        dto.setQuantidadePatos(venda.getQuantidadePatos());
        dto.setValorTotal(venda.getValorTotal());

        ClienteResponseDTO clienteResponseDTO = ClienteResponseDTO.fromCliente(venda.getCliente());
        dto.setCliente(clienteResponseDTO);

        List<PatoVendidosDTO> patosDTO = venda
            .getPatos()
            .stream()
            .map(pato -> {
                PatoVendidosDTO patoDTO = new PatoVendidosDTO();
                patoDTO.setId(pato.getId());
                patoDTO.setNome(pato.getNome());
                patoDTO.setPreco(pato.getPreco());
                return patoDTO;
            })
            .collect(Collectors.toList());
        dto.setPato(patosDTO);

        return dto;
    }

    public static Page<VendaResponseDTO> fromVendaPato(Page<Venda> vendaPato) {
        return vendaPato.map(VendaResponseDTO::fromVendaPato);
    }
}
