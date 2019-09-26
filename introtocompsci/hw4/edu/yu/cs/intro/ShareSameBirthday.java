package edu.yu.cs.intro;
public class ShareSameBirthday
{
	public static void main(String[] args)
	{
		int timessameone = 0;
			{int[] birthdates = new int[10];
			for(int surveynumber = 1; surveynumber <=10000; surveynumber++)
			{
				for(int date = 0; date < birthdates.length; date++){
				birthdates[date] = (int)(Math.random() *365);
				}
				eachsurvey:
					for(int i = 0; i < birthdates.length; i++){
						for(int j = i + 1; j < birthdates.length; j++){
							if (birthdates[i] == birthdates[j]){
								timessameone += 1;
								break eachsurvey;
							}
						}
					}
			}
			}
		int timessametwo = 0;
			{int[] birthdates = new int[23];
			for(int surveynumber = 1; surveynumber <=10000; surveynumber++)
			{
				for(int date = 0; date < birthdates.length; date++){
				birthdates[date] = (int)(Math.random() *365);
				}
				eachsurvey:
					for(int i = 0; i < birthdates.length; i++){
						for(int j = i + 1; j < birthdates.length; j++){
							if (birthdates[i] == birthdates[j]){
								timessametwo += 1;
								break eachsurvey;
							}
						}
					}
			}
			}	
		int timessamethree = 0;
			{int[] birthdates = new int[70];
			for(int surveynumber = 1; surveynumber <=10000; surveynumber++)
			{
				for(int date = 0; date < birthdates.length; date++){
				birthdates[date] = (int)(Math.random() *365);
				}
				eachsurvey:
					for(int i = 0; i < birthdates.length; i++){
						for(int j = i + 1; j < birthdates.length; j++){
							if (birthdates[i] == birthdates[j]){
								timessamethree += 1;
								break eachsurvey;
							}
						}
					}
			}
			}
			double trialone = timessameone / 10;
			double trialtwo = timessametwo / 10;
			double trialthree = timessamethree / 10;
			double percentone = trialone / 1000;
			double percenttwo = trialtwo / 1000;
			double percentthree = trialthree / 1000;
			System.out.println("Number of experminents per population size: 10,000");
			System.out.println("Population size: 10.  Shared Birthday Frequency: " + percentone);
			System.out.println("Population size: 23.  Shared Birthday Frequency: " + percenttwo);
			System.out.println("Population size: 70.  Shared Birthday Frequency: " + percentthree);
	}
}