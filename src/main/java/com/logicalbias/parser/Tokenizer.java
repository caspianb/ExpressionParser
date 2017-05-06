package com.logicalbias.parser;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<String>();

        try {
            StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(expression));
            tokenizer.ordinaryChar('/');
            tokenizer.lowerCaseMode(true);

            while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                switch (tokenizer.ttype) {
                    case StreamTokenizer.TT_NUMBER:
                        tokens.add(String.valueOf(tokenizer.nval));
                        break;
                    case StreamTokenizer.TT_WORD:
                        tokens.add(tokenizer.sval);
                        break;
                    default:
                        // For a single character token, ttype value is the single character, converted to an integer 
                        tokens.add(String.valueOf((char) tokenizer.ttype));
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        tokens = fixNegativeOperators(tokens);
        return tokens;
    }

    /**
     * This method will cycle through the list of tokens and look for two
     * <i>adjacent</i> numeric tokens. If the second number is negative
     * then this method will change the number to a positive value and
     * insert a minus operator '-' between the two numeric tokens.
     */
    protected List<String> fixNegativeOperators(List<String> tokens) {
        for (int i = 0; i < tokens.size() - 1; i++) {
            String leftToken = tokens.get(i);
            String rightToken = tokens.get(i + 1);

            if (isNumber(leftToken) && isNumber(rightToken)) {
                // This value is negative!
                if (rightToken.startsWith("-")) {
                    // Strip the minus sign
                    rightToken = rightToken.substring(1);

                    // Replace current right side token with the positive equivalent
                    tokens.set(i + 1, rightToken);

                    // Add the minus sign between the tokens
                    tokens.add(i + 1, "-");
                }
            }
        }

        return tokens;
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

}
