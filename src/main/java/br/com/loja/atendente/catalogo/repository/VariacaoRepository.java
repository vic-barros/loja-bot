package br.com.loja.atendente.catalogo.repository;

import br.com.loja.atendente.catalogo.model.Variacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariacaoRepository extends JpaRepository<Variacao, Long> {

    @Query("""
            SELECT v FROM Variacao v
            JOIN FETCH v.produto p
            JOIN FETCH p.categoria c
            WHERE v.ativo = true
              AND p.ativo = true
              AND v.quantidadeEstoque > 0
              AND (:categoria IS NULL OR LOWER(c.nome) = :categoria)
              AND (:cor       IS NULL OR v.cor = :cor)
              AND (:tamanho   IS NULL OR v.tamanho = :tamanho)
              AND (:tecido    IS NULL OR LOWER(p.tecido) = :tecido)
            ORDER BY p.nome, v.cor, v.tamanho
            """)
    List<Variacao> buscar(@Param("categoria") String categoria,
                          @Param("cor") String cor,
                          @Param("tamanho") String tamanho,
                          @Param("tecido") String tecido);
}
