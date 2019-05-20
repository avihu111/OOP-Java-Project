package oop.ex6.Variables;

import oop.ex6.main.InvalidCodeException;

/**
 * the variable exception class.
 */
public class VariableException extends InvalidCodeException {
    private static final long serialVersionUID = 1L;

    /* variable name */
    private final String name;

    /**
     * constructs a new variable exception object
     * @param name method name
     */
    public VariableException(String name) {
        this.name = name;
    }

    /**
     * a default constructor for variable exception object
     */
    public VariableException() {
        this.name = "unknown";
    }

    @Override
    public String toString() {
        return "problem with variable: " + name;
    }
}
