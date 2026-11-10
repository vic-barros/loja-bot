package br.com.loja.atendente.catalogo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "foto_produto",
        uniqueConstraints = @UniqueConstraint(columnNames = {"produto_id", "ordem"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FotoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fotoProdutoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "cor", nullable = true, length = 40)
    private String cor;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "ordem", nullable = false)
    private int ordem = 0;

    @PrePersist
    @PreUpdate
    private void padronizar() {
        if (cor != null) {
            cor = cor.trim().toLowerCase();
        }
    }
}
