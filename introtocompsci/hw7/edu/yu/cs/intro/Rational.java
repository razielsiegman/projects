package edu.yu.cs.intro;
public class Rational{

	public int numerator;
	public int denominator;

	public Rational(){
		this.numerator = 0;
		this.denominator = 1;
	}

	public String printRational(){
		String number = new String();
		number = "+" + this.getNumerator() + "/" + this.getDenominator();
		return number;
	} 

	public int getNumerator(){
		return numerator;
	}

	public int getDenominator(){
		return denominator;
	}

	public static void main(String[] args){
		Rational firstOperand = new Rational();
		Rational secondOperand = new Rational(35,9);
		//secondOperand.invert();
		Rational rationalNew = new Rational();
		//firstOperand.reduce();
		//rationalNew = firstOperand.add(secondOperand);
		//System.out.println(rationalNew.printRational());
		System.out.println(firstOperand.printRational());
		//System.out.println(rational.printRational());
		System.out.println(rationalNew.toDouble());
	}

	public Rational(int numerator, int denominator){
		if(denominator == 0){
			throw new IllegalArgumentException("Can't divde by 0");
		}
			this.numerator = numerator;
			this.denominator = denominator;
	}

	public void invert(){
		if(numerator == 0){
			throw new UnsupportedOperationException("Can't divide by 0");
		}
		int holder = this.getDenominator();
		this.denominator = this.getNumerator();
		this.numerator = holder;
	}

	public double toDouble(){
		double numeratorNew = this.getNumerator();
		double denominatorNew = this.getDenominator();
		double noFraction = (numeratorNew / denominatorNew);
		return noFraction;
	}

	public Rational reduce(){
		int greater = 0;
		int lesser = 0;
		int remainder = 1;
		int numeratorNew = 0;
		int denominatorNew = 0;
		if(this.getNumerator() == this.getDenominator()){
			numeratorNew = 1;
			denominatorNew = 1;
		}
		else if(this.getNumerator() == 0){
		denominatorNew = 1;
		}
		else{
		if(this.getNumerator() > this.getDenominator()){
			greater = this.getNumerator();
			lesser = this.getDenominator();
		}
		if(this.getNumerator() < this.getDenominator()){
			greater = this.getDenominator();
			lesser = this.getNumerator();
		}
		do{
			int temp = greater / lesser;
			remainder = greater - (lesser * temp);
			greater = lesser;
			lesser = remainder;
		}while(remainder != 0);
		numeratorNew = this.getNumerator() / greater;
		denominatorNew = this.getDenominator() / greater;
		}
		Rational rationalNew = new Rational(numeratorNew,denominatorNew);
		return rationalNew;
	}

	public Rational add(final Rational that){
		Rational finalRational = new Rational((this.numerator + that.numerator), (this.denominator + that.denominator));
		finalRational = finalRational.reduce();
		return finalRational;
	}
}