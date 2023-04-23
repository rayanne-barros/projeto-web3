package tech.ada.mercado.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.ada.mercado.model.Mercado;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MercadoRequest {
    private String nome;
    private String moeda;
    private BigDecimal saldo;

    public Mercado update(Mercado mercado) {
        mercado.setNome(this.nome);
        mercado.setMoeda(this.moeda);
        mercado.setSaldo(this.saldo);
        return mercado;
    }

    public Mercado create() {
        Mercado mercado = new Mercado();
        mercado.setNome(this.nome);
        mercado.setMoeda(this.moeda);
        mercado.setSaldo(this.saldo);
        return mercado;
    }
}
