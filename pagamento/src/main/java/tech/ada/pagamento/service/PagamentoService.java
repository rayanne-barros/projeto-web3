package tech.ada.pagamento.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.ada.pagamento.model.*;
import tech.ada.pagamento.repository.TransacaoRepository;

@Service
@Slf4j
public class PagamentoService {

    private TransacaoRepository transacaoRepository;

    public PagamentoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    public Mono<Comprovante> pagar(Pagamento pagamento) {

        WebClient webClient = WebClient.create("http://localhost:8080");
        Flux<Usuario> usuarios = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/usernames") // http://..users/usernames?users=bob,alice
                        .queryParam("users", pagamento.getParamUsuarios())
                        .build())
                .retrieve().bodyToFlux(Usuario.class);

        usuarios.flatMap(u -> {
            log.error(u.getUsername() + " : " + u.getBalance());
            return null;
        }).subscribe(u -> {
            log.error(u.getUsername() + " : " + u.getBalance());
            return null;
        });

        Mono<Comprovante> comprovanteMono = Flux.zip(usuarios, usuarios.skip(1))
                .map(tupla -> new Transacao(
                        tupla.getT1().getUsername(),
                        tupla.getT2().getUsername(),
                        pagamento.getValor()))
                .last()
                .flatMap(tx -> transacaoRepository.save(tx))
                .map(tx -> tx.getComprovate())
                .flatMap(cmp -> {
                    return salvar(cmp);
                });

        return comprovanteMono;
    }

    private Mono<Comprovante> salvar(Comprovante cmp) {
        WebClient webClient = WebClient.create("http://localhost:8080");
        Mono<Comprovante> monoComprovante = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/pagamentos")
                        .build())
                .bodyValue(cmp)
                .retrieve().bodyToMono( Comprovante.class);

        return monoComprovante;
    }

    public static void main(String[] args) {
        int a = 1_000_000_000;
        int b = 2_000_000_000;
        int c = (a + b) / 2;
        System.out.println( c );
        System.out.println((a+b)>>>1);
    }

}