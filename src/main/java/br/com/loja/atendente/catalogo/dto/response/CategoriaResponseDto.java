package br.com.loja.atendente.catalogo.dto.response;

import br.com.loja.atendente.catalogo.model.Categoria;

public record CategoriaResponseDto(
        Long categoriaId,
        String nome
) {

    public static CategoriaResponseDto de(Categoria categoria) {
        return new CategoriaResponseDto(
                categoria.getCategoriaId(),
                categoria.getNome());
    }
}