public class testfile
{ 
    public static void main(String[] args)
    {
        int[] operands = {4, 7, 4, 9, 1};
        int result = operands[1] + operands[2];
    	for(int i = 3; i <= operands[0]; i++){
    		result += (operands[i]);
    	}
        System.out.print(operands[1] + "+" + operands [2]);
        for(int i = 3; i <= operands[0]; i++){
            System.out.print("+" + operands[i]);
        }
        System.out.print("=" + result);
    }
}