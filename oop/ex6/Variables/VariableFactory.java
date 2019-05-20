package oop.ex6.Variables;

import oop.ex6.main.InvalidCodeException;

/**
 * this class is responsible for creating variable objects by request.
 */
public class VariableFactory {

    /* variable types */
    static final String BOOLEAN = "boolean";
    static final String STRING = "String";
    static final String INT = "int";
    static final String DOUBLE = "double";
    static final String CHAR = "char";

    /**
     * this method is creating a variable object according to variable type.
     *
     * @param type  the variable type
     * @param name  the variable name
     * @param value the value of initialization
     * @return a compatible type object
     * @throws VariableException if the type is invalid
     */
    private static Variable create(String type, String name, String value, boolean isFinal, boolean isArgument)
            throws InvalidCodeException {
        switch (type) {
            case BOOLEAN:
                return new BooleanVariable(name, value, isFinal, isArgument);
            case DOUBLE:
                return new DoubleVariable(name, value, isFinal, isArgument);
            case INT:
                return new IntegerVariable(name, value, isFinal, isArgument);
            case STRING:
                return new StringVariable(name, value, isFinal, isArgument);
            case CHAR:
                return new CharVariable(name, value, isFinal, isArgument);
            default:
                throw new VariableException(name);
        }
    }

    public static Variable create(String type, String name, boolean isFinal, boolean isArgument) throws InvalidCodeException{
        return create(type, name, null, isFinal, isArgument);
    }

    public static Variable create(String type, String name, String value, boolean isFinal) throws InvalidCodeException{
        return create(type, name, value, isFinal, false);
    }
}