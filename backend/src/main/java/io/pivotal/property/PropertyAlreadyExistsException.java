package io.pivotal.property;

public class PropertyAlreadyExistsException extends RuntimeException {

    public PropertyAlreadyExistsException(String message) {
        super(message);
    }

}
