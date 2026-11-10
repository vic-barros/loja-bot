package br.com.loja.atendente.catalogo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "variacao",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "produto_id", "cor", "tamanho"
        }))
@Getter
@NoArgsConstructor
public class Variacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variacao_id")
    private Long variacaoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    @Setter
    private Produto produto;

    @Setter
    @Column(name = "cor", nullable = false, length = 40)
    private String cor;

    @Setter
    @Column(name = "tamanho", nullable = false, length = 10)
    private String tamanho;

    @Setter
    @Column(name = "sku", nullable = false, length = 40, unique = true)
    private String sku;

    @Column(name = "quantidade_estoque", nullable = false)
    private int quantidadeEstoque = 0;

    @Setter
    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    // O JPA guarda um número de versão na linha. Ao salvar, ele confere se a versão ainda é a mesma que foi lida
    // Importante para quando houver a compra de duas clientes em produto com apenas 1 no estoque
    @Version
    @Column(name = "versao", nullable = false)
    private Long versao;

    @PrePersist
    @PreUpdate
    private void padronizar() {
        cor = cor.trim().toLowerCase();
        tamanho = tamanho.trim().toUpperCase();
    }

    public void baixarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        if (quantidade > this.quantidadeEstoque) {
            throw new IllegalStateException("Estoque insuficiente");
        }
        this.quantidadeEstoque -= quantidade;
    }

    public void reporEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        this.quantidadeEstoque += quantidade;
    }
}
