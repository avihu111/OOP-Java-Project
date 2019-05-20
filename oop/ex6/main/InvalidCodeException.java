package oop.ex6.main;

/**
 * this class is the super class of all program exceptions, represents an invalid code exception
 */
public class InvalidCodeException extends Exception {
    private static final long serialVersionUID = 1L;
    @Override
    public String toString() {
        return "there's a general error in the code";
    }
}