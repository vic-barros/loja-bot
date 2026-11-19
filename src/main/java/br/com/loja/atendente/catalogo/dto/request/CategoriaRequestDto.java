package br.com.loja.atendente.catalogo.dto.request;

import br.com.loja.atendente.catalogo.model.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDto(

        @NotBlank(message = "O nome da categoria deve ser informado")
        @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres")
        String nome
) {

    public Categoria paraModel() {
        Categoria categoria = new Categoria();
        categoria.setNome(nome.trim());
        return categoria;
    }
}