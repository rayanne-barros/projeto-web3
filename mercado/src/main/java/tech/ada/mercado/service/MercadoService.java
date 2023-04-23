package tech.ada.mercado.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.ada.mercado.model.Mercado;
import tech.ada.mercado.repository.MercadoRepository;

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

}
