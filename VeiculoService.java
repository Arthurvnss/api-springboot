package com.frota.api.service;

import com.frota.api.model.Veiculo;
import com.frota.api.repository.VeiculoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class VeiculoService {

    private final VeiculoRepository repository;

    public VeiculoService(VeiculoRepository repository) {
        this.repository = repository;
    }

    public Veiculo criar(Veiculo veiculo) {
        veiculo.setId(null); // garante que sera um INSERT
        veiculo.setPlaca(normalizarPlaca(veiculo.getPlaca()));

        if (repository.existsByPlaca(veiculo.getPlaca())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ja existe um veiculo com a placa " + veiculo.getPlaca());
        }
        return repository.save(veiculo);
    }

    public List<Veiculo> listar() {
        return repository.findAll();
    }

    public Veiculo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Veiculo nao encontrado com id " + id));
    }

    public Veiculo atualizar(Long id, Veiculo dados) {
        Veiculo existente = buscarPorId(id);
        String novaPlaca = normalizarPlaca(dados.getPlaca());

        repository.findByPlaca(novaPlaca).ifPresent(outro -> {
            if (!outro.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Ja existe um veiculo com a placa " + novaPlaca);
            }
        });

        existente.setPlaca(novaPlaca);
        existente.setMarca(dados.getMarca());
        existente.setModelo(dados.getModelo());
        existente.setAno(dados.getAno());
        return repository.save(existente);
    }

    public void deletar(Long id) {
        Veiculo existente = buscarPorId(id);
        repository.delete(existente);
    }

    private String normalizarPlaca(String placa) {
        return placa.trim().replace("-", "").toUpperCase(Locale.ROOT);
    }
}
