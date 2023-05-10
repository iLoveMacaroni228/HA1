package de.mcc.Exceptions;

public class NoProductsInCartException extends Exception{
    public NoProductsInCartException(String message){
        super(message);
    }
}
