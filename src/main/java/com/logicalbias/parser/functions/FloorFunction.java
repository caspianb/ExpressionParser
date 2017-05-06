package com.logicalbias.parser.functions;

public class FloorFunction extends Function {

    public FloorFunction(String token) {
        super(token, 1);
    }

    @Override
    public double apply(double... args) {
        double a = args[0];
        return Math.floor(a);
    }
}
