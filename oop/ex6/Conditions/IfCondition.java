package oop.ex6.Conditions;

import oop.ex6.Scopes.Scope;
import oop.ex6.main.InvalidCodeException;
import oop.ex6.main.LineReader;

/**
 * this class represents an If condition object of the program
 */
public class IfCondition {

    /**
     * the scope containing the if condition
     */
    private Scope scope;

    /**
     * constructs a new if condition variable object.
     *
     * @param parent the scope containing the if condition
     * @throws ConditionException when a problem in the condition is found
     */
    public IfCondition(Scope parent) throws ConditionException {
        if (parent == null) {
            throw new ConditionException();
        }
        scope = new Scope(parent, LineReader.ScopeType.INNER);

    }

    /**
     * reads the inner scope
     * @throws InvalidCodeException when there's an error in the scope
     */
    public void readScope() throws InvalidCodeException {
        scope.readScope();
    }
}
