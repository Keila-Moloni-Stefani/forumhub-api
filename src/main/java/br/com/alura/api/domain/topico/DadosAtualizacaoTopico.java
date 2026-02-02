package br.com.alura.api.domain.topico;

public record DadosAtualizacaoTopico(
        String titulo,
        String mensagem,
        StatusTopico status
) {}
