-- Dados de exemplo para desenvolvimento local.
--
-- Este arquivo NÃO roda em produção: ele fica em db/dev, uma pasta que o Flyway
-- só lê quando a variável FLYWAY_LOCATIONS inclui classpath:db/dev (veja o .env.example).
--
-- É uma migration "repetível" (prefixo R__): roda depois das versionadas e roda de novo
-- sempre que este arquivo mudar. Por isso todo INSERT aqui é idempotente: rodar duas
-- vezes não duplica nada.
--
-- Os valores já vêm padronizados como o sistema grava: cor e tecido em minúsculas,
-- tamanho em maiúsculas, e SKU no mesmo formato do ProdutoService (LEG-12-PRETA-M).

-- 1. Categorias -------------------------------------------------------------------

INSERT INTO categoria (nome)
VALUES ('Leggings'), ('Tops'), ('Shorts'), ('Biquínis'), ('Maiôs'), ('Saídas de praia')
ON CONFLICT (nome) DO NOTHING;

-- 2. Produtos (só cria se ainda não existir um produto com o mesmo nome) -----------

INSERT INTO produto (categoria_id, nome, descricao, tecido, preco)
SELECT c.categoria_id, p.nome, p.descricao, p.tecido, p.preco
FROM (VALUES
    ('Leggings',        'Legging Básica Cós Alto',         'Cós alto que não enrola, compressão média',       'suplex',    79.90),
    ('Leggings',        'Legging Estampada Fitness',       'Estampa exclusiva, ideal para treino funcional',  'poliamida', 99.90),
    ('Tops',            'Top Nadador Alta Sustentação',    'Alças nadador e bojo removível',                  'poliamida', 69.90),
    ('Tops',            'Top Faixa Básico',                'Top faixa sem bojo, para treinos leves',          'suplex',    49.90),
    ('Shorts',          'Short Saia Fitness',              'Short com saia sobreposta e bolso lateral',       'suplex',    59.90),
    ('Biquínis',        'Biquíni Top Cortininha',          'Parte de cima, vendida separadamente',            'lycra',     69.90),
    ('Biquínis',        'Biquíni Calcinha Asa Delta',      'Parte de baixo, vendida separadamente',           'lycra',     59.90),
    ('Maiôs',           'Maiô Decote Profundo',            'Costas abertas e forro duplo',                    'lycra',    149.90),
    ('Saídas de praia', 'Saída de Praia Kimono',           'Kimono leve, tamanho único',                      'viscose',  119.90),
    ('Saídas de praia', 'Saída de Praia Vestido Curto',    'Vestido soltinho com amarração na cintura',       'viscose',   99.90)
) AS p (categoria, nome, descricao, tecido, preco)
JOIN categoria c ON c.nome = p.categoria
WHERE NOT EXISTS (SELECT 1 FROM produto existente WHERE existente.nome = p.nome);

-- 3. Variações ----------------------------------------------------------------------
-- Algumas têm estoque 0 de propósito: servem para conferir que a busca não as mostra.

INSERT INTO variacao (produto_id, cor, tamanho, sku, quantidade_estoque)
SELECT pr.produto_id,
       v.cor,
       v.tamanho,
       pre.prefixo || '-' || pr.produto_id || '-'
           || trim(both '-' from regexp_replace(
                  upper(translate(v.cor,
                        'áàâãäéèêëíìîïóòôõöúùûüç',
                        'aaaaaeeeeiiiiooooouuuuc')),
                  '[^A-Z0-9]+', '-', 'g'))
           || '-' || v.tamanho AS sku,
       v.quantidade
FROM (VALUES
    ('Legging Básica Cós Alto',      'preta',          'P', 2),
    ('Legging Básica Cós Alto',      'preta',          'M', 4),
    ('Legging Básica Cós Alto',      'preta',          'G', 3),
    ('Legging Básica Cós Alto',      'azul-marinho',   'P', 1),
    ('Legging Básica Cós Alto',      'azul-marinho',   'M', 2),
    ('Legging Básica Cós Alto',      'azul-marinho',   'G', 0),
    ('Legging Estampada Fitness',    'estampa folhas', 'P', 2),
    ('Legging Estampada Fitness',    'estampa folhas', 'M', 2),
    ('Legging Estampada Fitness',    'estampa folhas', 'G', 1),
    ('Top Nadador Alta Sustentação', 'preto',          'P', 3),
    ('Top Nadador Alta Sustentação', 'preto',          'M', 3),
    ('Top Nadador Alta Sustentação', 'preto',          'G', 2),
    ('Top Nadador Alta Sustentação', 'rosa',           'P', 0),
    ('Top Nadador Alta Sustentação', 'rosa',           'M', 2),
    ('Top Nadador Alta Sustentação', 'rosa',           'G', 1),
    ('Top Faixa Básico',             'branco',         'P', 2),
    ('Top Faixa Básico',             'branco',         'M', 2),
    ('Top Faixa Básico',             'branco',         'G', 2),
    ('Short Saia Fitness',           'preto',          'P', 2),
    ('Short Saia Fitness',           'preto',          'M', 3),
    ('Short Saia Fitness',           'preto',          'G', 1),
    ('Short Saia Fitness',           'verde água',     'P', 1),
    ('Short Saia Fitness',           'verde água',     'M', 1),
    ('Short Saia Fitness',           'verde água',     'G', 0),
    ('Biquíni Top Cortininha',       'estampa folhas', 'P', 2),
    ('Biquíni Top Cortininha',       'estampa folhas', 'M', 3),
    ('Biquíni Top Cortininha',       'estampa folhas', 'G', 1),
    ('Biquíni Top Cortininha',       'preto',          'P', 1),
    ('Biquíni Top Cortininha',       'preto',          'M', 2),
    ('Biquíni Top Cortininha',       'preto',          'G', 2),
    ('Biquíni Calcinha Asa Delta',   'estampa folhas', 'P', 2),
    ('Biquíni Calcinha Asa Delta',   'estampa folhas', 'M', 2),
    ('Biquíni Calcinha Asa Delta',   'estampa folhas', 'G', 1),
    ('Biquíni Calcinha Asa Delta',   'preto',          'P', 2),
    ('Biquíni Calcinha Asa Delta',   'preto',          'M', 1),
    ('Biquíni Calcinha Asa Delta',   'preto',          'G', 1),
    ('Maiô Decote Profundo',         'vermelho',       'P', 1),
    ('Maiô Decote Profundo',         'vermelho',       'M', 2),
    ('Maiô Decote Profundo',         'vermelho',       'G', 1),
    ('Maiô Decote Profundo',         'preto',          'P', 1),
    ('Maiô Decote Profundo',         'preto',          'M', 1),
    ('Maiô Decote Profundo',         'preto',          'G', 0),
    ('Saída de Praia Kimono',        'off white',      'U', 3),
    ('Saída de Praia Kimono',        'estampa folhas', 'U', 2),
    ('Saída de Praia Vestido Curto', 'branca',         'U', 2)
) AS v (produto, cor, tamanho, quantidade)
JOIN produto pr ON pr.nome = v.produto
JOIN categoria c ON c.categoria_id = pr.categoria_id
JOIN (VALUES
    ('Leggings', 'LEG'), ('Tops', 'TOP'), ('Shorts', 'SHO'),
    ('Biquínis', 'BIQ'), ('Maiôs', 'MAI'), ('Saídas de praia', 'SAI')
) AS pre (categoria, prefixo) ON pre.categoria = c.nome
ON CONFLICT DO NOTHING;
