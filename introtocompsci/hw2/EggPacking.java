public class EggPacking
{
	public static void main(String[] args)
	{
		int eggs = Integer.parseInt(args[0]);
		int eggcartons = eggs / 12;
		int eggleftovers = (eggs - (12 * eggcartons));
		double packingcost = .31 * eggcartons;
		System.out.println(eggs + " eggs were packed into " + eggcartons + " cartons");
		System.out.println("There were " + eggleftovers + " leftover eggs");
		System.out.println("The total amount spent on egg cartons: $" + packingcost);
	}
}		