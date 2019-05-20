package oop.ex6.Methods;


import oop.ex6.Scopes.Scope;
import oop.ex6.Variables.Variable;
import oop.ex6.main.InvalidCodeException;


import java.util.ArrayList;

/**
 * this class represents a void method of the program
 */
class VoidMethod extends Method {

    private static final String METHOD_TYPE = "void";

    /**
     * constructs a new void method object
     * @param parent the scope containing the method
     * @param name method name
     * @param parameters the method parameters
     * @throws InvalidCodeException when a problem with me
     */
    VoidMethod(Scope parent, String name, ArrayList<Variable> parameters)throws InvalidCodeException {
        super(parent, name, parameters);
    }

    @Override
    String getType() {
        return METHOD_TYPE;
    }
}
