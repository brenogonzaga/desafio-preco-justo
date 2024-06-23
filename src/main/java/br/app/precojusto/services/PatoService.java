package br.app.precojusto.services;

import br.app.precojusto.exceptions.NotFound;
import br.app.precojusto.models.Pato;
import br.app.precojusto.models.dto.request.PatoRequestDTO;
import br.app.precojusto.models.dto.response.RelatorioResponseDTO;
import br.app.precojusto.repositories.PatoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PatoService {

    @Autowired
    private PatoRepository patoRepository;

    public Pato save(PatoRequestDTO createPatoDTO) throws NotFound {
        Pato pato = new Pato();
        pato.setNome(createPatoDTO.getNome());

        if (createPatoDTO.getMaeId() != null) {
            Pato mae = new Pato();
            mae.setId(createPatoDTO.getMaeId());
            if (patoRepository.findById(createPatoDTO.getMaeId()).isEmpty()) {
                throw new NotFound("Mãe não encontrada com ID: " + createPatoDTO.getMaeId());
            }
            pato.setMae(mae);
        }

        return patoRepository.save(pato);
    }

    public Page<Pato> findAll(int page, int size) {
        return patoRepository.findAll(PageRequest.of(page, size));
    }

    public Pato findById(Long id) throws NotFound {
        return patoRepository
            .findById(id)
            .orElseThrow(() -> new NotFound("Pato não encontrada com ID: " + id));
    }

    public Pato update(Long id, PatoRequestDTO patoRequestDTO) throws NotFound {
        Pato existingPato = findById(id);
        existingPato.setNome(patoRequestDTO.getNome());
        if (patoRequestDTO.getMaeId() != null) {
            Pato mae = new Pato();
            mae.setId(patoRequestDTO.getMaeId());
            existingPato.setMae(mae);
        }
        return patoRepository.save(existingPato);
    }

    public void delete(Long id) throws NotFound {
        // throw new NotFound("Pato não encontrado");
        findById(id);
        patoRepository.deleteById(id);
    }

    public List<RelatorioResponseDTO> obterPatosOrganizados() {
        List<Pato> patos = patoRepository.findAll();
        List<Pato> patosMaes = patos.stream().filter(p -> p.getMae() == null).collect(Collectors.toList());
        return patosMaes.stream().map(RelatorioResponseDTO::toPatoDTO).collect(Collectors.toList());
    }
}
