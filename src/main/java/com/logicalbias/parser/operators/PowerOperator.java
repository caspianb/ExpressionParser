package com.logicalbias.parser.operators;

public class PowerOperator extends Operator {

    public PowerOperator(String token, int precedence) {
        super(token, precedence, 2, Associativity.RIGHT);
    }

    @Override
    public double apply(double... args) {
        double a = args[0];
        double b = args[1];
        return Math.pow(a, b);
    }

}
