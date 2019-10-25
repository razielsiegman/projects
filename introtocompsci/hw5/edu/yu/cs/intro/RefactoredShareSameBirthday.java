package edu.yu.cs.intro;
public class RefactoredShareSameBirthday 
{
	public static void main(String[] args)
	{
	System.out.println("Number of experminents per population size: 10,000");
	System.out.println("Population size: 10.  Shared Birthday Frequency: " + runExperiments(10, 10000));
	System.out.println("Population size: 23.  Shared Birthday Frequency: " + runExperiments(23, 10000));
	System.out.println("Population size: 70.  Shared Birthday Frequency: " + runExperiments(70, 10000));

	}
	public static boolean runSingleExperiment (int populationSize)
	{
		boolean data = false;
		int[] birthdates = new int[populationSize];
		for(int date = 0; date < birthdates.length; date++){
		birthdates[date] = (int)(Math.random() *365);
		}
		for(int i = 0; i < birthdates.length; i++){
			for(int j = i + 1; j < birthdates.length; j++){
				if (birthdates[i] == birthdates[j]){
					data = true;
				}
			}
		}
		return data;
	}
	public static double runExperiments (int populationSize, int nExperiments)
	{
		if (populationSize < 2){
			throw new IllegalArgumentException("Must conduct test with a population of at least two people.");
		}
		if (nExperiments < 1){
			throw new IllegalArgumentException("Must conduct experiment at least one time.");
		}
		int timesSame = 0;
		double percent = 0;
		for(int i = 1; i <= nExperiments; i++){
			if(runSingleExperiment(populationSize)){
				timesSame +=1;
			}
			double timesSameFraction = timesSame;
			double nExperimentsFraction = nExperiments;
			percent = timesSameFraction / nExperimentsFraction;
		}
		return percent;
	}
}