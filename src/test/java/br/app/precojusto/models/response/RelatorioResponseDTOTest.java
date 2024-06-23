package br.app.precojusto.models.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.app.precojusto.models.Pato;
import br.app.precojusto.models.Venda;
import br.app.precojusto.models.dto.response.RelatorioResponseDTO;
import br.app.precojusto.models.Cliente;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class RelatorioResponseDTOTest {

    @Test
    public void testToPatoDTOWithNoNome() {
        Pato pato = new Pato();
        pato.setId(1L);
        pato.setNome("");
        pato.setVendido(false);
        pato.setPreco(100.0);
        pato.setFilhos(new ArrayList<>());

        RelatorioResponseDTO dto = RelatorioResponseDTO.toPatoDTO(pato);

        assertEquals("Sem Nome", dto.getNome());
        assertEquals("Disponível", dto.getStatus());
        assertEquals(100.0, dto.getValor());
        assertEquals(0, dto.getFilhos().size());
    }

    @Test
    public void testToPatoDTOWithVendidoAndElegivelDesconto() {
        Pato pato = new Pato();
        pato.setId(1L);
        pato.setNome("Pato Teste");
        pato.setVendido(true);
        pato.setPreco(100.0);

        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setElegivelDesconto(true);

        Venda venda = new Venda();
        venda.setCliente(cliente);
        pato.setVenda(venda);

        pato.setFilhos(new ArrayList<>());

        RelatorioResponseDTO dto = RelatorioResponseDTO.toPatoDTO(pato);

        assertEquals("Pato Teste", dto.getNome());
        assertEquals("Vendido", dto.getStatus());
        assertEquals("Cliente Teste", dto.getCliente());
        assertEquals("Elegível", dto.getTipoCliente());
        assertEquals(80.0, dto.getValor());
        assertEquals(0, dto.getFilhos().size());
    }


}