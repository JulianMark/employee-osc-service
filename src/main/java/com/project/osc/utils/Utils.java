package com.project.osc.utils;

public class Utils {

    public static void validateIdNumber (Integer idNumber){
        if (idNumber == null){
            throw new IllegalArgumentException("El id del empleado no puede ser nulo");
        }
        if (idNumber <= 0){
            throw new IllegalArgumentException("El id del empleado no puede ser menor o igual a 0");
        }
    }
}
