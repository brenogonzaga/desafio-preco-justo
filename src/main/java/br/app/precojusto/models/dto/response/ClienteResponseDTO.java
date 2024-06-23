package br.app.precojusto.models.dto.response;

import br.app.precojusto.models.Cliente;
import br.app.precojusto.models.dto.base.BaseClienteDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClienteResponseDTO extends BaseClienteDTO {

    private Long id;
    private boolean ativo;
    private boolean elegivelDesconto;

    public static ClienteResponseDTO fromCliente(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.id = cliente.getId();
        dto.setNome(cliente.getNome());
        dto.setCpf(cliente.getCpf());
        dto.setEmail(cliente.getEmail());
        dto.setElegivelDesconto(cliente.isElegivelDesconto());
        dto.setAtivo(cliente.isAtivo());
        return dto;
    }

    public static Page<ClienteResponseDTO> fromClientes(Page<Cliente> clientes) {
        List<ClienteResponseDTO> dtoList = clientes
            .stream()
            .map(ClienteResponseDTO::fromCliente)
            .collect(Collectors.toList());
        return new PageImpl<>(dtoList, clientes.getPageable(), clientes.getTotalElements());
    }
}
