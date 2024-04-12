package org.example.exceptions;

public class InvalidEmailException extends RuntimeException{
    public InvalidEmailException() {
        super("Email Format Is Wrong!");
    }
}
