package com.logicalbias;

import com.logicalbias.parser.ExpressionParser;

public class Main {
    public static void main(String[] args) {
        ExpressionParser parser = new ExpressionParser();

        //System.out.print("Enter an expression to parse: ");
        //Scanner scanner = new Scanner(System.in);
        //String expression = scanner.nextLine();

        String expression = "5! * 2";

        System.out.println("Calculating value for: " + expression);

        double value = parser.evaluate(expression);
        System.out.println(value);
    }
}
