package br.com.loja.atendente.catalogo.service;

import br.com.loja.atendente.catalogo.dto.request.CategoriaRequestDto;
import br.com.loja.atendente.catalogo.dto.response.CategoriaResponseDto;
import br.com.loja.atendente.catalogo.model.Categoria;
import br.com.loja.atendente.catalogo.repository.CategoriaRepository;
import br.com.loja.atendente.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public CategoriaResponseDto cadastrar(CategoriaRequestDto request) {
        String nome = request.nome().trim();

        categoriaRepository.findByNomeIgnoreCase(nome).ifPresent(existente -> {
            throw new RegraDeNegocioException("Já existe uma categoria com o nome " + nome);
        });

        Categoria categoria = categoriaRepository.save(request.paraModel());
        return CategoriaResponseDto.de(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> listar() {
        return categoriaRepository.findAll(Sort.by("nome"))
                .stream()
                .map(CategoriaResponseDto::de)
                .toList();

    }
}
