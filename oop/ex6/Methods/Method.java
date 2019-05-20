package oop.ex6.Methods;

import oop.ex6.Scopes.Scope;
import oop.ex6.Variables.Variable;
import oop.ex6.main.InvalidCodeException;
import oop.ex6.main.LineReader;

import java.util.ArrayList;

/**
 * this class represents a general method of the program file
 */
public abstract class Method {

    /* method name */
    private final String name;

    /* the scope containing the method */
    private final Scope scope;

    /* the method parameters (as assigned in the method declaration */
    private ArrayList<Variable> parameters;

    /**
     * constructs a new method object
     * @param parent the scope containing the method
     * @param name method name
     * @param parameters the method parameters
     * @throws InvalidCodeException when a problem with method
     */
    Method(Scope parent, String name, ArrayList<Variable> parameters) throws InvalidCodeException {

        if (parent.parent != null){  // checking if the method declaration is not under global scope
            throw new MethodException(name);
        }

        this.name = name;
        scope = new Scope(parent, LineReader.ScopeType.METHOD);
        // adding parameters as variables of the scope
        this.parameters = parameters;

        for (Variable parameter: parameters) {
            scope.addVariable(parameter);
        }

        // read {, } until method end:
        scope.skipLines();
    }

    /**
     * @return the parameters of the method
     */
    public ArrayList<Variable> getParameters() {
        return parameters;
    }

    /**
     * in charge for reading the method content
     * @throws InvalidCodeException if an error in method is found
     */
    public void readScope()throws InvalidCodeException{
        scope.readMethod(getType(), name);
    }

    /**
     * @return the method name
     */
    public String getName() {
        return name;
    }

    /**
     * @return method type
     */
    @SuppressWarnings("SameReturnValue")
    abstract String getType();  // not used in this implementation, but will be needed for further expanses.

    /**
     * @return the number of parameters in the method declaration
     */
    public int getParametersNumber() {
        return parameters.size();
    }
}
