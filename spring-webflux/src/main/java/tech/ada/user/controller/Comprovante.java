package tech.ada.user.controller;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Comprovante {

    public String id;
    private String pagador;
    private String recebedor;
    private Double valor;
    private LocalDateTime data;
    private Boolean ack_usuario;

    public String[] getParamsUsers() {
        return new String[] { this.pagador, this.recebedor};
    }

}
