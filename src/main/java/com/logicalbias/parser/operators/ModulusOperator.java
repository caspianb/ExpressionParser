package com.logicalbias.parser.operators;


public class ModulusOperator extends Operator {

    public ModulusOperator(String token, int precedence) {
        super(token, precedence, 2, Associativity.LEFT);
    }

    @Override
    public double apply(double... args) {
        double a = args[0];
        double b = args[1];
        return a % b;
    }
}
