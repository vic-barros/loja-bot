package br.com.loja.atendente.catalogo.dto.request;

import br.com.loja.atendente.catalogo.model.FotoProduto;
import br.com.loja.atendente.catalogo.model.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record FotoRequestDto(

        @NotBlank(message = "A URL da foto deve ser informada")
        @Size(max = 500, message = "A URL deve ter no máximo 500 caracteres")
        @Pattern(regexp = "^https://.*", message = "A URL deve começar com https://")
        String url,

        @Size(max = 40, message = "A cor deve ter no máximo 40 caracteres")
        String cor,

        @NotNull(message = "A ordem deve ser informada")
        @PositiveOrZero(message = "A ordem deve ser zero ou positiva")
        Integer ordem
) {

    public FotoProduto paraModel(Produto produto) {
        FotoProduto foto = new FotoProduto();
        foto.setProduto(produto);
        foto.setUrl(url.trim());
        foto.setCor(cor);
        foto.setOrdem(ordem);
        return foto;
    }
}