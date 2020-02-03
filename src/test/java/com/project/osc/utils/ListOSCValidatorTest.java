package com.project.osc.utils;

import com.project.osc.model.OSC;
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

@DisplayName("Validator List of OSC")
class ListOSCValidatorTest {

    @Mock
    private OSC osc = new OSC(1,"Afulic");

    @InjectMocks
    private ListOSCValidator sut;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("When list is empty. Should return 204 (No Content)")
    void obtainEmptyList_ListIsEmpty_ReturnsNonContent () {
        List<OSC> emptyList = Arrays.asList();
        ResponseEntity<OSCResponse> responseEntity = sut.apply(emptyList);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NO_CONTENT));
    }

    @Test
    @DisplayName("When list is not empty. Should return 200 (OK)")
    void obtainList_ListIsNotEmpty_ReturnsOk () {
        List<OSC> oscList = Arrays.asList(osc);
        ResponseEntity<OSCResponse> responseEntity = sut.apply(oscList);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getOscList().get(0).toString(),
                is(oscList.get(0).toString()));
    }
}