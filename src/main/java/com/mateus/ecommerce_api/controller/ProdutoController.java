package com.mateus.ecommerce_api.controller;

import com.mateus.ecommerce_api.model.Produto;
import com.mateus.ecommerce_api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // Rota pública: Listar produtos com paginação e ordenação
    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<Produto>> listarProdutos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        // Configura a ordenação (ascendente ou descendente)
        org.springframework.data.domain.Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc") ? org.springframework.data.domain.Sort.Direction.DESC : org.springframework.data.domain.Sort.Direction.ASC;

        org.springframework.data.domain.Pageable pageable =
                org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortDirection, sortBy));

        org.springframework.data.domain.Page<Produto> produtos = produtoRepository.findAll(pageable);
        return ResponseEntity.ok(produtos);
    }

    // Rota protegida: Apenas administradores podem cadastrar novos produtos
    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoRepository.save(produto);
        return ResponseEntity.status(201).body(novoProduto);
    }

    // NOVO - Rota protegida: Apenas administradores podem atualizar um produto
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto dadosAtualizados) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(dadosAtualizados.getNome());
                    produto.setDescricao(dadosAtualizados.getDescricao());
                    produto.setPreco(dadosAtualizados.getPreco());
                    produto.setQuantidadeEstoque(dadosAtualizados.getQuantidadeEstoque());

                    Produto produtoAtualizado = produtoRepository.save(produto);
                    return ResponseEntity.ok(produtoAtualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // NOVO - Rota protegida: Apenas administradores podem apagar um produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}