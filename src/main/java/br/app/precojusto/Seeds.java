package br.app.precojusto;

import br.app.precojusto.models.*;
import br.app.precojusto.models.Telefone.TipoTelefone;
import br.app.precojusto.repositories.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Seeds {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private PatoRepository patoRepository;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        seedClientes();
        seedPatos();
    }

    private void seedClientes() {
        for (int i = 0; i < 5; i++) {
            Cliente cliente = Cliente.builder().
                nome("Cliente " + i).
                cpf("99846249519").
                email("cliente" + i + "@example.com").
                hashedPassword("123").
                elegivelDesconto(i % 2 == 0). // Alternando entre elegível ou não para desconto
                ativo(true).
                build();
            clienteRepository.save(cliente);

            // Adicionando endereço para o cliente
            Endereco endereco = Endereco.builder().
                logradouro("Rua Exemplo " + i).
                numero("" + (100 + i)).
                complemento("Apto " + i).
                bairro("Bairro " + i).
                cidade("Cidade " + i).
                estado("Estado " + i).
                cep("12345-67" + i).
                pais("Brasil").
                cliente(cliente).
                build();


           
            enderecoRepository.save(endereco);

            // Adicionando telefone para o cliente
            Telefone telefone = Telefone.builder().
                tipo(TipoTelefone.CELULAR).
                ddd(11).
                numero("9876543" + i).
                cliente(cliente).
                build();
            telefoneRepository.save(telefone);
        }
    }

    private int getLineageDepth(Pato pato) {
        int depth = 0;
        Pato current = pato;
        while (current.getMae() != null) {
            depth++;
            current = current.getMae();
            if (depth >= 2) break;
        }
        return depth;
    }

    private void seedPatos() {
        Random random = new Random();
        List<Pato> possiveisMaes = patoRepository
            .findAll()
            .stream()
            .filter(pato -> getLineageDepth(pato) < 2)
            .collect(Collectors.toList());

        for (int i = 0; i < 10; i++) {
            Pato pato = new Pato();
            pato.setNome("Pato Extra " + i);
            if (!possiveisMaes.isEmpty()) {
                int indiceAleatorio = random.nextInt(possiveisMaes.size());
                Pato maeAleatoria = possiveisMaes.get(indiceAleatorio);
                pato.setMae(maeAleatoria);
            }

            patoRepository.save(pato);
            if (getLineageDepth(pato) < 2) {
                possiveisMaes.add(pato);
            }
        }
    }
}
