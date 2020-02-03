package com.project.osc.utils;

import com.project.osc.model.Campaign;
import com.project.osc.service.http.CampaignResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class ListCampaignValidator implements Function<List<Campaign>, ResponseEntity<CampaignResponse>>{

    private static final Logger LOGGER = LoggerFactory.getLogger(ListCampaignValidator.class);

    @Override
    public ResponseEntity<CampaignResponse> apply(List<Campaign> listCampaigns) {
        if (!listCampaigns.isEmpty()) {
            LOGGER.info("the list for campaign for the employee was obtained");
            return ResponseEntity.ok().body(new CampaignResponse(listCampaigns));
        }
        LOGGER.info("the list of campaign for the employee was not obtained");
        return ResponseEntity.noContent().build();
    }
}
