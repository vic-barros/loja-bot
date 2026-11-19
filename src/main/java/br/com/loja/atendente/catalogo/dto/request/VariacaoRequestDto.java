package br.com.loja.atendente.catalogo.dto.request;

import br.com.loja.atendente.catalogo.model.Produto;
import br.com.loja.atendente.catalogo.model.Variacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record VariacaoRequestDto(

        @NotBlank(message = "A cor deve ser informada)")
        @Size(max = 40, message = "A cor deve ter no áximo 40 caracteres")
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
