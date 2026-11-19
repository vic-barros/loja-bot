package br.com.loja.atendente.catalogo.dto.response;

import br.com.loja.atendente.catalogo.model.Produto;
import br.com.loja.atendente.catalogo.model.Variacao;

import java.math.BigDecimal;

public record VariacaoResponseDto(
        Long variacaoId,
        Long produtoId,
        String produto,
        String categoria,
        String tecido,
        BigDecimal preco,
        String cor,
        String tamanho,
        String sku,
        int quantidadeEstoque
) {

    public static VariacaoResponseDto de(Variacao v) {
        Produto p = v.getProduto();
        return new VariacaoResponseDto(
                v.getVariacaoId(),
                p.getProdutoId(),
                p.getNome(),
                p.getCategoria().getNome(),
                p.getTecido(),
                p.getPreco(),
                v.getCor(),
                v.getTamanho(),
                v.getSku(),
                v.getQuantidadeEstoque());
    }
}