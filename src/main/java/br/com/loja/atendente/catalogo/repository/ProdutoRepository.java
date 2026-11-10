package br.com.loja.atendente.catalogo.repository;

import br.com.loja.atendente.catalogo.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
