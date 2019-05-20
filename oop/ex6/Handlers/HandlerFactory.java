package oop.ex6.Handlers;

import oop.ex6.Conditions.ConditionException;
import oop.ex6.Conditions.IfCondition;
import oop.ex6.Conditions.WhileCondition;
import oop.ex6.Methods.Method;
import oop.ex6.Methods.MethodException;
import oop.ex6.Methods.MethodFactory;
import oop.ex6.Scopes.Scope;
import oop.ex6.Variables.Variable;
import oop.ex6.Variables.VariableException;
import oop.ex6.Variables.VariableFactory;
import oop.ex6.main.InvalidCodeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class contains all sub-classes of the line handlers, and is responsible for gathering all handlers
 * to relevant arrays, according to handlers that are relevant to a specific scope type.
 */
public class HandlerFactory {

    /* general patterns */
    private final String methodName = "([A-Za-z][A-Za-z_\\d]*)";
    private final String variableName = "([A-Za-z][A-Za-z_\\d]*| *_[A-Za-z_\\d]+)";
    private final Pattern variableNamePattern = Pattern.compile(variableName);
    private final String variableTypes = "(int|double|char|boolean|String)";
    private final String methodParameter = " *" + variableTypes + "\\s+" + variableName + "\\s*";
    private static final HandlerFactory instance = new HandlerFactory();

    /* scope handlers */
    private final Handler[] globalHandlers;
    private final Handler[] methodHandlers;
    private final Handler[] innerHandlers;

    /* reserved words in java */
    private final String[] reservedWords = {"void", "true", "false", "int", "double", "if", "char", "while",
            "String", "boolean", "final", "return"};

    /**
     * creates a new Handler factory object
     */
    private HandlerFactory() {
        // creating all handlers instances
        Handler declarationHandler = new DeclarationHandler();
        Handler voidMethodHandler = new VoidMethodHandler();
        Handler ifHandler = new IfHandler();
        Handler whileHandler = new WhileHandler();
        Handler finalHandler = new FinalHandler();
        Handler assignmentHandler = new AssignmentHandler();
        Handler methodCallHandler = new MethodCallHandler();
        Handler returnHandler = new ReturnHandler();
        // splitting them by scope types - each one contains relevant lints to it's scope
        globalHandlers = new Handler[]{declarationHandler, voidMethodHandler, finalHandler, assignmentHandler};
        methodHandlers = new Handler[]{declarationHandler, ifHandler, whileHandler, finalHandler, assignmentHandler,
                methodCallHandler, returnHandler};
        innerHandlers = methodHandlers.clone();
    }

    /**
     * @return an instance of this class
     */
    public static HandlerFactory getInstance() {
        return instance;
    }

    /**
     * @return the array with all relevant handlers for global scope
     */
    public Handler[] getGlobalHandlers() {
        return globalHandlers;
    }

    /**
     * @return the array with all relevant handlers for method scope
     */
    public Handler[] getMethodHandlers() {
        return methodHandlers;
    }

    /**
     * @return the array with all relevant handlers for inner scope
     */
    public Handler[] getInnerHandlers() {
        return innerHandlers;
    }

    /**
     * represents a handler for a variable declaration
     */
    private class DeclarationHandler implements Handler {
        /* declaration patterns */
        private final Pattern declarationPattern = Pattern.compile(variableTypes + "\\s+(.*);");
        private final Pattern optionalAssignmentPattern = Pattern.compile(variableName + "\\s*(?:=\\s*(.*))?");

        @Override
        public boolean handle(Matcher matcher, Scope scope) throws InvalidCodeException {
            String type = matcher.group(1);
            String declarationString = matcher.group(2);
            String[] declares = masterSplitter(declarationString, ",");
            if (declares == null) {
                throw new VariableException();
            }
            for (String declare : declares) {
                createVariable(type, declare, optionalAssignmentPattern, scope, false);
            }
            return false;
        }

        @Override
        public Pattern getPattern() {
            return declarationPattern;
        }
    }

    /**
     * represents a handler for a void method creation
     */
    private class VoidMethodHandler implements Handler {

