package edu.yu.cs.intro.test;
import edu.yu.cs.intro.MethodArithmetic;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodArithmeticTest
{
	@Test
	public void arrayLengthDoublerNormal(){
		int[] values = {2,5,4};
		int[] expected = MethodArithmetic.arrayLengthDoubler(values);
		assertEquals("testing doubling array of size 3",expected[0],values[0]);
		assertEquals("testing doubling array of size 3",expected[1],values[1]);
		assertEquals("testing doubling array of size 3",expected[2],values[2]);
		assertEquals("testing doubling array of size 3",expected.length,6);
	}

	@Test
	public void arrayLengthDoublerBoundries(){
		int[] values = {4};
		int[] expected = MethodArithmetic.arrayLengthDoubler(values);
		assertEquals("testing doubling array of size 1",expected[0], values[0]);
		assertEquals("testing doubling array of size 1",expected.length,2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void arrayLengthDoublerinvalid(){
		int[] array = new int[0];
		MethodArithmetic.arrayLengthDoubler(array);
	}

	@Test
	public void isArithmeticNormal(){
		String operation = "add";
		assertEquals("testing input of proper operation",true,MethodArithmetic.isArithmetic(operation));
	} 

	@Test
	public void isArithmeticBoundaries(){
		String operation = "";
		assertEquals("testing input of improper operation,",false,MethodArithmetic.isArithmetic(operation));
	}

	@Test(expected = IllegalArgumentException.class)
	public void isArithmeticInvalid(){
		String operation = null;
		MethodArithmetic.isArithmetic(operation);
	}

	@Test 
	public void addBoundaries(){
		int[] array = {2,0,1};
		assertEquals("testing addition of 0 and 1 equals 1","0+1=1",MethodArithmetic.add(array));
	}

	@Test
	public void addNormal(){
		int[] array = {4,53,6,2,32};
		assertEquals("testing addition of 53,6,2,32 equals 93","53+6+2+32=93",MethodArithmetic.add(array));
	}

	@Test(expected = IllegalArgumentException.class)
	public void addInvalid(){
		int[] array = {5, 45, 3};
		MethodArithmetic.add(array);
	}

	@Test 
	public void subtractBoundaries(){
		int[] array = {2,0,-1};
		assertEquals("testing subtraction of 0 and -1 equals 1","0--1=1",MethodArithmetic.subtract(array));
	}

	@Test
	public void subtractNormal(){
		int[] array = {3,30,7,3};
		assertEquals("testing addition of 30,7,3 equals 20","30-7-3=20",MethodArithmetic.subtract(array));
	}

	@Test(expected = IllegalArgumentException.class)
	public void subtractInvalid(){
		int[] array = {8, 4325, 45, 6, 3, 6};
		MethodArithmetic.subtract(array);
	}

	@Test 
	public void multiplyBoundaries(){
		int[] array = {2,0,0};
		assertEquals("testing multiplication of 0 and 0 equals 0","0*0=0",MethodArithmetic.multiply(array));
	}

	@Test
	public void multiplyNormal(){
		int[] array = {3, 10, 4, 20};
		assertEquals("testing multiplication of 10, 4, 20 equals 800","10*4*20=800",MethodArithmetic.multiply(array));
	}

	@Test(expected = IllegalArgumentException.class)
	public void multiplyInvalid(){
		int[] array = {9, 54, 643, 7};
		MethodArithmetic.multiply(array);
	}

	@Test 
	public void divideBoundaries(){
		int[] array = {2,1,1};
		assertEquals("testing division of 1 and 1 equals 1","1/1=1",MethodArithmetic.divide(array));
	}

	@Test
	public void divideNormal(){
		int[] array = {4, 120, 4, 5, 2};
		assertEquals("testing multiplication of 120, 4, 5, 2 equlas 3","120/4/5/2=3",MethodArithmetic.divide(array));
	}

	@Test(expected = ArithmeticException.class)
	public void divideNnvalid(){
		int[] array = {2, 6, 0};
		MethodArithmetic.divide(array);
	}

}