package com.project.osc.utils;

import com.project.osc.model.Campaign;
import com.project.osc.model.OSC;
import com.project.osc.service.http.CampaignResponse;
import com.project.osc.service.http.OSCResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("Validator List of Campaign")
class ListCampaignValidatorTest {

    @Mock
    private Campaign campaign = new Campaign(1, "Rio Cuarto", 1, "Afulic");

    @InjectMocks
    private ListCampaignValidator sut;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("When list is empty should returns 204")
    void obtainEmptyList_ListIsEmpty_ReturnsNonContent () {
        List<Campaign> emptyList = Arrays.asList();
        ResponseEntity<CampaignResponse> responseEntity = sut.obtainList(emptyList);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NO_CONTENT));
    }

    @Test
    @DisplayName("When list is not empty should returns 200")
    void obtainList_ListIsNotEmpty_ReturnsOk () {
        List<Campaign> campaignList = Arrays.asList(campaign);
        ResponseEntity<CampaignResponse> responseEntity = sut.obtainList(campaignList);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCampaignList().get(0).toString(),
                is(campaignList.get(0).toString()));
    }
}