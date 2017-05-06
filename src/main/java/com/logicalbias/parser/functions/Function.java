package com.logicalbias.parser.functions;

public abstract class Function {

    private final String token;
    private final int numberOfArguments;

    public Function(String token, int numberOfArguments) {
        this.token = token;
        this.numberOfArguments = numberOfArguments;
    }

    public String getToken() {
        return token;
    }

    public int getNumberOfArguments() {
        return numberOfArguments;
    }

    public abstract double apply(double... args);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Function)) return false;
        Function other = (Function) obj;
        if (token == null) {
            if (other.token != null) return false;
        }
        else if (!token.equals(other.token)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Function [token=" + token + "]";
    }

}
