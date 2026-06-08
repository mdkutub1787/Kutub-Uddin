package com.logicsoftbd.lsl.utils.Validator.validations;

 
public interface Validation {

    String getErrorMessage();

    boolean isValid(String text);

}