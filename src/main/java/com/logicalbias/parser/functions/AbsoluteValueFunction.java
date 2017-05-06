package com.logicalbias.parser.functions;

public class AbsoluteValueFunction extends Function {

    public AbsoluteValueFunction(String token) {
        super(token, 1);
    }

    @Override
    public double apply(double... args) {
        double a = args[0];
        return Math.abs(a);
    }

}
