package br.com.golfinvest.data.model;

import java.text.MessageFormat;

public enum Errors {

    INVALID_NUMBER_FORMAT("Formato do número [{0}] é inválido.");

    private String description;

    Errors(String desc){ this.description = desc; }


    public String getDescription(Object... parameters) {
        if (parameters != null && parameters.length > 0) {
            return MessageFormat.format(description, parameters);
        }else {
            return  description;
        }
    }
}
