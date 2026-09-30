package com.mateus.ecommerce_api.controller;


import com.mateus.ecommerce_api.model.Usuario;
import com.mateus.ecommerce_api.repository.UsuarioRepository;
import com.mateus.ecommerce_api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/registrar")
    public String registrarUsuario(@RequestBody Usuario novoUsuario) {
        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);
        repository.save(novoUsuario);
        return "Usuário registrado com sucesso!";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario loginUsuario) {
        // 1. Procura o utilizador na base de dados através do email
        Optional<Usuario> usuarioEncontrado = repository.findByEmail(loginUsuario.getEmail());

        // 2. Verifica se o utilizador existe e se a password enviada coincide com a encriptada
        if (usuarioEncontrado.isPresent() && passwordEncoder.matches(loginUsuario.getSenha(), usuarioEncontrado.get().getSenha())) {

            // 3. Se estiver tudo certo, gera e devolve o Token JWT
            String token = tokenService.gerarToken(usuarioEncontrado.get());
            return ResponseEntity.ok(token);
        }

        // 4. Se a password ou email estiverem errados, barra a entrada (Erro 401 - Não Autorizado)
        return ResponseEntity.status(401).body("Email ou password incorretos!");
    }
}