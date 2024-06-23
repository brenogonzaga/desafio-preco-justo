package br.app.precojusto.services;

import br.app.precojusto.exceptions.BadRequest;
import br.app.precojusto.exceptions.NotFound;
import br.app.precojusto.models.Cliente;
import br.app.precojusto.models.dto.request.ClienteRequestDTO;
import br.app.precojusto.models.dto.response.ClienteResponseDTO;
import br.app.precojusto.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteRequestDTO clienteRequestDTO;
    private ClienteResponseDTO clienteResponseDTO;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        clienteRequestDTO = new ClienteRequestDTO();
        clienteRequestDTO.setEmail("test@example.com");
        clienteRequestDTO.setNome("Test Name");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setEmail("test@example.com");
        cliente.setNome("Test Name");

        clienteResponseDTO = ClienteResponseDTO.fromCliente(cliente);
    }

  

    @Test
    void saveThrowsBadRequest() {
        when(clienteRepository.findByEmail(anyString())).thenReturn(cliente);

        assertThrows(BadRequest.class, () -> clienteService.save(clienteRequestDTO));
    }

    @Test
    void findAllSuccess() {
        Page<Cliente> clientePage = new PageImpl<>(Collections.singletonList(cliente));
        when(clienteRepository.findAll(any(PageRequest.class))).thenReturn(clientePage);

        Page<ClienteResponseDTO> result = clienteService.findAll(0, 1);

        assertFalse(result.isEmpty());
        assertEquals(cliente.getEmail(), result.getContent().get(0).getEmail());
    }

    @Test
    void findByEmailSuccess() {
        when(clienteRepository.findByEmail(anyString())).thenReturn(cliente);

        Cliente result = clienteService.findByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }





    @Test
    void deleteThrowsNotFound() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFound.class, () -> clienteService.delete(1L));
    }

    @Test
    void findByIdSuccess() throws NotFound {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));

        Cliente foundCliente = clienteService.findById(1L);

        assertNotNull(foundCliente);
        assertEquals(1L, foundCliente.getId());
    }

    @Test
    void findByIdThrowsNotFound() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFound.class, () -> clienteService.findById(1L));
    }
}