package com.project.osc.service;

import com.project.osc.mapper.CampaignMapper;
import com.project.osc.model.Campaign;
import com.project.osc.service.http.CampaignResponse;
import com.project.osc.utils.ListCampaignValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Employee Campaign Service")
class CampaignServiceTest {

    private static final Integer VALID_ID = 1;
    private static final Campaign VALID_CAMPAIGN_A_LIST_1 = new Campaign(1
                                                                        ,"RIO CUARTO"
                                                                        ,1
                                                                        ,"AFULIC");
    private static final Campaign VALID_CAMPAIGN_A_LIST_2 = new Campaign(2
                                                                        ,"VILLA ALLENDE"
                                                                        ,1
                                                                        ,"SOLES");

    @Mock
    private CampaignMapper campaignMapper;

    @Mock
    private ListCampaignValidator listCampaignValidator;

    @InjectMocks
    private OSCService sut;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @ArgumentsSource(RequestArgumentsSource.class)
    @DisplayName("When VALID_IDEMPLOYEE is null or less than or equal to zero. Should return 400 (Bad Request)")
    public void obtainCampaignList_idEmployeeIsNull_ReturnsBadRequest(Integer idEmployee){
        ResponseEntity<CampaignResponse> responseEntity = sut.obtainCampaignList(idEmployee);
        assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.BAD_REQUEST));
    }

    @Test
    @DisplayName("When CampaignMapper throws Exception. Should return 500 (Internal Server Error)")
    public void obtainCampaignList_CampaignMapperThrowException_ReturnsInternalServerError(){
        when(campaignMapper.obtainCampaignList(any())).thenThrow(new RuntimeException("something bad happen"));
        ResponseEntity<CampaignResponse> responseEntity = sut.obtainCampaignList(VALID_ID);
        assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    @DisplayName("When CampaignList throws Exception. Should return 204 (No Content)")
    public void obtainCampaignList_CampaignListIsEmpty_ReturnsNoContent(){
        ArrayList<Campaign> emptyList = new ArrayList<>();
        when(campaignMapper.obtainCampaignList(any())).thenReturn(emptyList);
        when(listCampaignValidator.apply(emptyList))
                .thenReturn(ResponseEntity.noContent().build());
        ResponseEntity<CampaignResponse> responseEntity = sut.obtainCampaignList(VALID_ID);
        assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.NO_CONTENT));
    }

    @Test
    @DisplayName("When No Exception is Caught. Should return 200 (OK)")
    public void obtainCampaignList_NoExceptionCaught_ReturnsOk(){
        List<Campaign> campaignList = Arrays.asList(VALID_CAMPAIGN_A_LIST_1, VALID_CAMPAIGN_A_LIST_2);
        when(campaignMapper.obtainCampaignList(any()))
                .thenReturn(campaignList);
        when(listCampaignValidator.apply(campaignList))
                .thenReturn(ResponseEntity.ok(new CampaignResponse(campaignList)));
        ResponseEntity<CampaignResponse> responseEntity = sut.obtainCampaignList(VALID_ID);
        assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCampaignList().get(0).toString(),
                is(campaignList.get(0).toString()));
    }

    static class RequestArgumentsSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments>
        provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(null, 0, -1)
                    .map(Arguments::of);
        }
    }
}