package br.app.precojusto.services;

import br.app.precojusto.exceptions.NotFound;
import br.app.precojusto.models.Pato;
import br.app.precojusto.models.dto.request.PatoRequestDTO;
import br.app.precojusto.repositories.PatoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatoServiceTest {

    @Mock
    private PatoRepository patoRepository;

    @InjectMocks
    private PatoService patoService;

    private Pato pato;
    private PatoRequestDTO patoRequestDTO;

    @BeforeEach
    void setUp() {
        pato = new Pato();
        pato.setId(1L);
        pato.setNome("Pato Teste");

        patoRequestDTO = new PatoRequestDTO();
        patoRequestDTO.setNome("Pato Teste");
        patoRequestDTO.setMaeId(null);
    }


    @Test
    void saveThrowsNotFound() {
        patoRequestDTO.setMaeId(2L);
        when(patoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFound.class, () -> patoService.save(patoRequestDTO));
    }

    @Test
    void findAllSuccess() {
        Page<Pato> patoPage = new PageImpl<>(Arrays.asList(pato));
        when(patoRepository.findAll(any(PageRequest.class))).thenReturn(patoPage);

        Page<Pato> result = patoService.findAll(0, 1);

        assertFalse(result.isEmpty());
        assertEquals(pato.getNome(), result.getContent().get(0).getNome());
    }

    @Test
    void findByIdSuccess() throws NotFound {
        when(patoRepository.findById(anyLong())).thenReturn(Optional.of(pato));

        Pato foundPato = patoService.findById(1L);

        assertNotNull(foundPato);
        assertEquals(pato.getNome(), foundPato.getNome());
    }

    @Test
    void findByIdThrowsNotFound() {
        when(patoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFound.class, () -> patoService.findById(1L));
    }

 

    @Test
    void deleteSuccess() throws NotFound {
        when(patoRepository.findById(anyLong())).thenReturn(Optional.of(pato));
        doNothing().when(patoRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> patoService.delete(1L));
    }

    @Test
    void deleteThrowsNotFound() {
        when(patoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFound.class, () -> patoService.delete(1L));
    }

   
}