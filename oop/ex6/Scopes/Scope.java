package oop.ex6.Scopes;

import oop.ex6.Methods.Method;
import oop.ex6.Methods.MethodException;
import oop.ex6.Variables.Variable;
import oop.ex6.Variables.VariableException;
import oop.ex6.main.InvalidCodeException;
import oop.ex6.main.LineReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * this class represents a certain scope in the program. Scope can be nested, thus every scope has a
 * "pointer" ( by composition ) to its upper scope
 */
public class Scope {

    /** the upper scope of this scope */
    public Scope parent;

    /* a line reader object - so the scope can read its own content */
    private final LineReader lineReader;

    /* contains all variables relevant for this scope */
    private final HashMap<String, Variable> variables;

    /* contains all methods relevant for this scope*/
    private LinkedHashMap<String, Method> methods;

    /* scope type */
    private final LineReader.ScopeType scopeType;

    /**
     * creates a new scope object for the program
     * @param parent the upper scope for this scope
     * @param scopeType scope type - (inner/ method / global)
     */
    public Scope(Scope parent, LineReader.ScopeType scopeType) {
        this.parent = parent;
        this.lineReader = parent.lineReader;
        this.variables = new HashMap<>();
        this.scopeType = scopeType;
    }

    /**
     * creates a new scope object for the program
     * @param lineReader a line reader object
     * @param scopeType the scope type
     */
    public Scope(LineReader lineReader, LineReader.ScopeType scopeType) {
        this.lineReader = lineReader;
        this.variables = new HashMap<>();
        this.methods = new LinkedHashMap<>();
        this.scopeType = scopeType;
    }

    /**
     * in charge for reading the scope content
     * @throws InvalidCodeException if an error in scope is found
     */
    public void readScope() throws InvalidCodeException {
        boolean isScopeOpen = true;
        while (isScopeOpen) {
            isScopeOpen = lineReader.readLine(this, scopeType);
        }
    }

    /**
     *
     * @param type the type of the method to search
     * @param name the name of the method to search
     * @throws InvalidCodeException in case of an error
     */
    public void readMethod(String type, String name) throws InvalidCodeException {
        lineReader.goToMethod(type, name);
        readScope();
    }

    /**
     * @param variableName variable name to be found
     * @return returns the requested variable if exists in the program
     */
    public Variable getVariable(String variableName) {
        Scope current = this;
        while (current != null) { // searching in all upper scopes
            if (current.variables.containsKey(variableName)) {
                return current.variables.get(variableName);
            }
            current = current.parent;
        }
        return null;
    }

    /**
     * @param variableName the name of the variable to find.
     * @return the requested variable if exists in this scope
     */
    public Variable getLocalVariable(String variableName) {
        return variables.get(variableName);

    }

    /**
     * @param variable the variable object to add to this scope
     * @throws InvalidCodeException if an error with addition occurs.
     */
    public void addVariable(Variable variable) throws InvalidCodeException{
        // if name exist in current scope or there is a method with this name already
        if (variables.containsKey(variable.getName()) || (methods != null && methods.containsKey(variable.getName()))){
            throw new VariableException(variable.getName());
        }

        this.variables.put(variable.getName(), variable);
    }

    /**
     * @param method the method object to add to this scope
     * @throws InvalidCodeException if an error with addition occurs.
     */
    public void addMethod(Method method) throws InvalidCodeException{

        if (methods.containsKey(method.getName())){
            throw new MethodException(method.getName());
        }

        this.methods.put(method.getName(), method);
    }

    /**
     * skipping a the lines of this scope for this read.
     * @throws InvalidCodeException if scope is not closed properly
     */
    public void skipLines() throws InvalidCodeException {
        lineReader.skipLines();
    }

    /**
     * @param methodName the name of the method to find.
     * @return the requested method if exists in this scope
     */
    public Method getMethod(String methodName){
        Scope current = this;
        while (current.parent != null) {
            current = current.parent;
        }
        return current.methods.get(methodName);
    }

    /**
     * @return all the methods in this scope
     */
    public LinkedHashMap<String, Method> getAllMethods() {
        return methods;
    }
}


