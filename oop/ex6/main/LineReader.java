package oop.ex6.main;


import oop.ex6.Handlers.Handler;
import oop.ex6.Handlers.HandlerFactory;
import oop.ex6.Methods.MethodException;
import oop.ex6.Scopes.Scope;
import oop.ex6.Scopes.UnclosedScopeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 * represents a line reader
 */
public class LineReader {
    private static final String COMMENT = "//";
    private static final String SCOPE_CLOSE = "}";
    private static final String SCOPE_OPEN = "{";
    /**
     * the file path to read
     */
    private final File file;
    /**
     * the scanner object
     */
    private Scanner scanner;
    /**
     * for validating method return is the last line
     */
    private boolean isLastLineReturn;

    /**
     * creates all scope types, and attaches their specific handlers array
     */
    public enum ScopeType {
        GLOBAL(HandlerFactory.getInstance().getGlobalHandlers()),
        METHOD(HandlerFactory.getInstance().getMethodHandlers()),
        INNER(HandlerFactory.getInstance().getInnerHandlers());

        private final Handler[] handlers;

        ScopeType(Handler[] handlers) {
            this.handlers = handlers;
        }

        Handler[] getHandlers() {
            return handlers;
        }
    }

    /**
     * creates a new reader, and initializes a scanner
     *
     * @param fileName the code file to check
     * @throws FileNotFoundException if file it not found
     */
    LineReader(String fileName) throws FileNotFoundException {
        file = new File(fileName);
        scanner = new Scanner(file);
    }

    /**
     * reads a line of of code
     *
     * @param scope     the scope reading
     * @param scopeType the scope type
     * @return true if scope is still open, false otherwise
     * @throws InvalidCodeException in case of an error
     */
    public boolean readLine(Scope scope, ScopeType scopeType) throws InvalidCodeException {
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith(COMMENT))
                return true;

            line = line.trim();
            // empty line
            if (line.isEmpty())
                return true;

            if (line.equals(SCOPE_CLOSE)) {
                if (scopeType == ScopeType.METHOD) {
                    if (isLastLineReturn) {
                        return false;
                    } else {
                        throw new MethodException();
                    }
                } else if (scopeType == ScopeType.GLOBAL) {
                    throw new UnclosedScopeException();
                } else {
                    return false;
                }
            }

            for (Handler handler : scopeType.getHandlers()) {
                Matcher matcher = handler.getPattern().matcher(line);
                if (matcher.matches()) {
                    isLastLineReturn = handler.handle(matcher, scope);
                    return true;
                }
            }

            throw new UnrecognizedLineException();
        } else if (scope.parent != null) {
            // throw something
            throw new UnclosedScopeException();
        }

        return false;
    }

    /**
     * skips all line of the method by reading {} until method scope is closed.
     *
     * @throws InvalidCodeException if the read is wrong
     */
    public void skipLines() throws InvalidCodeException {
        int counter = 1;
        while (counter != 0) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(SCOPE_OPEN)) {
                    counter++;
                } else if (line.contains(SCOPE_CLOSE)) {
                    counter--;
                }

            } else {
                throw new UnclosedScopeException();
            }
        }
    }

    /**
     * searches for the line of a method creation
     *
     * @param type the method type
     * @param name the method name
     */
    public void goToMethod(String type, String name) {
        String line = scanner.nextLine();
        while (!(line.contains(type) && line.contains(name))) {
            line = scanner.nextLine();
        }
    }

    /**
     * resets the scanner for another read
     *
     * @throws FileNotFoundException in case the find doesn't exist
     */
    void reset() throws FileNotFoundException {
        scanner = new Scanner(file);
    }
}


