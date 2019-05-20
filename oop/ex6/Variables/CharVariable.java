package oop.ex6.Variables;

import oop.ex6.main.InvalidCodeException;

import java.util.regex.Pattern;

/**
 * this class represents a char variable of the program
 */
public class CharVariable extends Variable {

    /* the pattern of a valid char var*/
    private static final Pattern pattern = Pattern.compile("\'.\'");


    /**
     * constructs a new char variable object.
     * @param name var name
     * @param value var value
     * @param isArgument is variable is an argument of a method
     * @throws VariableException when value or name are illegal.
     */
    CharVariable(String name, String value, boolean isFinal, boolean isArgument) throws InvalidCodeException {
        super(name, value, isFinal, isArgument);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String getType() {
        return VariableFactory.CHAR;
    }
}