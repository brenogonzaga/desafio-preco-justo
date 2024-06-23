package br.app.precojusto.services;

import br.app.precojusto.exceptions.NotFound;
import br.app.precojusto.models.Cliente;
import br.app.precojusto.models.Pato;
import br.app.precojusto.models.Venda;
import br.app.precojusto.repositories.VendaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PatoService patoService;

    public Venda create(Long clienteId, List<Long> patosId) throws NotFound {
        Cliente cliente = clienteService.findById(clienteId);
        if (cliente == null) {
            throw new EntityNotFoundException("Cliente não encontrado com ID: " + clienteId);
        }

        List<Pato> patos = getPatos(patosId);

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setPatos(patos);
        venda.setQuantidadePatos(patos.size());
        venda.setDesconto(cliente.isElegivelDesconto());
        double valorTotal = patos.stream().mapToDouble(Pato::getPreco).sum();
        if (cliente.isElegivelDesconto()) {
            valorTotal *= 0.8; // Aplica 20% de desconto
        }
        venda.setValorTotal(valorTotal);
        patos.forEach(pato -> {
            pato.setVenda(venda);
            pato.setVendido(true);
        });

        return vendaRepository.save(venda);
    }

    public Page<Venda> findAll(int page, int size) {
        return vendaRepository.findAll(PageRequest.of(page, size));
    }

    public Venda findById(Long id) throws NotFound {
        return vendaRepository
            .findById(id)
            .orElseThrow(() -> new NotFound("Venda não encontrada com ID: " + id));
    }

    private List<Pato> getPatos(List<Long> patosId) {
        return patosId
            .stream()
            .map(patoId -> {
                Pato pato = null;
                try {
                    pato = patoService.findById(patoId);
                } catch (NotFound e) {
                    e.printStackTrace();
                    throw new EntityNotFoundException("Pato não encontrado com ID: " + patoId);
                }
                if (pato == null) {
                    throw new EntityNotFoundException("Pato não encontrado com ID: " + patoId);
                }
                if (pato.isVendido()) {
                    throw new EntityNotFoundException("Pato já vendido com ID: " + patoId);
                }
                return pato;
            })
            .collect(Collectors.toList());
    }
}
