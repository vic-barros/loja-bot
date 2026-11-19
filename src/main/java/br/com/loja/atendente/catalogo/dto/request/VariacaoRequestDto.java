package br.com.loja.atendente.catalogo.dto.request;

import br.com.loja.atendente.catalogo.model.Produto;
import br.com.loja.atendente.catalogo.model.Variacao;
import jakarta.validation.constraints.*;

public record VariacaoRequestDto(

        @NotNull(message = "O id do produto deve ser informado")
        Produto produtoId,

        @NotBlank(message = "A cor deve ser informada)")
        @Size(max = 40)
        String cor,

        @NotBlank(message = "O tamanho deve ser informado)")
        @Size(max = 10, message = "O tamanho deve ter no máximo 10 caracteres")
        String tamanho,

        @NotNull(message = "O estoque deve ser informado")
        @PositiveOrZero(message = "O estoque deve ser positivo ou zerado")
        Integer quantidadeEstoque
) {

    public Variacao paraModel(Produto produto) {
        Variacao variacao = new Variacao();
        variacao.setProduto(produto);
        variacao.setCor(cor);
        variacao.setTamanho(tamanho);
        if (quantidadeEstoque > 0) {
            variacao.reporEstoque(quantidadeEstoque);
        }
        return variacao;
    }
}
