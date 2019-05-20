package oop.ex6.Methods;

import oop.ex6.main.InvalidCodeException;

/**
 * the method exception class
 */
public class MethodException extends InvalidCodeException {
    private static final long serialVersionUID = 1L;
    /* method name */
    private String name ;

    /**
     * constructs a new method exception object
     * @param name method name
     */
    public MethodException(String name) {
        this.name = name;
    }

    /**
     * a default constructor for method exception object
     */
    public MethodException() {
    }

    @Override
    public String toString() {
        return "method " + name + " is invalid";
    }
}
