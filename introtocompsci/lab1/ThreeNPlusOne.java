/**
* Basic outline of the solution:
*
Code receives an input from user with a number.
If the number is even, divide by 2.
If it is odd, multiply by 3 and add 1. 
When reaching the number 1, print out the whole sequence of numbers, as well as how many numbers are in the sequence.
* **************************************

* First pseudocode refinement:
*
Method 1:
Ask user for input.
Send input to method 2.
Receive and print data from method 2.
Method 2:
Create string that has the current integer.
Create new integer to keep track of how many computations are done.
If integer is even, divide by 2, then save as current integer and add to string.  Also, add 1 to computation count.
If integer is odd, multiply by 3 and add 1, then save as current integer and add to string.  Also, add 1 to computation count.
When current integer reaches 1, print computation count and send string to method 1.
* ***************************************
* pseudocode of the solution, which is roughly
* 1:1 with the actual code:
*
Method 1:
Create a scanner.
Create an integer, that is assigned a number from the scanner input.
Call method 2 using this integer.
Receive string from method 2, and then print.
Method 2:
Create a string that contains the current integer.
Create a new integer to keep track of computation count, starting as 1.
Create an iteration that breaks out of itself if the integer is equal to 1.
If integer is even, divide by 2.  Then save this as the current integer.  Then concatinate with string.  Then, add 1 to computation count.
If integer is odd, multiply by 3 and add 1.  Then save this as the current integer.  Then concatinate with string.  Then, add 1 to computation count.
Print the integer of computation count.  Send string to method 1.
*/

import java.util.Scanner;
public class ThreeNPlusOne{

	public static void main(String[] args){
		System.out.print("Please enter a positive number: ");
    	Scanner scanner = new Scanner(System.in);
    	int input = Integer.parseInt(scanner.next());
    	System.out.println(generateThreeN(input));
	}

	public static String generateThreeN(int input){
		String message = new String();
		message = message + input;
		int count = 1;
		while(true){
			if((input / 2)*2 == input){
				input = input / 2;
				message = message + "," + input;
				count = count + 1;
				if(input == 1){
					break;
				}
			}
			else{
				input = (input * 3) + 1;
				message = message + "," + input;
				count = count + 1;
			}
		}
		System.out.println("Your sequence contains " + count + " numbers");
		return message;
	}

}