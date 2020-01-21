package com.project.osc.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.project.osc.utils.Utils.validateIdNumber;

@DisplayName("Utils methods test")
class UtilsTest {

    @Test
    void obtainIdNumber_NumberIsNull_ThrowIllegalArgumentException () {
        Assertions.assertThrows(IllegalArgumentException.class,()-> validateIdNumber(null));
    }

    @Test
    void obtainIdNumber_NumberIsZeroOrLess_ThrowIllegalArgumentException () {
        Assertions.assertThrows(IllegalArgumentException.class,()-> validateIdNumber(0));
    }

}