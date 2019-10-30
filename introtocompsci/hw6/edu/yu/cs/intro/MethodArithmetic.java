package edu.yu.cs.intro;

import java.util.Scanner;

public class MethodArithmetic {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Specify an operation. You can enter \"add\", \"sub\", \"mul\", \"div\", \"pow\"");
            System.out.println("To turn off the calculator, enter \"off\"");
            System.out.print("Please enter your operation: ");
            String operation = scanner.next();
            try{
                if (isArithmetic(operation)) {
                    System.out.println(arithmetic(operation, scanner));
                }
                else if (operation.equalsIgnoreCase("pow")) {
                    System.out.println(exponent(scanner));
                }
                else if (operation.equalsIgnoreCase("off")) {
                    break;
                }
            }catch(ArithmeticException e){
                System.out.println("Please only enter mathematically valid inputs!");
            }
        }
    }

    public static int[] getOperands(Scanner scanner) {
       	int[] operands = new int[3];
       	int count = 0;
       		while(true){
	    		System.out.println("Please enter an operand. To stop entering operands, enter \"done\"");
    			String input = scanner.next();
    			
	    			if(input.equalsIgnoreCase("done")){
    					break;
	    			}
		    		else if(operands.length == count + 1){
		    			operands = arrayLengthDoubler(operands);
	    				count += 1;
    					int parsedinput = Integer.parseInt(input);
    					operands[count] = parsedinput;
    					operands[0] = count;
		    		}
    				else{
    				count += 1;
    				int parsedinput = Integer.parseInt(input);
    				operands[count] = parsedinput;
    				operands[0] = count;
    				}

    		}
        return operands;
    }

    public static int[] arrayLengthDoubler(int[] array) {
    	if(array.length == 0){
    		throw new IllegalArgumentException("Must insert array with at least length one");
    	}
    	int[] arrayDoubled = new int[array.length*2];
    	for(int i = 0; i < array.length; i++){
    		arrayDoubled[i] = array[i];
    	}
        return arrayDoubled;
    }

    public static String exponent(Scanner scanner) {
    	System.out.print("Please enter the base: ");
    	int base = Integer.parseInt(scanner.next());
    	System.out.print("Please enter the exponent (positive numbers only): ");
    	int exponent = Integer.parseInt(scanner.next());
    	if (exponent < 1){
    		throw new ArithmeticException();
    	}
    	int result = 1;
    	for(int i = 1; i <= exponent; i++){
    		result *= base;
    	}
    	return base + " raised to the " + exponent + " power = " + result;
    }

    public static boolean isArithmetic(String operation) {
    	if(operation == null){
    	throw new IllegalArgumentException("no content in string");
    	}
    	if((operation.equalsIgnoreCase("add"))||(operation.equalsIgnoreCase("sub"))||(operation.equalsIgnoreCase("mul"))||(operation.equalsIgnoreCase("div"))){
        	return true;
    	}
    	else{
    		return false;
    	}
    }

    public static String arithmetic(String operation, Scanner scanner) throws ArithmeticException{
    	int[] operands = getOperands(scanner);
    	String message = null;
    	if(operation.equalsIgnoreCase("add")){
    		message = add(operands);
    	}
    	else if(operation.equalsIgnoreCase("sub")){
    		message = subtract(operands);
    	}
    	else if(operation.equalsIgnoreCase("mul")){
    		message = multiply(operands);
    	}
    	else if(operation.equalsIgnoreCase("div")){
    		message = divide(operands);
    	}
    	return message;
    }

    public static String add(int[] operands) throws ArithmeticException{
    	if(operands[0] > (operands.length - 1)){
    		throw new IllegalArgumentException("First value in array must be the total number of operands");
    	}
        int result = operands[1] + operands[2];
    	for(int i = 3; i <= operands[0]; i++){
    		result += (operands[i]);
    	}
	    String message = new String();
    	for(int i = 2; i <= (operands[0]); i++){
 		message = message + "+" + operands[i];
 		}
 		message = operands[1] + message + "=" + result;
    	return message;

    }
    public static String subtract(int[] operands) throws ArithmeticException{
    	if(operands[0] > (operands.length - 1)){
    	throw new IllegalArgumentException("First value in array must be the total number of operands");
    	}
        int result = operands[1] - operands[2];
    	for(int i = 3; i <= operands[0]; i++){
    		result -= (operands[i]);
    	}
	    String message = new String();
    	for(int i = 2; i <= (operands[0]); i++){
 		message = message + "-" + operands[i];
 		}
 		message = operands[1] + message + "=" + result;
    	return message;
    }

    public static String multiply(int[] operands) throws ArithmeticException{
    	if(operands[0] > (operands.length - 1)){
    	throw new IllegalArgumentException("First value in array must be the total number of operands");
    	}
        int result = operands[1] * operands[2];
    	for(int i = 3; i <= operands[0]; i++){
    		result *= (operands[i]);
    	}
	    String message = new String();
    	for(int i = 2; i <= (operands[0]); i++){
 		message = message + "*" + operands[i];
 		}
 		message = operands[1] + message + "=" + result;
    	return message;
    }

    public static String divide(int[] operands) throws ArithmeticException{
    	if(operands[0] > (operands.length - 1)){
    	throw new IllegalArgumentException("First value in array must be the total number of operands");
    	}
        int result = operands[1] / operands[2];
    	for(int i = 3; i <= operands[0]; i++){
    		result /= (operands[i]);
    	}
	    String message = new String();
    	for(int i = 2; i <= (operands[0]); i++){
 		message = message + "/" + operands[i];
 		}
 		message = operands[1] + message + "=" + result;
    	return message;
    }

}

