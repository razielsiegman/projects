package edu.yu.cs.intro;
import java.util.Scanner;
public class IfArithmetic
{
	public static void main(String[] args) 
	{
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("What operation would you like to do? You can enter add, sub, mul, div");
		System.out.print("Please enter it now: ");
		String operation = scanner.next();
		System.out.print("Please enter the first operand: ");
		int operand1 = scanner.nextInt();
		System.out.print("Please enter the second operand: ");
		int operand2 = scanner.nextInt();
		int finalnumber;

		if(operation .equals("add")){
			finalnumber = operand1 + operand2;
			System.out.print(operand1 + " + " + operand2 + " = " + finalnumber);
		}
		else if(operation .equals("sub")){
			finalnumber = operand1 - operand2;
			System.out.print(operand1 + " - " + operand2 + " = " + finalnumber);
		}
		else if(operation .equals("mul")){
			finalnumber = operand1 * operand2;
			System.out.print(operand1 + " * " + operand2 + " = " + finalnumber);
		}
		else if(operation .equals("div")){
			finalnumber = operand1 / operand2;
			System.out.print(operand1 + " / " + operand2 + " = " + finalnumber);
		}
		else{
			System.out.print("Invalid operation: you must enter one of the following: add, sub, mul, div");
		}
	}
}