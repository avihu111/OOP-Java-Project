package oop.ex6.Conditions;

import oop.ex6.Scopes.Scope;

/**
 * this class represents a while condition object of the program. Extends from IfCondition as they are
 * treated similarly.
 */
public class WhileCondition extends IfCondition{

    /**
     * constructs a new while condition variable object.
     * @param parent the scope containing the while condition
     * @throws ConditionException when a problem in the condition is found
     */
    public WhileCondition(Scope parent) throws ConditionException {
        super(parent);
    }
}
