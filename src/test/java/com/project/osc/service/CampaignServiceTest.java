package com.project.osc.service;

import com.project.osc.mapper.CampaignMapper;
import com.project.osc.mapper.OSCMapper;
import com.project.osc.model.Campaign;
import com.project.osc.model.OSC;
import com.project.osc.service.http.CampaignResponse;
import com.project.osc.service.http.OSCResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Employee Campaign Service")
class CampaignServiceTest {

    private static final Integer idEmployee = 1;
    private static final Campaign VALID_CAMPAIGN_A_LIST_1= new Campaign(1,"RIO CUARTO",1,"AFULIC");
    private static final Campaign VALID_CAMPAIGN_A_LIST_2 = new Campaign(2,"VILLA ALLENDE",1,"SOLES");


    @Mock
    private CampaignMapper campaignMapper;

    @InjectMocks
    private OSCService sut;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    @DisplayName("Obtain campaign list from employee")
    class ObtainCampaign {

        @Nested
        @DisplayName("Should return 400 (Bad Request)")
        class ObtainCampaignBadRequestTest {

            @Test
            @DisplayName("When param idEmployee is null")
            public void obtainCampaignList_idEmployeeIsNull_ReturnsBadRequest(){
                ResponseEntity<CampaignResponse> responseEntity = sut.obtainCampaignList(null);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.BAD_REQUEST));
            }

            @Test
            @DisplayName("When param idEmployee is zero")
            public void obtainCampaignList_idEmployeeIsZero_ReturnsBadRequest(){
                ResponseEntity<CampaignResponse> responseEntity = sut.obtainCampaignList(0);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.BAD_REQUEST));
            }

            @Test
            @DisplayName("When param idEmployee is minor zero")
            public void obtainCampaignList_idEmployeeIsMinorZero_ReturnBadRequest(){
                ResponseEntity<CampaignResponse> responseEntity = sut.obtainCampaignList(-1);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.BAD_REQUEST));
            }
        }
        @Nested
        @DisplayName("Should return 500 (Internal Server Error)")
        class ObtainCampaignInternalServerError {

            @Test
            @DisplayName("When CampaignMapper throws Exception")
            public void obtainCampaignList_CampaignMapperThrowException_ReturnInternalServerError(){
                when(campaignMapper.obtainCampaignList(any())).thenThrow(new RuntimeException("something bad happen"));
                ResponseEntity<CampaignResponse> responseEntity = sut.obtainCampaignList(idEmployee);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.INTERNAL_SERVER_ERROR));
            }
        }

        @Nested
        @SuiteDisplayName("Should return 204 (No Content)")
        class ObtainCampaignNoContentTest {

            @Test
            @DisplayName("When CampaignList throws Exception")
            public void obtainCampaignList_CampaignListIsEmpty_ReturnNoContent(){
                when(campaignMapper.obtainCampaignList(any())).thenReturn(new ArrayList<Campaign>());
                ResponseEntity<CampaignResponse> responseEntity = sut.obtainCampaignList(idEmployee);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.NO_CONTENT));
            }
        }

        @Nested
        @SuiteDisplayName("Should return 200 (OK)")
        class ObtainCampaignStatusOKTest {

            @Test
            @DisplayName("When No Exception is Caught")
            public void obtainCampaignList_NoExceptionCaught_ReturnsOk(){
                when(campaignMapper.obtainCampaignList(any())).thenReturn(Arrays.asList(VALID_CAMPAIGN_A_LIST_1,VALID_CAMPAIGN_A_LIST_2));
                ResponseEntity<CampaignResponse> responseEntity = sut.obtainCampaignList(idEmployee);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.OK));
            }
        }

    }

}