package com.project.osc.utils;

public class Utils {

    public static void validateIdNumber (Integer idNumber){
        if (idNumber == null){
            throw new IllegalArgumentException("ID employee must not be null");
        }
        if (idNumber <= 0){
            throw new IllegalArgumentException("ID employee must not be less than or equal to zero");
        }
    }
}
