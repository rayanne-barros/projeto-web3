package tech.ada.mercado.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Moeda {
    @JsonProperty("high")
    private Double high;

    public Double getHigh() {
        return high;
    }
}
