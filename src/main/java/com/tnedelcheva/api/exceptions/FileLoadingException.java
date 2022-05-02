package com.tnedelcheva.api.exceptions;

public class FileLoadingException extends RuntimeException {
    private String message;

    public FileLoadingException(String msg ) {
        super(msg);
        this.message = msg;
    }
}
