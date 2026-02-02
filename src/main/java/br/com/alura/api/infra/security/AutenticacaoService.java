package br.com.alura.api.infra.security;

import br.com.alura.api.domain.usuario.Usuario;
import br.com.alura.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Buscando usuário no banco: " + username);

        var usuario = repository.findByEmail(username);

        if (usuario == null) {
            System.err.println("ERRO: Usuário não encontrado no banco!");
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        Usuario user = (Usuario) usuario;
        System.out.println("Usuário encontrado: " + user.getNome());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Senha (primeiros 20 chars): " + user.getPassword().substring(0, 20) + "...");

        return usuario;
    }
}