package oop.ex6.Scopes;

import oop.ex6.main.InvalidCodeException;

/**
 * represents an unclosed scope exception object. Thrown when scope is not closed as expected
 */
public class UnclosedScopeException extends InvalidCodeException {
    private static final long serialVersionUID = 1L;
    @Override
    public String toString() {
        return "there is a scope unclosed properly";
    }
}