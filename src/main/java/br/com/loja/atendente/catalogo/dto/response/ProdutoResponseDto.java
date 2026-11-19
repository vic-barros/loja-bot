package br.com.loja.atendente.catalogo.dto.response;

import br.com.loja.atendente.catalogo.model.FotoProduto;
import br.com.loja.atendente.catalogo.model.Produto;
import br.com.loja.atendente.catalogo.model.Variacao;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoResponseDto(
        Long produtoId,
        String nome,
        String categoria,
        String descricao,
        String tecido,
        BigDecimal preco,
        boolean ativo,
        List<VariacaoResumo> variacoes,
        List<String> fotos
) {

    public record VariacaoResumo(
            Long variacaoId,
            String cor,
            String tamanho,
            String sku,
            int quantidadeEstoque,
            boolean ativo
    ) {
        public static VariacaoResumo de(Variacao v) {
            return new VariacaoResumo(
                    v.getVariacaoId(),
                    v.getCor(),
                    v.getTamanho(),
                    v.getSku(),
                    v.getQuantidadeEstoque(),
                    v.isAtivo());
        }
    }

    public static ProdutoResponseDto de(Produto produto,
                                        List<Variacao> variacoes,
                                        List<FotoProduto> fotos) {
        return new ProdutoResponseDto(
                produto.getProdutoId(),
                produto.getNome(),
                produto.getCategoria().getNome(),
                produto.getDescricao(),
                produto.getTecido(),
                produto.getPreco(),
                produto.isAtivo(),
                variacoes.stream().map(VariacaoResumo::de).toList(),
                fotos.stream().map(FotoProduto::getUrl).toList());
    }
}