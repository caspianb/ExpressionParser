package com.logicalbias.parser.functions;

public class CeilingFunction extends Function {

    public CeilingFunction(String token) {
        super(token, 1);
    }

    @Override
    public double apply(double... args) {
        double a = args[0];
        return Math.ceil(a);
    }
}
