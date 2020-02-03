package com.project.osc.service;

import com.project.osc.mapper.OSCMapper;
import com.project.osc.model.OSC;
import com.project.osc.service.http.OSCResponse;
import com.project.osc.utils.ListOSCValidator;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Employee OSC Service")
class OSCServiceTest {

    private static final Integer VALID_ID = 1;
    private static final OSC VALID_OSC_A_LIST_1 = new OSC(1,"AFULIC");
    private static final OSC VALID_OSC_A_LIST_2 = new OSC(1,"SOLES");

    @Mock
    private OSCMapper oscMapper;

    @Mock
    private ListOSCValidator listOSCValidator;

    @InjectMocks
    private OSCService sut;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @ArgumentsSource(RequestArgumentsSource.class)
    @DisplayName("When VALID_ID is null or less than or equal to zero. Should return 400 (Bad Request)")
    public void obtainOSCList_idEmployeeIsNullOrLessThanOrEqualToZero_ReturnsBadRequest(Integer idEmployee){
        ResponseEntity<OSCResponse> responseEntity = sut.obtainOSCList(idEmployee);
        assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.BAD_REQUEST));
    }

    @Test
    @DisplayName("When OSCMapper throws Exception. Should return 500 (Internal Server Error)")
    public void obtainOSCList_OSCMapperThrowException_ReturnsInternalServerError(){
        when(oscMapper.obtainOSCList(any())).thenThrow(new RuntimeException("something bad happen"));
        ResponseEntity<OSCResponse> responseEntity = sut.obtainOSCList(VALID_ID);
        assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    @DisplayName("When OSCList throws Exception. Should return 204 (No Content)")
    public void obtainOSCList_OSCListIsEmpty_ReturnsNoContent(){
        List<OSC> emptyList = Arrays.asList();
        when(oscMapper.obtainOSCList(any())).thenReturn(emptyList);
        when(listOSCValidator.apply(emptyList)).thenReturn(ResponseEntity.noContent().build());
        ResponseEntity<OSCResponse> responseEntity = sut.obtainOSCList(VALID_ID);
        assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.NO_CONTENT));
    }

    @Test
    @DisplayName("When No Exception is Caught. Should return 200 (OK)")
    public void obtainOSCList_NoExceptionCaught_ReturnsOk(){
        List<OSC> oscList = Arrays.asList(VALID_OSC_A_LIST_1, VALID_OSC_A_LIST_2);
        when(oscMapper.obtainOSCList(any())).thenReturn(oscList);
        when(listOSCValidator.apply(oscList))
                .thenReturn(ResponseEntity.ok(new OSCResponse(oscList)));
        ResponseEntity<OSCResponse> responseEntity = sut.obtainOSCList(VALID_ID);
        assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getOscList().get(0).toString(),
                is(oscList.get(0).toString()));
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