package ru.practicum.gateway.common.exceptions;

public class MethodNotAllowedException extends RuntimeException {
    public MethodNotAllowedException(String mes) {
        super(mes);
    }
}
