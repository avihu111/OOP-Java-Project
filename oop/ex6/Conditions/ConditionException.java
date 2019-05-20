package oop.ex6.Conditions;

import oop.ex6.main.InvalidCodeException;

/**
 * represents a condition exception (if / while)
 */
public class ConditionException extends InvalidCodeException {
    private static final long serialVersionUID = 1L;
    @Override
    public String toString() {
        return "there is a problem with condition statement";
    }
}
