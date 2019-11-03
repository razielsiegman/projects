import org.junit.Test;
import static org.junit.Assert.*;

public class ThreeNPlusOneTest
{
	@Test
	public void Testingnormal(){
		String message = "19,58,29,88,44,22,11,34,17,52,26,13,40,20,10,5,16,8,4,2,1";
		assertEquals("computing sequence of the number 19",message,ThreeNPlusOne.generateThreeN(19));
	}
	@Test
	public void Testingboundary(){
		String message = "1,4,2,1";
		assertEquals("computing sequence of the number 1",message,ThreeNPlusOne.generateThreeN(1));
	}
}
