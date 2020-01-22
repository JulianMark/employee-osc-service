package com.project.osc.utils;

import com.project.osc.model.OSC;
import com.project.osc.service.http.OSCResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class ListOSCValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListOSCValidator.class);


    public Function<List<OSC>, ResponseEntity<OSCResponse>> obtainListValidator() {
        return this::obtainList;
    }

    private ResponseEntity<OSCResponse> obtainList(List<OSC> oscList) {
        if (!oscList.isEmpty()) {
            LOGGER.info("the list of osc for the employee was obtained");
            return ResponseEntity.ok(new OSCResponse(oscList));
        }
        LOGGER.info("the list of osc for the employee was not obtained");
        return ResponseEntity.noContent().build();
    }
}
