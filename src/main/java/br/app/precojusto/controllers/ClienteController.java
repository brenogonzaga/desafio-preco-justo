package br.app.precojusto.controllers;

import br.app.precojusto.exceptions.BadRequest;
import br.app.precojusto.exceptions.NotFound;
import br.app.precojusto.models.Cliente;
import br.app.precojusto.models.dto.request.ClienteRequestDTO;
import br.app.precojusto.models.dto.response.ClienteResponseDTO;
import br.app.precojusto.services.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteRequestDTO clienteDTO)
        throws BadRequest, NotFound {
        ClienteResponseDTO responseClienteDTO = clienteService.save(clienteDTO);
        return ResponseEntity.created(null).body(responseClienteDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> getAllClientes(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<ClienteResponseDTO> responseClienteDTO = clienteService.findAll(page, size);
        return ResponseEntity.ok(responseClienteDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id) throws NotFound {
        Cliente cliente = clienteService.findById(id);
        ClienteResponseDTO responseClienteDTO = ClienteResponseDTO.fromCliente(cliente);
        return ResponseEntity.ok(responseClienteDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> updateCliente(
        @PathVariable Long id,
        @Valid @RequestBody ClienteRequestDTO clienteDTO
    ) throws NotFound, BadRequest {
        Cliente cliente = clienteService.update(id, clienteDTO);
        ClienteResponseDTO responseClienteDTO = ClienteResponseDTO.fromCliente(cliente);
        return ResponseEntity.ok(responseClienteDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) throws NotFound {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
