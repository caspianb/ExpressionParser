package com.logicalbias.parser.operators;

public class FactorialOperator extends Operator {

    public FactorialOperator(String token, int precedence) {
        super(token, precedence, 1, Associativity.RIGHT);
    }

    @Override
    public double apply(double... args) {
        double a = args[0];
        double value = a;
        for (int i = 2; i < a; i++) {
            value = value * i;
        }
        return value;
    }

}
