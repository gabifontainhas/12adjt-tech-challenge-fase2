package br.com.gabifontainhas.techchallenge.application.exception;

public class MenuItemAlreadyExistsException extends RuntimeException {
    public MenuItemAlreadyExistsException(String message) {
        super(message);
    }
}
