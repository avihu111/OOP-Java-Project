package oop.ex6.Methods;
import oop.ex6.Scopes.Scope;
import oop.ex6.Variables.Variable;
import oop.ex6.main.InvalidCodeException;
import java.util.ArrayList;

/**
 * this class is responsible for creating method objects by request.
 */
public class MethodFactory {

    /* method types */
    private static final String VOID = "void";

    /**
     * this method is creating a method object according to method type.
     * @param scope the scope containing the method
     * @param type method type
     * @param name method name
     * @param parameters method parameters
     * @return a new method object
     * @throws InvalidCodeException if there is an error with method
     */
    public static Method create(Scope scope, String type, String name, ArrayList<Variable> parameters)throws InvalidCodeException {

        if (type.equals(VOID)){
            return new VoidMethod(scope, name, parameters);
        }

        // can add other method types in the future

        else {
            throw new MethodException(name);
        }
    }
}
