//import java.io.PrintStream;
//import java.util.HashMap;
//import java.util.Map;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main implements CalculatorInterface {
    private static final String OPERATOR_TOKENS = "+-*/^";

    /**
     * Returns whether the next token is a double, false otherwise.
     *
     * @param token string containing the token
     * @return boolean whether the next token is a double
     * @deprecated
     */
    boolean nextTokenIsDouble(String token) {
        Scanner in = new Scanner(token);
        return in.hasNextDouble();
    }

    /**
     * Reads tokens from an input string and returns a TokenList.
     *
     * @param input string containing tokens
     * @return TokenList containing the tokens from the input string
     */
    public TokenList readTokens(String input) {
        Scanner in = new Scanner(input);
        TokenList result = new TokenList();

        while (in.hasNext()) {
            String token = in.next();

            if (tokenIsDouble(token)) {
                result.add(parseNumber(token));
            } else if (tokenIsOperator(token)) {
                result.add(parseOperator(token));
            } else if (tokenIsParenthesis(token)) {
                result.add(parseParenthesis(token));
            } else {
                System.out.println("Incorrect input token.");
            }
        }

        result.evaluate();

        return result;
    }

    /**
     * Parses a NumberToken from a string.
     *
     * @param token string containing a NumberToken
     * @return NumberToken object with the value of the input string
     */
    private NumberToken parseNumber(String token) {
        Scanner in = new Scanner(token);
        return readNumber(in);
    }

    /**
     * Returns a new NumberToken from a Scanner object.
     *
     * @param in Scanner object containing the token
     * @return NumberToken containing the token as value
     */
    private NumberToken readNumber(Scanner in) {
        double number = Double.parseDouble(in.next());
        return new NumberToken(number);
    }

    /**
     * Parses an OperatorToken from a string.
     *
     * @param token string containing an OperatorToken
     * @return OperatorToken object with the value of the input string
     */
    private OperatorToken parseOperator(String token) {
        Scanner in = new Scanner(token);
        return readOperator(in);
    }

    /**
     * Returns a new OperatorToken from a Scanner object.
     *
     * @param in Scanner object containing the token
     * @return OperatorToken containing the token as value
     */
    private OperatorToken readOperator(Scanner in) {
        String operator = in.next();
        return new OperatorToken(operator);
    }

    /**
     * Parses a ParenthesisToken from a string.
     *
     * @param token string containing a ParenthesisToken
     * @return ParenthesisToken object with the value of the input string
     */
    private ParenthesisToken parseParenthesis(String token) {
        Scanner in = new Scanner(token);
        return readParenthesis(in);
    }

    /**
     * Returns a new ParenthesisToken from a Scanner object.
     *
     * @param in Scanner object containing the token
     * @return ParenthesisToken containing the token as value
     */
    private ParenthesisToken readParenthesis(Scanner in) {
        String parenthesis = in.next();
        return new ParenthesisToken(parenthesis);
    }

    /**
     * Asserts if a token is a double.
     *
     * @param token string containing a token
     * @return boolean whether a string containing a token is a double
     */
    private boolean tokenIsDouble(String token) {
        try {
            Double.parseDouble(token);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    /**
     * Asserts if a token is an operator.
     *
     * @param token string containing an operator
     * @return boolean whether a string containing a token is an operator
     */
    private boolean tokenIsOperator(String token) {
        for (char ch : OPERATOR_TOKENS.toCharArray()) {
            CharSequence chs = String.valueOf(ch);
            if (token.contains(chs)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Asserts if a token is a parenthesis.
     *
     * @param token string containing a parenthesis
     * @return boolean whether a string containing a token is a parenthesis
     */
    private boolean tokenIsParenthesis(String token) {
        if (token.contains("(")) {
            return true;
        } else if (token.contains(")")) {
            return true;
        }

        return false;
    }

    /**
     * Evaluates a postfix(rpn) expression.
     *
     * @param tokens TokenList containing a postfix expression
     * @return a double containing the outcome of the evaluated expression
     */
    public Double rpn(TokenList tokens) {
        DoubleStack stack = new DoubleStack();

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getType() == 1) {
                stack.push(Double.parseDouble(tokens.get(i).getValue()));
            } else if (token.getType() == 2) {
                if (stack.size() > 1) {
                    stack = performOperation(tokens.get(i), stack);
                }
            }
        }

        return stack.top();
    }

    /**
     * Evaluates an expression of 2 doubles and an operator.
     *
     * @param operator a token of type operator
     * @param stack    a stack of doubles
     * @return DoubleStack with the evaluated expression
     */
    private DoubleStack performOperation(Token operator, DoubleStack stack) {
        double a = stack.pop();
        double b = stack.pop();

        switch (operator.getValue()) {
            case "+":
                stack.push(a + b);
                break;
            case "-":
                stack.push(a - b);
                break;
            case "*":
                stack.push(a * b);
                break;
            case "/":
                stack.push(a / b);
                break;
            case "^":
                stack.push(Math.pow(a, b));
                break;
        }

        return stack;
    }

    /**
     * Converts infix expressions into postfix expressions.
     *
     * @param tokens TokenList containing an infix expression
     * @return TokenList containing a postfix(rpn) expression
     */
    public TokenList shuntingYard(TokenList tokens) {
        TokenStack stack = new TokenStack();
        TokenList output = new TokenList();

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getType() == 1) {
                output.add(token);
            } else if (token.getType() == 2) {
                while (stack.hasTop() && stack.top().getType() == 2
                        && stack.top().getPrecedence() >= token.getPrecedence()) {
                    output.add(stack.pop());
                }

                if (!Objects.equals(token.getValue(), "(")) {
                    if (!Objects.equals(token.getValue(), ")")) {
                        stack.push(token);
                    }
                }

            }
            if (Objects.equals(token.getValue(), "(")) {
                stack.push(token);
            }
            if (Objects.equals(token.getValue(), ")")) {
                while (stack.hasTop() && !Objects.equals(stack.top().getValue(), "(")) {
                    if (stack.top().getType() == 2) {
                        output.add(stack.pop());
                    }
                }
                stack.pop();
            }
        }

        while (stack.size() > 0) {
            output.add(stack.pop());
        }

        output.evaluate();

        return output;
    }

    /**
     * Starts the program.
     */
    private void start() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            System.out.println(rpn(shuntingYard(readTokens(input))));
        }
    }

    /**
     * Runs the APCalculator.
     *
     * @param argv list of strings
     */
    public static void main(String[] argv) {
        new Main().start();
    }
}
