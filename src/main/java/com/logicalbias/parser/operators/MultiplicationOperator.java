package com.logicalbias.parser.operators;


public class MultiplicationOperator extends Operator {

    public MultiplicationOperator(String token, int precedence) {
        super(token, precedence, 2, Associativity.LEFT);
    }

    @Override
    public double apply(double... args) {
        double a = args[0];
        double b = args[1];
        return a * b;
    }

}
