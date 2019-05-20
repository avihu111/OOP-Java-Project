package oop.ex6.Variables;

import oop.ex6.main.InvalidCodeException;

import java.util.regex.Pattern;

/**
 * this class represents an integer variable of the program
 */
class IntegerVariable extends Variable {

    /* the pattern of a valid int var*/
    private static final Pattern pattern = Pattern.compile("-?\\d+");

    /**
     * constructs a new int variable object.
     * @param name    var name
     * @param value   var value
     * @param isFinal is the variable declared as final
     * @param isArgument is variable an argument of a method
     * @throws VariableException when value or name are illegal.
     */
    IntegerVariable(String name, String value, boolean isFinal, boolean isArgument) throws InvalidCodeException {
        super(name, value, isFinal, isArgument);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String getType() {
        return VariableFactory.INT;
    }


    @Override
    public boolean isCompatibleAs(String otherType) {
        return otherType.equals(VariableFactory.BOOLEAN) || otherType.equals(VariableFactory.INT) ||
                otherType.equals(VariableFactory.DOUBLE);
    }

}