        /* void method declaration patterns */
        private final Pattern parameterPattern = Pattern.compile(methodParameter);
        private final Pattern voidMethodPattern = Pattern.compile("void\\s+" + methodName + "\\s*\\((.*)\\)\\s*\\{\\s*");

        @Override
        public Pattern getPattern() {
            return voidMethodPattern;
        }

        @Override
        public boolean handle(Matcher matcher, Scope scope) throws InvalidCodeException {
            String methodName = matcher.group(1);
            String parametersString = matcher.group(2);
            ArrayList<Variable> parameterVariables = new ArrayList<>();
            String[] parameters = masterSplitter(parametersString, ",");
            if (parameters != null) {
                for (String parameter : parameters) {
                    parameter = parameter.trim();
                    Matcher paramMatcher = parameterPattern.matcher(parameter);
                    if (paramMatcher.matches()) {
                        String type = paramMatcher.group(1);
                        String name = paramMatcher.group(2);
                        parameterVariables.add(VariableFactory.create(type,name , false, true));
                    } else {
                        throw new MethodException(methodName);
                    }
                }
            }
            // need to loop over the parameters groups and add them
            Method method = MethodFactory.create(scope, "void", methodName, parameterVariables);
            scope.addMethod(method);
            return false;
        }
    }

    /**
     * represents a handler for an if block creation
     */
    class IfHandler implements Handler {
        /* if statement patterns */
        private final Pattern ifPattern = Pattern.compile("if\\s*\\((.*)\\)\\s*\\{");
        private final String booleanSplit = "\\|\\||&&";

        void conditionPreCheck(Matcher matcher, Scope scope) throws InvalidCodeException {
            String booleanString = matcher.group(1);
            String[] values = masterSplitter(booleanString, booleanSplit);
            if (values == null) {
                throw new ConditionException();
            }
            for (String value : values) {
                value = value.trim();
                if (checkVariables("boolean", value, scope))
                    VariableFactory.create("boolean", "boolean", value, false);
            }
        }

        @Override
        public boolean handle(Matcher matcher, Scope scope) throws InvalidCodeException {
            conditionPreCheck(matcher, scope);
            IfCondition ifCondition = new IfCondition(scope);
            ifCondition.readScope();
            return false;
        }

        @Override
        public Pattern getPattern() {
            return ifPattern;
        }
    }

    /**
     * represents a handler for while loop creation
     */
    private class WhileHandler extends IfHandler implements Handler {
        /* while statement patterns */
        private final Pattern whilePattern = Pattern.compile("while\\s*\\((.*)\\)\\s*\\{");

        @Override
        public boolean handle(Matcher matcher, Scope scope) throws InvalidCodeException {
            conditionPreCheck(matcher, scope);
            WhileCondition whileCondition = new WhileCondition(scope);
            whileCondition.readScope();
            return false;
        }

        @Override
        public Pattern getPattern() {
            return whilePattern;
        }
    }

    /**
     * represents a handler for a final variable declaration
     */
    private class FinalHandler implements Handler {
        /* final declaration patterns */
        final Pattern finalPattern = Pattern.compile("final\\s+" + variableTypes + "\\s+(.*);");
        private final Pattern mandatoryAssignmentPattern = Pattern.compile(variableName + "\\s*=\\s*(.*)");

        @Override
        public boolean handle(Matcher matcher, Scope scope) throws InvalidCodeException {
            String type = matcher.group(1);
            String declarationsString = matcher.group(2);
            String[] declarations = masterSplitter(declarationsString, ",");
            if (declarations == null) {
                throw new VariableException();
            }
            for (String declaration : declarations) {
                createVariable(type, declaration, mandatoryAssignmentPattern, scope, true);

            }
            return false;
        }

        @Override
        public Pattern getPattern() {
            return finalPattern;
        }
    }

    /**
     * represents a handler for a variable assignment
     */
    private class AssignmentHandler implements Handler {
        /* variable assignment pattern */
        final Pattern assignmentPattern = Pattern.compile(variableName + "\\s*=\\s*(\\S+)\\s*;");

