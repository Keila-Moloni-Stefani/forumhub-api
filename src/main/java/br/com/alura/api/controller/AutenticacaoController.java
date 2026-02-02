package br.com.alura.api.controller;

import br.com.alura.api.domain.usuario.DadosAutenticacao;
import br.com.alura.api.domain.usuario.Usuario;
import br.com.alura.api.infra.security.DadosTokenJWT;
import br.com.alura.api.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        System.out.println("=== TENTATIVA DE LOGIN ===");
        System.out.println("Email recebido: " + dados.email());
        System.out.println("Senha recebida: " + dados.senha());

        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
            System.out.println("Token de autenticação criado");

            var authentication = manager.authenticate(authenticationToken);
            System.out.println("Autenticação bem-sucedida!");

            var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
            System.out.println("Token JWT gerado: " + tokenJWT);

            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
        } catch (Exception e) {
            System.err.println("ERRO na autenticação: " + e.getClass().getName());
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}