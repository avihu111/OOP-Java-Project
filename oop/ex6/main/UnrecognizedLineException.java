package oop.ex6.main;

/**
 * this class represents an unrecognized line exception, thrown when the line code does not fit any legal
 * pattern
 */
class UnrecognizedLineException extends InvalidCodeException {
    private static final long serialVersionUID = 1L;
    @Override
    public String toString() {
        return "unrecognized line was found in code";
    }
}
