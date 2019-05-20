package oop.ex6.Variables;

import oop.ex6.main.InvalidCodeException;

import java.util.regex.Pattern;

/**
 * this abstract class represents a general variable of the program file
 */
public abstract class Variable {

    /* var name */
    private final String name;

    /* var value */
    private String value;

    /* is this variable declared as final */
    private final boolean isFinal;

    /* is variable an argument of a method */
    private final boolean isArgument;

    /**
     * constructs a new variable object
     * @param name  var name
     * @param value var initial value
     * @throws VariableException in case the variable value or name is invalid
     */
    Variable(String name, String value, boolean isFinal, boolean isArgument) throws InvalidCodeException {
        this.name = name;
        this.value = value;
        this.isFinal = isFinal;
        this.isArgument = isArgument;

        if (isValueIllegal()) {
            throw new VariableException(name);
        }
    }

    /**
     * @return variable name
     */
    public String getName() {
        return name;
    }

    /**
     * this method is updating the value of a variable
     * @param value the new value
     * @throws VariableException if value is illegal
     */
    public void setValue(String value) throws VariableException {
        this.value = value;
        if (isFinal || isValueIllegal()) {
            throw new VariableException(name);
        }
    }

    /**
     * verifies if the given value is legal according to type and syntax rules.
     * @return true if the value is legal.
     */
    private boolean isValueIllegal() {
        return value != null && !getPattern().matcher(value).matches();
    }

    /**
     * @return the valid value pattern, according to var type
     */
    protected abstract Pattern getPattern();

    /**
     * @return the value of the var
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the var type
     */
    public abstract String getType();

    /**
     * this method is checking if the type of a variable matches to another variable type, in order to
     * verify assignments
     * @param otherType other type
     * @return true if types are compatible
     */
    public boolean isCompatibleAs(String otherType){
        return getType().equals(otherType);
    }

    /**
     * @return true if the variable is an argument of a method
     */
    public boolean getIsArgument() {
        return isArgument;
    }

    /**
     * @return true if the variable is assigned as final
     */
    public boolean getIsFinal() {
        return isFinal;
    }
}