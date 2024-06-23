package br.app.precojusto.controllers;

import br.app.precojusto.exceptions.NotFound;
import br.app.precojusto.models.Pato;
import br.app.precojusto.models.dto.request.PatoRequestDTO;
import br.app.precojusto.models.dto.response.PatoResponseDTO;
import br.app.precojusto.services.PatoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patos")
@Tag(name = "Patos")
public class PatoController {

    @Autowired
    private PatoService patoService;

    @PostMapping
    public ResponseEntity<PatoResponseDTO> createPato(@Valid @RequestBody PatoRequestDTO pato) throws NotFound {
        Pato savedPato = patoService.save(pato);
        PatoResponseDTO responseDTO = PatoResponseDTO.fromPato(savedPato);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<PatoResponseDTO>> getAllPatos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<Pato> patos = patoService.findAll(page, size);
        Page<PatoResponseDTO> responseDTO = PatoResponseDTO.fromPato(patos);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatoResponseDTO> getPatoById(@PathVariable Long id) throws NotFound {
        Pato pato = patoService.findById(id);
        PatoResponseDTO responseDTO = PatoResponseDTO.fromPato(pato);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatoResponseDTO> updatePato(@PathVariable Long id, @Valid @RequestBody PatoRequestDTO pato)
        throws NotFound {
        PatoResponseDTO responseDTO = PatoResponseDTO.fromPato(patoService.update(id, pato));
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePato(@PathVariable Long id) throws NotFound {
        patoService.delete(id);
    }
}
