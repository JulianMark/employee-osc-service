package com.project.osc.service;

import com.project.osc.mapper.OSCMapper;
import com.project.osc.model.OSC;
import com.project.osc.service.http.OSCResponse;
import org.h2.command.dml.MergeUsing;
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
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Employee OSC Service")
class OSCServiceTest {

    private static final Integer idEmployee = 1;
    private static final OSC VALID_OSC_A_LIST_1= new OSC(1,"AFULIC");
    private static final OSC VALID_OSC_A_LIST_2 = new OSC(1,"SOLES");


    @Mock
    private OSCMapper oscMapper;

    @InjectMocks
    private OSCService sut;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    @DisplayName("Obtain OSC list from employee")
    class ObtainOSC {

        @Nested
        @DisplayName("Should return 400 (Bad Request)")
        class ObtainOSCBadRequestTest {

            @Test
            @DisplayName("When param idEmployee is null")
            public void obtainOSCList_idEmployeeIsNull_ReturnsBadRequest(){
                ResponseEntity<OSCResponse> responseEntity = sut.obtainOSCList(null);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.BAD_REQUEST));
            }

            @Test
            @DisplayName("When param idEmployee is zero")
            public void obtainOSCList_idEmployeeIsZero_ReturnsBadRequest(){
                ResponseEntity<OSCResponse> responseEntity = sut.obtainOSCList(0);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.BAD_REQUEST));
            }

            @Test
            @DisplayName("When param idEmployee is minor zero")
            public void obtainOSCList_idEmployeeIsMinorZero_ReturnBadRequest(){
                ResponseEntity<OSCResponse> responseEntity = sut.obtainOSCList(-1);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.BAD_REQUEST));
            }
        }
        @Nested
        @DisplayName("Should return 500 (Internal Server Error)")
        class ObtainOSCInternalServerError {

            @Test
            @DisplayName("When OSCMapper throws Exception")
            public void obtainOSCList_OSCMapperThrowException_ReturnInternalServerError(){
                when(oscMapper.obtainOSCList(any())).thenThrow(new RuntimeException("something bad happen"));
                ResponseEntity<OSCResponse> responseEntity = sut.obtainOSCList(idEmployee);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.INTERNAL_SERVER_ERROR));
            }
        }

        @Nested
        @SuiteDisplayName("Should return 204 (No Content)")
        class ObtainOSCeNoContentTest {

            @Test
            @DisplayName("When OSCList throws Exception")
            public void obtainOSCList_OSCListIsEmpty_ReturnNoContent(){
                when(oscMapper.obtainOSCList(any())).thenReturn(new ArrayList<OSC>());
                ResponseEntity<OSCResponse> responseEntity = sut.obtainOSCList(idEmployee);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.NO_CONTENT));
            }
        }

        @Nested
        @SuiteDisplayName("Should return 200 (OK)")
        class ObtainOSCStatusOKTest {

            @Test
            @DisplayName("When No Exception is Catched")
            public void obtainOSCList_NoExceptionCatched_ReturnsOk(){
                when(oscMapper.obtainOSCList(any())).thenReturn(Arrays.asList(VALID_OSC_A_LIST_1,VALID_OSC_A_LIST_2));
                ResponseEntity<OSCResponse> responseEntity = sut.obtainOSCList(idEmployee);
                assertThat("Status Code Response", responseEntity.getStatusCode(),is(HttpStatus.OK));
            }
        }

    }

}