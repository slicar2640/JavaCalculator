import java.util.ArrayList;

public class Evaluator {
    public double evaluate(String str) {
        ArrayList<Token> tokens = tokenize(str);
        ArrayList<Token> parsed = parse(tokens);
        double evaluated = evaluatePostfix(parsed);
        return evaluated;
    }

    public ArrayList<Token> tokenize(String str) {
        ArrayList<Token> tokens = new ArrayList<Token>();

        for (int i = 0; i < str.length(); i++) {
            String token = str.substring(i, i + 1);

            boolean isNumber = token.matches("[0-9.]");
            boolean isOperator = token.matches("[+\\-*/%^()]");

            if (isNumber) {
                int j = i;
                while (j < str.length() && str.substring(j, j + 1).matches("[0-9.]")) {
                    j++;
                }

                tokens.add(new Token("number", str.substring(i, j)));
                i = j - 1;
            } else if (isOperator) {
                tokens.add(new Token("operator", token));
            }
        }

        for (int i = 0; i < tokens.size() - 1; i++) {
            if (tokens.get(i).type.equals("number") || tokens.get(i).lexeme.equals(")")) {
                if (tokens.get(i + 1).lexeme.equals("(")) {
                    tokens.add(i + 1, new Token("operator", "*"));
                }
            }
        }

        return tokens;
    }

    public ArrayList<Token> parse(ArrayList<Token> tokens) {
        ArrayList<Token> outputQueue = new ArrayList<Token>();
        ArrayList<Token> operatorStack = new ArrayList<Token>();

        for (Token token : tokens) {
            if (token.type.equals("number")) {
                outputQueue.add(token);
            } else if (token.type.equals("operator")) {
                if (token.lexeme.equals("(")) {
                    operatorStack.add(token);
                } else if (token.lexeme.equals(")")) {
                    while (operatorStack.size() > 0
                            && !(operatorStack.get(operatorStack.size() - 1).lexeme.equals("("))) {
                        outputQueue.add(operatorStack.get(operatorStack.size() - 1));
                        operatorStack.remove(operatorStack.size() - 1);
                    }
                } else {
                    while (operatorStack.size() > 0 && opPrecedence(
                            operatorStack.get(operatorStack.size() - 1).lexeme) >= opPrecedence(token.lexeme)) {
                        outputQueue.add(operatorStack.get(operatorStack.size() - 1));
                        operatorStack.remove(operatorStack.size() - 1);
                    }
                    operatorStack.add(token);
                }
            }
        }
        while (operatorStack.size() > 0) {
            outputQueue.add(operatorStack.get(operatorStack.size() - 1));
            operatorStack.remove(operatorStack.size() - 1);
        }

        for (int i = 0; i < outputQueue.size(); i++) {
            if (outputQueue.get(i).lexeme.equals("(")) {
                outputQueue.remove(i);
                i--;
            }
        }

        return outputQueue;
    }

    public double evaluatePostfix(ArrayList<Token> postfix) {
        ArrayList<Double> stack = new ArrayList<Double>();

        for (Token token : postfix) {
            if (token.type.equals("number")) {
                stack.add(Double.parseDouble(token.lexeme));
            } else if (token.type.equals("operator")) {
                double operand2 = stack.get(stack.size() - 1);
                stack.remove(stack.size() - 1);
                double operand1 = stack.get(stack.size() - 1);
                stack.remove(stack.size() - 1);

                double result;
                switch (token.lexeme) {
                    case "+":
                        result = operand1 + operand2;
                        break;
                    case "-":
                        result = operand1 - operand2;
                        break;
                    case "*":
                        result = operand1 * operand2;
                        break;
                    case "/":
                        result = operand1 / operand2;
                        break;
                    case "%":
                        result = operand1 % operand2;
                        break;
                    case "^":
                        result = Math.pow(operand1, operand2);
                        break;
                    default:
                        result = 0.0;
                        break;
                }
                stack.add(result);
            }
        }
        return stack.get(0);
    }

    public void printArrayList(ArrayList<Token> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(tokens.get(i).type + " " + tokens.get(i).lexeme);
        }
    }

    public int opPrecedence(String lexeme) {
        if (lexeme.equals("+") || lexeme.equals("-")) {
            return 1;
        } else if (lexeme.equals("*") || lexeme.equals("/") || lexeme.equals("%")) {
            return 2;
        } else if (lexeme.equals("^")) {
            return 3;
        } else {
            return 0;
        }
    }
}
