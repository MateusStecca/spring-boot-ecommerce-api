package com.mateus.ecommerce_api.controller;

import com.mateus.ecommerce_api.model.*;
import com.mateus.ecommerce_api.repository.PedidoRepository;
import com.mateus.ecommerce_api.repository.ProdutoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private com.mateus.ecommerce_api.service.TokenService tokenService;

    @Autowired
    private com.mateus.ecommerce_api.repository.UsuarioRepository usuarioRepository;

    // Rota protegida: Qualquer cliente autenticado pode fechar um pedido
    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody DadosPedido dados, HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return ResponseEntity.status(401).body("Token não fornecido");
        var token = authHeader.replace("Bearer ", "");
        var email = tokenService.validarToken(token);

        if (email.isEmpty()) return ResponseEntity.status(401).body("Token inválido");

        double valorTotal = 0.0;
        List<ItemPedido> itensPedido = new ArrayList<>();

        for (var itemDto : dados.getItens()) {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId()).orElse(null);

            if (produto == null) {
                return ResponseEntity.badRequest().body("Produto com ID " + itemDto.getProdutoId() + " não encontrado.");
            }

            if (produto.getQuantidadeEstoque() < itemDto.getQuantidade()) {
                return ResponseEntity.badRequest().body("Stock insuficiente para o produto: " + produto.getNome());
            }

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemDto.getQuantidade());
            produtoRepository.save(produto);

            ItemPedido itemPedido = new ItemPedido(produto, itemDto.getQuantidade(), produto.getPreco());
            itensPedido.add(itemPedido);

            valorTotal += produto.getPreco() * itemDto.getQuantidade();
        }

        Pedido pedido = new Pedido(email, valorTotal, itensPedido);
        pedidoRepository.save(pedido);

        return ResponseEntity.status(201).body(pedido);
    }

    // Rota protegida: O cliente autenticado vê apenas o seu próprio histórico de pedidos
    @GetMapping("/meus")
    public ResponseEntity<List<Pedido>> listarMeusPedidos(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return ResponseEntity.status(401).build();
        var token = authHeader.replace("Bearer ", "");
        var email = tokenService.validarToken(token);

        if (email.isEmpty()) return ResponseEntity.status(401).build();

        List<Pedido> meusPedidos = pedidoRepository.findByEmailUsuario(email);
        return ResponseEntity.ok(meusPedidos);
    }

    // Rota para listar todos os pedidos (para administradores / testes)
    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoRepository.findAll());
    }
}