package br.com.golfinvest.data.model;

public class BaseException extends Exception{
    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) { super(message, cause); }
}
