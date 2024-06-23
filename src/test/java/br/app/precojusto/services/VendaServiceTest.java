package br.app.precojusto.services;

import br.app.precojusto.exceptions.NotFound;
import br.app.precojusto.models.Cliente;
import br.app.precojusto.models.Pato;
import br.app.precojusto.models.Venda;
import br.app.precojusto.repositories.VendaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private PatoService patoService;

    @InjectMocks
    private VendaService vendaService;

    @BeforeEach
    void setUp() {
        // Configuração inicial para os testes, se necessário
    }

    @Test
    void testCreateVendaComSucesso() throws NotFound {
        Cliente clienteMock = new Cliente(); 
        clienteMock.setId(1L);
        clienteMock.setElegivelDesconto(true);

        Pato patoMock = new Pato(); 
        patoMock.setId(1L);
        patoMock.setPreco(100.0);
        patoMock.setVendido(false);

        when(clienteService.findById(anyLong())).thenReturn(clienteMock);
        when(patoService.findById(anyLong())).thenReturn(patoMock);
        when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Venda venda = vendaService.create(1L, Arrays.asList(1L));

        assertNotNull(venda);
        assertTrue(venda.isDesconto());
        assertEquals(1, venda.getQuantidadePatos());
        assertEquals(80.0, venda.getValorTotal()); 
        assertTrue(venda.getPatos().get(0).isVendido());

        verify(clienteService).findById(1L);
        verify(patoService).findById(1L);
        verify(vendaRepository).save(any(Venda.class));
    }

}