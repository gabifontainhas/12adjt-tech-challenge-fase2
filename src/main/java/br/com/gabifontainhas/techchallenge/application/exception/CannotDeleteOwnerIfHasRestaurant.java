package br.com.gabifontainhas.techchallenge.application.exception;

public class CannotDeleteOwnerIfHasRestaurant extends RuntimeException {
    public CannotDeleteOwnerIfHasRestaurant(String message) {
        super(message);
    }
}
