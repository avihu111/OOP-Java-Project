package oop.ex6.Handlers;

import oop.ex6.Scopes.Scope;
import oop.ex6.main.InvalidCodeException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this interface is created to use a for loop iterating all legal
 * options for a code line (prevents multiple conditions and uses polymorphism)
 */
public interface Handler {

    /**
     * this method is handling a legal line of the given code file, such as collecting variables, handle
     * variables assignments, verifying method calls etc.
     *
     * @param matcher the certain line matcher
     * @param scope   the current scope
     * @return false when finished handling the line and no error is found
     * @throws InvalidCodeException when illegal code is found
     */
    boolean handle(Matcher matcher, Scope scope) throws InvalidCodeException;

    /**
     * @return the pattern fits the line type
     */
    Pattern getPattern();
}