import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;	

public class Main implements CalculatorInterface {
	
	static final String OPERATOR_TOKENS = "+-*/^";

	/**
	 * Returns whether the next token is a double, false otherwise.
	 *
	 * @param token string containing the token
	 * @return boolean whether the next token is a double
	 */
	boolean nextTokenIsDouble(String token) {
		Scanner in = new Scanner(token);
		return in.hasNextDouble();
	}

	/**
     * Read tokens from an input string, returns a TokenList.
     *
	 * @param input string containing tokens
	 * @return TokenList containing the tokens from the input string
	 */
	public TokenList readTokens(String input) {
    	
    	Scanner in = new Scanner(input);
    	TokenList result = new TokenList();	
    	
    	while(in.hasNext()){
    		String token = in.next();
        	
        	if (tokenIsDouble(token)) {
        		result.add(parseNumber(token));
        	}
        	else if (tokenIsOperator(token)) {
    			result.add(parseOperator(token));
    		} 
    		else if (tokenIsParenthesis(token)) {
    			result.add(parseParenthesis(token));
    		} 
    		else {
    			System.out.println("Incorrect input token.");
    		}
    	}
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
     * @param token string containing a OperatorToken
     * @return OperatorToken object with the value of the input string
	 */
	private OperatorToken parseOperator(String token) {
		Scanner in = new Scanner(token);
		return readOperator(in);
	}

	/**
	 * @param in
	 * @return
	 */
	private OperatorToken readOperator(Scanner in) {
    	String operator = in.next();
    	OperatorToken operator_token = new OperatorToken(operator);
    	return operator_token;
    }

	/**
	 * @param token
	 * @return
	 */
	private ParenthesisToken parseParenthesis(String token) {
		Scanner in = new Scanner(token);
		return readParenthesis(in);
	}

	/**
	 * @param in
	 * @return
	 */
	private ParenthesisToken readParenthesis(Scanner in) {
    	String parenthesis = in.next();
    	ParenthesisToken parenthesis_token = new ParenthesisToken(parenthesis);
    	return parenthesis_token;
    }

	/**
	 * @param token
	 * @return
	 */
	private boolean tokenIsDouble(String token) {
    	  try  
    	  {  
    	    double d = Double.parseDouble(token);  
    	  }  
    	  catch(NumberFormatException nfe)  
    	  {  
    	    return false;  
    	  }  
    	  return true;  
	}

	/**
	 * @param token
	 * @return
	 */
	private boolean tokenIsOperator(String token) {
		for (char ch: OPERATOR_TOKENS.toCharArray()) {
			CharSequence chs = String.valueOf(token);
			if (token.contains(chs)) {
		  		return true;
		  	}
		}
	  	return false;
	  	 
	}

	/**
	 * @param token
	 * @return
	 */
	private boolean tokenIsParenthesis(String token) {
		if (token.contains("(")) {
	  		return true;
	  	}
		else if (token.contains(")")) {
			return true;
		}
	  	return false; 
	}

	/**
	 * @param tokens
	 * @return
	 */
	public Double rpn(TokenList tokens) {
    	int i = 0;
    	DoubleStack double_stack = new DoubleStack();
        while (i < tokens.size()) {
        	Token token = tokens.get(i);
        	if (token.getType() == 1) {
        		double_stack.push(Double.parseDouble(tokens.get(i).getValue()));
        	}
        	else if (token.getType() == 2) {
        		if (double_stack.size() > 1) {
        			double_stack = performOperation(tokens.get(i), double_stack);
        		}
        	}
        	i++;
        }
        System.out.println("aids");
        return double_stack.top();
    }

	/**
	 * @param operator
	 * @param stack
	 * @return
	 */
	private DoubleStack performOperation(Token operator, DoubleStack stack) {
    	double a = stack.pop();
		double b = stack.pop();
    	switch (operator.getValue()) {
			case "+": stack.push(a + b); break;
			case "-": stack.push(a - b); break;
			case "*": stack.push(a * b); break;
			case "/": stack.push(a / b); break;
			case "^": stack.push(Math.pow(a, b)); break;
    	}

    	return stack;
	}

	/**
	 * @param tokens
	 * @return
	 */
	public TokenList shuntingYard(TokenList tokens) {
    	System.out.println("aids2");
    	TokenStack token_stack = new TokenStack();
    	TokenList output_list = new TokenList();
    	int i = 0;
    	
    	while (i < tokens.size()) {
    		Token token = tokens.get(i);
        	if (token.getType() == 1) {
        		output_list.add(token);
        	}
        	else if (token.getType() == 2) {
        		if (token_stack.hasTop()) {
        			//System.out.println(token_stack.top().getPrecedence());
        			if (token_stack.top().getPrecedence() >= token.getPrecedence()) {
            			output_list.add(token_stack.pop());
            			System.out.println("AIDS8");
            		}
        		}
        		System.out.println("AIDS9");
        		token_stack.push(token);
        		
        	}
        	if (token.getValue() == "(") {
        		token_stack.push(token);
        	}
        	if (token.getValue() == ")") {
        		while (token_stack.top().getValue() != "(") {
        			output_list.add(token_stack.pop());
        		}
        		token_stack.pop();
        	}
        	i++;
    	}
    	while (token_stack.size() > 0) {
    		output_list.add(token_stack.pop());
    	}
        		
        return output_list;
    }

	/**
	 *
	 */
	private void start() {
    	Scanner scanner = new Scanner(System.in);
    	
    	while(scanner.hasNextLine()) {
    	    String input = scanner.nextLine();
    	    if(input.equals("exit")) {
    	        break;
    	    }
    	    System.out.println(rpn(shuntingYard(readTokens(input))));
    	}
    	
    }

	/**
	 * @param argv
	 */
	public static void main(String[] argv) {
        new Main().start();
    }
}
