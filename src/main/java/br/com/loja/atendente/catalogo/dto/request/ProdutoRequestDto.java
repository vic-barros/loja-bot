package br.com.loja.atendente.catalogo.dto.request;

import br.com.loja.atendente.catalogo.model.Categoria;
import br.com.loja.atendente.catalogo.model.Produto;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoRequestDto(

        @NotNull(message = "O Id da Categoria deve ser informado")
        Long categoriaId,

        @NotBlank(message = "O nome do produto deve ser informado")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres")
        String nome,

        String descricao,

        @Size(max = 50, message = "O tecido deve ter no máximo 50 caracteres")
        String tecido,

        @NotNull(message = "O preço do produto deve ser informado")
        @Positive
        @Digits(integer = 8, fraction = 2)
        BigDecimal preco,

        List<VariacaoRequestDto> variacoes
) {

    public Produto paraModel(Categoria categoria) {
        Produto produto = new Produto();
        produto.setCategoria(categoria);
        produto.setNome(nome.trim());
        produto.setDescricao(descricao);
        produto.setTecido(tecido == null ? null : tecido.trim().toLowerCase());
        produto.setPreco(preco);
        return produto;
    }
}
