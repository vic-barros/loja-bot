package br.com.loja.atendente.catalogo.service;

import br.com.loja.atendente.catalogo.dto.request.FotoRequestDto;
import br.com.loja.atendente.catalogo.dto.request.ProdutoRequestDto;
import br.com.loja.atendente.catalogo.dto.request.VariacaoRequestDto;
import br.com.loja.atendente.catalogo.dto.response.ProdutoResponseDto;
import br.com.loja.atendente.catalogo.dto.response.VariacaoResponseDto;
import br.com.loja.atendente.catalogo.model.Categoria;
import br.com.loja.atendente.catalogo.model.FotoProduto;
import br.com.loja.atendente.catalogo.model.Produto;
import br.com.loja.atendente.catalogo.model.Variacao;
import br.com.loja.atendente.catalogo.repository.CategoriaRepository;
import br.com.loja.atendente.catalogo.repository.FotoProdutoRepository;
import br.com.loja.atendente.catalogo.repository.ProdutoRepository;
import br.com.loja.atendente.catalogo.repository.VariacaoRepository;
import br.com.loja.atendente.exception.RecursoNaoEncontradoException;
import br.com.loja.atendente.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private static final int TAMANHO_MAXIMO_SKU = 40;

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final VariacaoRepository variacaoRepository;
    private final FotoProdutoRepository fotoProdutoRepository;

    @Transactional
    public ProdutoResponseDto cadastrar(ProdutoRequestDto request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada"));

        validarVariacoesRepetidas(request.variacoes());

        Produto produto = produtoRepository.save(request.paraModel(categoria));

        List<Variacao> variacoes = new ArrayList<>();
        for (VariacaoRequestDto variacaoRequest : request.variacoes()) {
            Variacao variacao = variacaoRequest.paraModel(produto);
            variacao.setSku(gerarSku(produto, variacaoRequest.cor(), variacaoRequest.tamanho()));
            variacoes.add(variacao);
        }
        variacaoRepository.saveAll(variacoes);

        return ProdutoResponseDto.de(produto, variacoes, List.of());
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDto buscarPorId(Long produtoId) {
        Produto produto = buscarProduto(produtoId);
        List<Variacao> variacoes = variacaoRepository.findByProdutoProdutoId(produtoId);
        List<FotoProduto> fotos = fotoProdutoRepository.findByProdutoProdutoIdOrderByOrdemAsc(produtoId);
        return ProdutoResponseDto.de(produto, variacoes, fotos);
    }

    @Transactional(readOnly = true)
    public List<VariacaoResponseDto> buscar(String categoria, String cor, String tamanho, String tecido) {
        return variacaoRepository.buscar(
                        minusculo(categoria),
                        minusculo(cor),
                        maiusculo(tamanho),
                        minusculo(tecido))
                .stream()
                .map(VariacaoResponseDto::de)
                .toList();
    }

    @Transactional
    public ProdutoResponseDto adicionarFoto(Long produtoId, FotoRequestDto request) {
        Produto produto = buscarProduto(produtoId);

        if (fotoProdutoRepository.existsByProdutoProdutoIdAndOrdem(produtoId, request.ordem())) {
            throw new RegraDeNegocioException("Já existe uma foto na posição " + request.ordem() + " deste produto");
        }

        fotoProdutoRepository.save(request.paraModel(produto));
        return buscarPorId(produtoId);
    }

    // ---------- regras auxiliares ----------

    private Produto buscarProduto(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
    }

    private void validarVariacoesRepetidas(List<VariacaoRequestDto> variacoes) {
        Set<String> combinacoes = new HashSet<>();
        for (VariacaoRequestDto v : variacoes) {
            String chave = minusculo(v.cor()) + "|" + maiusculo(v.tamanho());
            if (!combinacoes.add(chave)) {
                throw new RegraDeNegocioException(
                        "Variação repetida: cor " + v.cor().trim() + ", tamanho " + v.tamanho().trim());
            }
        }
    }

    // Formato: LEG-12-PRETA-M (categoria, id do produto, cor, tamanho)
    private String gerarSku(Produto produto, String cor, String tamanho) {
        String prefixo = paraCodigo(produto.getCategoria().getNome()).replace("-", "");
        prefixo = prefixo.substring(0, Math.min(3, prefixo.length()));

        String sku = prefixo + "-" + produto.getProdutoId() + "-" + paraCodigo(cor) + "-" + paraCodigo(tamanho);
        return sku.length() > TAMANHO_MAXIMO_SKU ? sku.substring(0, TAMANHO_MAXIMO_SKU) : sku;
    }

    // "Verde Água" -> "VERDE-AGUA"
    private String paraCodigo(String texto) {
        String semAcento = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.toUpperCase()
                .replaceAll("[^A-Z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    private String minusculo(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim().toLowerCase();
    }

    private String maiusculo(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim().toUpperCase();
    }
}