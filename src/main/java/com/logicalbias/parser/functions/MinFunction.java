package com.logicalbias.parser.functions;

public class MinFunction extends Function {

    public MinFunction(String token) {
        super(token, 2);
    }

    @Override
    public double apply(double... args) {
        double a = args[0];
        double b = args[1];
        return Math.min(a, b);
    }
}
