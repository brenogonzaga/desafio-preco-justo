package br.app.precojusto.models.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class VendaRequestDTO {

    private Long clienteId;
    private List<Long> patosId;
}
