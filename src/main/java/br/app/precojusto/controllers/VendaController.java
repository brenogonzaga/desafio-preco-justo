package br.app.precojusto.controllers;

import br.app.precojusto.exceptions.NotFound;
import br.app.precojusto.models.Venda;
import br.app.precojusto.models.dto.request.VendaRequestDTO;
import br.app.precojusto.models.dto.response.VendaResponseDTO;
import br.app.precojusto.services.VendaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendas")
@Tag(name = "Vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping
    public ResponseEntity<VendaResponseDTO> createVenda(@Valid @RequestBody VendaRequestDTO createVendaDTO)
        throws NotFound {
        Venda vendaRealizada = vendaService.create(
            createVendaDTO.getClienteId(),
            createVendaDTO.getPatosId()
        );
        return ResponseEntity.ok(VendaResponseDTO.fromVendaPato(vendaRealizada));
    }

    @GetMapping
    public ResponseEntity<Page<VendaResponseDTO>> getAllVendas(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<Venda> venda = vendaService.findAll(page, size);
        return ResponseEntity.ok(VendaResponseDTO.fromVendaPato(venda));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> getVendaById(@PathVariable Long id) throws NotFound {
        Venda venda = vendaService.findById(id);
        return ResponseEntity.ok(VendaResponseDTO.fromVendaPato(venda));
    }
}
