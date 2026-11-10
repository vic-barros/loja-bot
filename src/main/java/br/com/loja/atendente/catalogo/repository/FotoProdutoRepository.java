package br.com.loja.atendente.catalogo.repository;

import br.com.loja.atendente.catalogo.model.FotoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoProdutoRepository extends JpaRepository<FotoProduto, Long> {

    // Lista as fotos de um produto em ordem
    List<FotoProduto> findByProdutoProdutoIdOrderByOrdemAsc(Long produtoId);
}
