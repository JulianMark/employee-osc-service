package com.project.osc.service.http;
import com.project.osc.model.Campaign;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CampaignResponse {

    @ApiModelProperty(notes="Lista de Campañas donde trabajo el empleado")
    private List<Campaign> campaignList;
    @ApiModelProperty(notes="Mensaje de error, en caso de que falle el WS")
    private String errorMessage;

    public CampaignResponse(List<Campaign> campaignList) {
        this.campaignList = campaignList;
    }

    public CampaignResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
