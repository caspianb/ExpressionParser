package com.logicalbias.parser.operators;

import com.logicalbias.parser.functions.Function;

public abstract class Operator extends Function {

    public enum Associativity {
        LEFT, RIGHT;
    }

    private final int precedence;
    private final Associativity associativity;

    public Operator(String token, int precedence, int numberOfArguments, Associativity associativity) {
        super(token, numberOfArguments);
        this.precedence = precedence;
        this.associativity = associativity;
    }

    public int getPrecedence() {
        return precedence;
    }

    public boolean isLeftAssociative() {
        return this.associativity == Associativity.LEFT;
    }

    public abstract double apply(double... args);

}
