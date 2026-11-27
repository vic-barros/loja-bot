package br.com.loja.atendente.catalogo.controller;

import br.com.loja.atendente.catalogo.dto.request.FotoRequestDto;
import br.com.loja.atendente.catalogo.dto.request.ProdutoRequestDto;
import br.com.loja.atendente.catalogo.dto.response.ProdutoResponseDto;
import br.com.loja.atendente.catalogo.dto.response.VariacaoResponseDto;
import br.com.loja.atendente.catalogo.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    // Cadastra o produto com as variações numa transação só
    @PostMapping
    public ResponseEntity<ProdutoResponseDto> cadastrar(@Valid @RequestBody ProdutoRequestDto request,
                                                        UriComponentsBuilder uriBuilder) {
        ProdutoResponseDto produto = produtoService.cadastrar(request);
        URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produto.produtoId()).toUri();
        return ResponseEntity.created(uri).body(produto);
    }

    // Visão completa da lojista: inclui variações inativas e sem estoque
    @GetMapping("/{produtoId}")
    public ProdutoResponseDto buscarPorId(@PathVariable Long produtoId) {
        return produtoService.buscarPorId(produtoId);
    }

    // Busca usada pela IA: só peças ativas e com estoque. Todos os filtros são opcionais.
    // O caminho fixo "/busca" tem prioridade sobre "/{produtoId}", então não há conflito.
    @GetMapping("/busca")
    public List<VariacaoResponseDto> buscar(@RequestParam(required = false) String categoria,
                                            @RequestParam(required = false) String cor,
                                            @RequestParam(required = false) String tamanho,
                                            @RequestParam(required = false) String tecido) {
        return produtoService.buscar(categoria, cor, tamanho, tecido);
    }

    @PostMapping("/{produtoId}/fotos")
    public ResponseEntity<ProdutoResponseDto> adicionarFoto(@PathVariable Long produtoId,
                                                            @Valid @RequestBody FotoRequestDto request,
                                                            UriComponentsBuilder uriBuilder) {
        ProdutoResponseDto produto = produtoService.adicionarFoto(produtoId, request);
        URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produtoId).toUri();
        return ResponseEntity.created(uri).body(produto);
    }
}
