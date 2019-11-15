package edu.yu.cs.intro;
import java.util.Scanner;
public class CountdownTimer{

	private long pad;

	public CountdownTimer(long pad){
	this.pad = pad;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("What two numbers would you like to time a countdown from? ");
		long timeOne = scanner.nextLong();
		if(timeOne <= 0){
			throw new IllegalArgumentException("Must input a positive number");
		}
		long timeTwo = scanner.nextLong();
		if(timeTwo <= 0){
			throw new IllegalArgumentException("Must input a positive number");
		}
		long pad = 0;
		System.out.print("Would you like to pad the time? ");
		String answer = scanner.next();
		if(answer.equals("yes")){
			System.out.print("How many milliseconds would you like to pad the time by? ");
			pad = scanner.nextLong();
		}
		CountdownTimer timer = new CountdownTimer(pad);
		long milliTimeOne = (timer.countdown(timeOne));
		long milliTimeTwo = (timer.countdown(timeTwo));
		double secondsTimeOne = (double)milliTimeOne/1000d;
		double secondsTimeTwo = (double)milliTimeTwo/1000d;
		System.out.println("Counting down from " + timeOne + " took " + milliTimeOne + " milliseconds, which is " + secondsTimeOne + " seconds");
		System.out.println("Counting down from " + timeTwo + " took " + milliTimeTwo + " milliseconds, which is " + secondsTimeTwo + " seconds");
	}

	public long countdown(long time){
	long milliseconds = 0;
	if(pad == 0){
	Stopwatch stopwatch = new Stopwatch();
	stopwatch.start();
	for(long i = 1; i <= time; i++){
	}
	stopwatch.stop();
	return stopwatch.elapsed();
	}
	else{
	Stopwatch stopwatch = new Stopwatch(pad);
	stopwatch.start();
	for(long i = 1; i <= time; i++){
	}
	stopwatch.stop();
	return stopwatch.elapsed();
	}
	}
}