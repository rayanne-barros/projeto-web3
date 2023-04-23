package tech.ada.mercado.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.ada.mercado.model.Mercado;
import tech.ada.mercado.service.MercadoService;

@RestController
@RequestMapping("/mercados")
@Slf4j
public class MercadoController {
    @Autowired
    private MercadoService service;

    @PostMapping
    public Mono<ResponseEntity<Mercado>> salvar(@RequestBody Mercado mercado) {
        return service.salvar(mercado)
                .map(atual -> ResponseEntity.ok().body(atual));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Mercado>>> listar() {
        return service.listar()
                .collectList()
                .map(mercados -> ResponseEntity.ok().body(Flux.fromIterable(mercados)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> remover(@PathVariable String id) {
        return service.remover(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Mercado>> atualizar(@RequestBody MercadoRequest mercado, @PathVariable String id) {
        return service.atualizar(mercado.create(), id)
                .map(atual -> ResponseEntity.ok().body(atual))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

}
