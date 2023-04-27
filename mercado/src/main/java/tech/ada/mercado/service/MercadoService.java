package tech.ada.mercado.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.ada.mercado.model.Mercado;
import tech.ada.mercado.model.Moeda;
import tech.ada.mercado.repository.MercadoRepository;

import java.util.List;

@Service
public class MercadoService {
    @Autowired
    private MercadoRepository repository;

    public Mono<Mercado> salvar(Mercado mercado) {
        return repository.save(mercado);
    }

    public Flux<Mercado> listar(){
        return repository.findAll();
    }

    public Mono<Mercado> buscarPorId (String id) {
        return repository.findById(id);
    }

    public Mono<?> remover(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("user not found id " + id)))
                .flatMap(u -> repository.deleteById(id))
                .then();
    }

    public Mono<Mercado> atualizar(Mercado mercado, String id) {
        return repository.findById(id)
                .flatMap(atual -> repository.save(atual.update(mercado)));
    }

    public Flux<Moeda> cotacao(String moeda) {

        WebClient webClient = WebClient.create("https://economia.awesomeapi.com.br");
        Flux<Moeda> cotacao = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + moeda +"/")
                        .build())
                .retrieve().bodyToFlux(Moeda.class);
        return cotacao;
    }

}
