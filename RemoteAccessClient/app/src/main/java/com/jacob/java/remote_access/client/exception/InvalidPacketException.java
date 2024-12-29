package com.jacob.java.remote_access.client.exception;

public class InvalidPacketException extends RuntimeException {
    public InvalidPacketException(String message) {
        super(message);
    }
    public InvalidPacketException(String message, Throwable e) {
        super(message, e);
    }

    public InvalidPacketException(Throwable e) {
        super(e);
    }
}
