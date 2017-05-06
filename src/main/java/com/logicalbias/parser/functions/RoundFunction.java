package com.logicalbias.parser.functions;

public class RoundFunction extends Function {

    public RoundFunction(String token) {
        super(token, 1);
    }

    @Override
    public double apply(double... args) {
        double a = args[0];
        return Math.round(a);
    }

}