        @Override
        public boolean handle(Matcher matcher, Scope scope) throws InvalidCodeException {
            String name = matcher.group(1);
            String value = matcher.group(2);
            Variable variable = scope.getLocalVariable(name);
            if (variable == null) {
                variable = scope.getVariable(name);
                if (variable == null || variable.getIsFinal()) {
                    throw new VariableException();
                }
                VariableFactory.create(variable.getType(), variable.getName(), value, false);
            } else {
                variable.setValue(value);
            }
            return false;
        }

        @Override
        public Pattern getPattern() {
            return assignmentPattern;
        }
    }

    /**
     * represents a handler for a method call
     */
    private class MethodCallHandler implements Handler {
        /* method call pattern */
        private final Pattern methodCallPattern = Pattern.compile(methodName + "\\s*\\((.*)\\)\\s*;");

        @Override
        public Pattern getPattern() {
            return methodCallPattern;
        }

        @Override
        public boolean handle(Matcher matcher, Scope scope) throws InvalidCodeException {
            // check method name
            String methodName = matcher.group(1);
            String parametersString = matcher.group(2);
            Method method = scope.getMethod(methodName);
            if (method == null) {
                throw new MethodException(methodName);
            }
            // check arguments
            String[] parameters = masterSplitter(parametersString, ",");
            if (parameters == null) {
                if (method.getParametersNumber() == 0) {
                    return false;
                }
                throw new MethodException(methodName);
            }

            if (parameters.length != method.getParametersNumber()) {
                throw new MethodException(methodName);
            }

            for (int i = 0; i < parameters.length; i++) {
                String parameter = parameters[i];
                parameter = parameter.trim();
                Variable methodArgument = method.getParameters().get(i);
                if (checkVariables(methodArgument.getType(), parameter, scope)) {
                    VariableFactory.create(methodArgument.getType(), "variable", parameter, false);
                }
            }
            return false;
        }

    }

    /**
     * represents a handler for the return keyword
     */
    private class ReturnHandler implements Handler {
        /* return statement pattern */
        private final Pattern returnPattern = Pattern.compile("return\\s*;");

        @Override
        public boolean handle(Matcher matcher, Scope scope) {
            // used to determine if it was last line before method end
            return true;
        }

        @Override
        public Pattern getPattern() {
            return returnPattern;
        }
    }


    /**
     * checks if a given name matches a variable in the scope
     */
    private boolean checkVariables(String type, String name, Scope scope) throws InvalidCodeException {
        if (variableNamePattern.matcher(name).matches() && !Arrays.asList(reservedWords).contains(name)) {
            Variable variable = scope.getVariable(name);
            if (variable == null)
                throw new VariableException();

            if (!variable.isCompatibleAs(type) || (variable.getValue() == null && !variable.getIsArgument()))
                throw new VariableException();

            return false;
        }
        return true;
    }

    /**
     * splits a line by a delimiter and performs validations
     *
     * @param line      the line to split
     * @param delimiter the delimiter to split by
     * @return an array of strings or null if line only contains spaces
     * @throws InvalidCodeException in case of invalid line
     */
    private String[] masterSplitter(String line, String delimiter) throws InvalidCodeException {
        line = line.trim();
        if (line.isEmpty()) {
            return null;
        }
        if (line.startsWith(delimiter) || line.endsWith(delimiter)) {
            throw new VariableException();
        }
        return line.split(delimiter);

    }

    /**
     * creates a variable in an assignment
     *
     * @param type    variable type
     * @param row     contains the name and value in a string
     * @param pattern the pattern to find groups in the row
     * @param scope   the scope to add the variables
     * @param isFinal decides if the variable is final
     * @throws InvalidCodeException in case of an error
     */
    private void createVariable(String type, String row, Pattern pattern, Scope scope, boolean isFinal)
            throws InvalidCodeException {
        row = row.trim();
        Matcher assignmentMather = pattern.matcher(row);
        if (assignmentMather.matches()) {
            String name = assignmentMather.group(1);
            String value = assignmentMather.group(2);
            // checking if the value is a variable that exists in the scope
            if (value == null || checkVariables(type, value, scope)) {
                Variable variable = VariableFactory.create(type, name, value, isFinal);
                scope.addVariable(variable);
            }
        } else {
            throw new VariableException();
        }
    }
}
