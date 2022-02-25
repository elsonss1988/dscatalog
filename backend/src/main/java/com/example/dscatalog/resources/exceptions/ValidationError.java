package com.example.dscatalog.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError{
    private List<FieldMessage> erros=new ArrayList<>();

    public List<FieldMessage> getErros() {
        return erros;
    }

    public void addErros(String fieldName, String message){
        erros.add(new FieldMessage(fieldName,message));
    }
}
