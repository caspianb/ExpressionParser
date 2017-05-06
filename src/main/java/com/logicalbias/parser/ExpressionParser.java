package com.logicalbias.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.logicalbias.parser.functions.AbsoluteValueFunction;
import com.logicalbias.parser.functions.CeilingFunction;
import com.logicalbias.parser.functions.FloorFunction;
import com.logicalbias.parser.functions.Function;
import com.logicalbias.parser.functions.MaxFunction;
import com.logicalbias.parser.functions.MinFunction;
import com.logicalbias.parser.functions.RoundFunction;
import com.logicalbias.parser.operators.AdditionOperator;
import com.logicalbias.parser.operators.DivisionOperator;
import com.logicalbias.parser.operators.FactorialOperator;
import com.logicalbias.parser.operators.ModulusOperator;
import com.logicalbias.parser.operators.MultiplicationOperator;
import com.logicalbias.parser.operators.Operator;
import com.logicalbias.parser.operators.PowerOperator;
import com.logicalbias.parser.operators.SubtractionOperator;

public class ExpressionParser {

    private static final String FUNCTION_ARGUMENT_SEPARATOR = ",";
    private static final String LEFT_PAREN = "(";
    private static final String RIGHT_PAREN = ")";

    private static final Map<String, Function> supportedFunctions = new HashMap<>();
    private static final Map<String, Operator> supportedOperators = new HashMap<>();
    private static final Map<String, Double> supportedConstants = new HashMap<>();

    static {
        supportedFunctions.put("abs", new AbsoluteValueFunction("abs"));
        supportedFunctions.put("min", new MinFunction("min"));
        supportedFunctions.put("max", new MaxFunction("max"));
        supportedFunctions.put("floor", new FloorFunction("floor"));
        supportedFunctions.put("ceil", new CeilingFunction("ceil"));
        supportedFunctions.put("round", new RoundFunction("round"));

        supportedOperators.put("+", new AdditionOperator("+", 1));
        supportedOperators.put("-", new SubtractionOperator("-", 1));
        supportedOperators.put("*", new MultiplicationOperator("*", 3));
        supportedOperators.put("/", new DivisionOperator("/", 3));
        supportedOperators.put("%", new ModulusOperator("%", 3));
        supportedOperators.put("^", new PowerOperator("^", 5));
        supportedOperators.put("!", new FactorialOperator("!", 7));

        supportedConstants.put("pi", Math.PI);
        supportedConstants.put("e", Math.E);
    }

    protected Tokenizer tokenizer = new Tokenizer();

    public ExpressionParser() {
    }

    public double evaluate(String expression) {
        // First we need to tokenize the expression
        List<String> tokens = tokenize(expression);

        // Now we will run the shunting-yard algorithm to generate an RPN list of the expression tree
        List<String> outputQueue = shuntingYard(tokens);
        System.out.println(outputQueue);

        // Finally we will scan through and evaluate the expression
        double value = evaluate(outputQueue);
        return value;
    }

    /**
     * Evaluates and returns the value of a mathematical expression configured as a 
     * valid list of tokens in reverse polish notation order. 
     */
    protected double evaluate(List<String> rpnTokens) {
        LinkedList<Double> arguments = new LinkedList<>();

        // Iterate through the list of tokens looking for functions
        for (String token : rpnTokens) {
            // Try and locate a function for this token
            Function function = null;
            if (isFunction(token)) {
                function = getFunction(token);
            }
            else if (isOperator(token)) {
                function = getOperator(token);
            }

            // If token is a numeric value then push it to the front of the arguments stack
            else if (isNumber(token)) {
                arguments.push(getNumber(token));
            }
            else if (isConstant(token)) {
                arguments.push(getConstant(token));
            }
            else {
                throw new RuntimeException("Invalid token detected in expression... " + token);
            }

            if (function != null) {
                // Retrieve the argument(s) for this operator
                // Take care here to retrieve the arguments in the *reverse order* from the stack
                int numArgs = function.getNumberOfArguments();
                double[] args = new double[numArgs];
                for (int i = numArgs - 1; i >= 0; i--) {
                    args[i] = arguments.pop();
                }

                // Apply the operator to these values and retrieve the new value
                double value = function.apply(args);

                // Push the calculated to the front of the stack
                arguments.push(value);
            }
        }

        // The arguments stack should now only contain one value -- the value of the expression
        if (arguments.size() > 1) {
            throw new RuntimeException("Invalid expression... Multiple values remained after evaluation: " + arguments);
        }

        return arguments.pop();
    }

