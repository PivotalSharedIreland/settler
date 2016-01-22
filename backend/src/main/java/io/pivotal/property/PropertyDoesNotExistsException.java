package io.pivotal.property;

public class PropertyDoesNotExistsException extends RuntimeException {

    public PropertyDoesNotExistsException(String message) {
        super(message);
    }

}
