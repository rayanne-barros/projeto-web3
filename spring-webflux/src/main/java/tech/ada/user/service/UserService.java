package tech.ada.user.service;

import lombok.extern.slf4j.Slf4j;
import tech.ada.user.controller.Comprovante;
import tech.ada.user.model.User;
import tech.ada.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Mono<User> salvar(User user) {
        return repository.save(user);
    }

    public Flux<User> listar() {
        return repository.findAll();
    }

    public Mono<User> atualizar(User user, String id) {
        return repository.findById(id)
            .flatMap( atual -> repository.save(atual.update(user)));
    }

    public Mono<?> remover(String id) {
        return repository.findById(id)
            .switchIfEmpty(Mono.error(new RuntimeException("user not found id " + id)))
            .flatMap(u -> repository.deleteById(id))
            .then();
    }

    public Mono<User> buscarPorId(String id) {
        return repository.findById(id);
    }

    public Flux<User> buscarPorUsernames(String ... users) {
        return repository.findByUsernameIn(Arrays.asList(users));
    }

    public Mono<Comprovante> atualizar(Comprovante comp) {
        Flux<User> users = this.buscarPorUsernames(comp.getParamsUsers());
        return users.zipWith(users.skip(1))
            .map(tupla -> {
                User pagador = tupla.getT1();
                User recebedor = tupla.getT2();
                pagador.pay(recebedor, comp);
                return List.of(pagador, recebedor);
            })
                .flatMap(lista -> {
                    return repository.saveAll(lista);
                })
                .then(Mono.just(comp));
    }

}