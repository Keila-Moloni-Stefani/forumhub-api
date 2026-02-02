package br.com.alura.api.controller;

import br.com.alura.api.domain.topico.*;
import br.com.alura.api.domain.usuario.Usuario;
import br.com.alura.api.infra.exception.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoTopico> cadastrar(
            @RequestBody @Valid DadosCadastroTopico dados,
            @AuthenticationPrincipal Usuario usuario,
            UriComponentsBuilder uriBuilder) {

        // Validação: não permitir tópicos duplicados
        if (topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            throw new ValidacaoException("Já existe um tópico com este título e mensagem");
        }

        var topico = new Topico(dados, usuario);
        topicoRepository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(
            @PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable paginacao) {
        var page = topicoRepository.findAll(paginacao).map(DadosListagemTopico::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTopico> detalhar(@PathVariable Long id) {
        var topico = topicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoTopico> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoTopico dados,
            @AuthenticationPrincipal Usuario usuario) {

        var topico = topicoRepository.getReferenceById(id);

        // Validação: apenas o autor pode atualizar
        if (!topico.getAutor().getId().equals(usuario.getId())) {
            throw new ValidacaoException("Você não tem permissão para atualizar este tópico");
        }

        topico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        var topico = topicoRepository.getReferenceById(id);

        // Validação: apenas o autor pode deletar
        if (!topico.getAutor().getId().equals(usuario.getId())) {
            throw new ValidacaoException("Você não tem permissão para excluir este tópico");
        }

        topicoRepository.delete(topico);

        return ResponseEntity.noContent().build();
    }
}