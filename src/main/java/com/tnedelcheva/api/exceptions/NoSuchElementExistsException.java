package com.tnedelcheva.api.exceptions;

public class NoSuchElementExistsException extends RuntimeException {

        private String message;

        public NoSuchElementExistsException() {}

        public NoSuchElementExistsException(String msg) {
            super(msg);
            this.message = msg;
        }
}
