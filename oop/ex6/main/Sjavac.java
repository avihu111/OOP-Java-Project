package oop.ex6.main;

import oop.ex6.Methods.Method;
import oop.ex6.Scopes.Scope;

import java.io.FileNotFoundException;

/**
 * this class holds the main method, responsible for receiving the given code file and handle errors.
 */
public class Sjavac {

    private static final int VALID_ARGUMENT_NUMBER = 1;
    private static final int LEGAL_CODE = 0;
    private static final int ILLEGAL_CODE = 1;
    private static final int IO_ERROR = 2;

    public static void main(String[] args) {
        if (args.length != VALID_ARGUMENT_NUMBER) {
            System.err.println("wrong argument number");
            System.out.println(IO_ERROR);
        } else {
            try {
                LineReader lineReader = new LineReader(args[0]);
                Scope scope = new Scope(lineReader, LineReader.ScopeType.GLOBAL);
                scope.readScope();
                lineReader.reset();
                for (Method method: scope.getAllMethods().values()){
                    method.readScope();
                }
                System.out.println(LEGAL_CODE);  // when code is legal
            } catch (FileNotFoundException e) {
                System.err.println("file is not found");
                System.out.println(IO_ERROR);
            } catch (InvalidCodeException e) {
                System.out.println(ILLEGAL_CODE); // when code is illegal
                System.err.println(e.toString());
            }
        }
    }
}