    /**
     * Tokenizes the specified expression and performs rudimentary sanity checks prior to returning;
     */
    protected List<String> tokenize(String expression) {
        List<String> tokens = tokenizer.tokenize(expression);

        System.out.println("Tokens: " + tokens);

        // Preliminary fast sanity check of tokens
        for (int i = 0; i < tokens.size() - 1; i++) {
            String leftToken = tokens.get(i);
            String rightToken = tokens.get(i + 1);

            // There should never be two adjacent numbers or operators in the mathematical expression...
            if (isNumber(leftToken) && isNumber(rightToken)) {
                throw new RuntimeException("Invalid expression detected! Operator expected between " + leftToken + " " + rightToken);
            }

            // TODO: How to perform this validation AND handle factorial operators?
            /*if (isOperator(leftToken) && isOperator(rightToken)) {
                throw new RuntimeException("Invalid expression detected! Numeric value expected " + leftToken + " " + rightToken);
            }*/
        }

        return tokens;
    }

    /**
     * Executes Djikstra's Shunting-yard algorithm against the specified list of tokens.
     * Returns the RPN (Reverse Polish Notation) queue of the set expression.<br />
     * @see <a href="http://en.wikipedia.org/wiki/Shunting-yard_algorithm">http://en.wikipedia.org/wiki/Shunting-yard_algorithm</a>
     * 
     */
    protected List<String> shuntingYard(List<String> tokens) {
        List<String> outputQueue = new LinkedList<>();
        Stack<String> stack = new Stack<>();

        for (String token : tokens) {
            if (isFunction(token)) {
                stack.push(token);
            }
            else if (isNumber(token) || isConstant(token)) {
                outputQueue.add(token);
            }
            else if (isOperator(token)) {
                Operator o1 = getOperator(token);

                // while there is an operator token, o2, at the top of the stack, and
                //      either token is left-associative and token precedence <= o2
                //      OR token has less precedence than o2 
                //      THEN pop o2 off stack, onto output queue
                while (!stack.isEmpty() && isOperator(stack.peek())) {
                    Operator o2 = getOperator(stack.peek());
                    if (o1.getPrecedence() < o2.getPrecedence() ||
                            (o1.isLeftAssociative() && o1.getPrecedence() <= o2.getPrecedence())) {
                        String o2Token = stack.pop();
                        outputQueue.add(o2Token);
                    }
                    else {
                        break;
                    }
                }

                stack.push(token);
            }
            else if (token.equals(FUNCTION_ARGUMENT_SEPARATOR)) {
                // Until the token at the top of the stack is a left parenthesis, 
                // pop operators off the stack onto the output queue. If no left parentheses are encountered, 
                // either the separator was misplaced or parentheses were mismatched.
                while (!stack.peek().equals(LEFT_PAREN)) {
                    String stackToken = stack.pop();
                    outputQueue.add(stackToken);

                    if (stack.isEmpty()) {
                        throw new RuntimeException("Mismatched parentheses or comma detected in expression... ");
                    }
                }
            }
            else if (token.equals(LEFT_PAREN)) {
                stack.push(token);
            }
            else if (token.equals(RIGHT_PAREN)) {
                // 1. Until the token at the top of the stack is a left parenthesis, pop operators off the stack onto the output queue.
                // 2. Pop the left parenthesis from the stack, but not onto the output queue.
                // 3. If the token at the top of the stack is a function token, pop it onto the output queue.
                // 4. If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
                while (!stack.peek().equals(LEFT_PAREN)) {
                    String stackToken = stack.pop();
                    outputQueue.add(stackToken);

                    if (stack.isEmpty()) {
                        throw new RuntimeException("Mismatched parentheses detected in expression... ");
                    }
                }

                // Pop the top element off the stack -- It should be a LEFT_PAREN due to while check above
                stack.pop();

                if (!stack.isEmpty() && isFunction(stack.peek())) {
                    String stackToken = stack.pop();
                    outputQueue.add(stackToken);
                }
            }
        }

        while (!stack.isEmpty()) {
            String stackToken = stack.pop();

            if (isGroupingOperator(stackToken)) {
                throw new RuntimeException("Mismatched parentheses detected in expression... ");
            }

            outputQueue.add(stackToken);
        }

        return outputQueue;
    }

    protected boolean isFunction(String token) {
        return supportedFunctions.containsKey(token);
    }

    public Function getFunction(String token) {
        return supportedFunctions.get(token);
    }

    protected boolean isOperator(String token) {
        return supportedOperators.containsKey(token);
    }

    protected Operator getOperator(String token) {
        return supportedOperators.get(token);
    }

    protected boolean isConstant(String token) {
        return supportedConstants.containsKey(token);
    }

    protected Double getConstant(String token) {
        return supportedConstants.get(token);
    }

    protected boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
        }
        catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    protected Double getNumber(String token) {
        return Double.parseDouble(token);
    }

    protected boolean isGroupingOperator(String token) {
        return token.matches("[()]");
    }

}
