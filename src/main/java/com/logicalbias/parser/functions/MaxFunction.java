package com.logicalbias.parser.functions;

public class MaxFunction extends Function {

    public MaxFunction(String token) {
        super(token, 2);
    }

    @Override
    public double apply(double... args) {
        double a = args[0];
        double b = args[1];
        return Math.max(a, b);
    }
}
