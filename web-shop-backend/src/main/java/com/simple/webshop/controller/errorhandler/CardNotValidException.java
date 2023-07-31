package com.simple.webshop.controller.errorhandler;

public class CardNotValidException extends RuntimeException {

    public CardNotValidException() {
        super("Card information is not valid");
    }
}
