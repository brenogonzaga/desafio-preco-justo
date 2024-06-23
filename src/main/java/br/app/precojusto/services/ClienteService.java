package br.app.precojusto.services;

import br.app.precojusto.exceptions.BadRequest;
import br.app.precojusto.exceptions.NotFound;
import br.app.precojusto.models.Cliente;
import br.app.precojusto.models.dto.request.ClienteRequestDTO;
import br.app.precojusto.models.dto.response.ClienteResponseDTO;
import br.app.precojusto.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteResponseDTO save(ClienteRequestDTO clienteDTO) throws BadRequest {
        Cliente clienteEmail = findByEmail(clienteDTO.getEmail());
        if (clienteEmail != null) {
            throw new BadRequest("Email já cadastrado");
        }
        // Cliente clienteCpf = clienteRepository.findByCpf(clienteDTO.getCpf());
        // if (clienteCpf != null) {
        //     throw new BadRequest("CPF já cadastrado");
        // }
        Cliente clienteFromDTO = clienteDTO.toCliente();
        return ClienteResponseDTO.fromCliente(clienteRepository.save(clienteFromDTO));
    }

    public Page<ClienteResponseDTO> findAll(int page, int size) {
        return ClienteResponseDTO.fromClientes(clienteRepository.findAll(PageRequest.of(page, size)));
    }

    public Cliente findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Cliente update(Long id, ClienteRequestDTO clienteDTO) throws NotFound, BadRequest {
        Cliente existingCliente = findById(id);
        Cliente cliente = clienteDTO.toCliente();
        cliente.setId(existingCliente.getId());
        if (cliente.getEmail() != null) {
            Cliente clienteEmail = findByEmail(cliente.getEmail());
            if (clienteEmail != null && !clienteEmail.getId().equals(id)) {
                throw new BadRequest("Email já cadastrado");
            }
        }
        // TODO: Antes de salvar, verificar se o email já existe
        // Verificar também CPF
        return clienteRepository.save(cliente);
    }

    public void delete(Long id) throws NotFound {
        Cliente cliente = findById(id);
        if (!cliente.isAtivo()) {
            throw new NotFound("Cliente não encontrado com ID: " + id);
        }
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }

    public Cliente findById(Long id) throws NotFound {
        return clienteRepository
            .findById(id)
            .orElseThrow(() -> new NotFound("Cliente não encontrada com ID: " + id));
    }
}
