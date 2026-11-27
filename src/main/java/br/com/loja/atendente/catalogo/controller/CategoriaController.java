package br.com.loja.atendente.catalogo.controller;

import br.com.loja.atendente.catalogo.dto.request.CategoriaRequestDto;
import br.com.loja.atendente.catalogo.dto.response.CategoriaResponseDto;
import br.com.loja.atendente.catalogo.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // 201 Created + cabeçalho Location com o endereço da categoria criada
    @PostMapping
    public ResponseEntity<CategoriaResponseDto> cadastrar(@Valid @RequestBody CategoriaRequestDto request,
                                                          UriComponentsBuilder uriBuilder) {
        CategoriaResponseDto categoria = categoriaService.cadastrar(request);
        URI uri = uriBuilder.path("/categorias/{id}").buildAndExpand(categoria.categoriaId()).toUri();
        return ResponseEntity.created(uri).body(categoria);
    }

    // Lista em ordem alfabética
    @GetMapping
    public List<CategoriaResponseDto> listar() {
        return categoriaService.listar();
    }
}
