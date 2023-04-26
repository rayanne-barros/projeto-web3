package tech.ada.mercado.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Moeda {

    private String code;
    private String codein;
    private String name;

    private Double high;
}
