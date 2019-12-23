package com.project.osc.service.http;

import io.swagger.annotations.ApiModelProperty;
import com.project.osc.model.OSC;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OSCResponse {

    @ApiModelProperty(notes="Lista de OSCs donde trabajo el empleado")
    private List<OSC> oscList;
    @ApiModelProperty(notes="Mensaje de error, en caso de que falle el WS")
    private String errorMessage;

    public OSCResponse(List<OSC> oscList) {
        this.oscList = oscList;
    }

    public OSCResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
