import static org.junit.Assert.*;

import org.junit.Test;

public class ParallelPrimesTest {

	@Test
	public void test() {
		int[] input = Main.generateRandomInput(100);
		ParallelPrimes calculator = new ParallelPrimes(input);
		calculator.setNumberOfThreads(8);
		int[] linearResult, parallelResult;
		parallelResult = calculator.computeEntrywiseNumberOfPrimeDivisors();
		linearResult = Main.computePrimeFactors(input);
		assertArrayEquals(linearResult, parallelResult);
	}
	
	@Test
	public void testParallelConstantArray() {
		int[] input = {1,2,3,4,5,6,7,8};
		ParallelPrimes calculator = new ParallelPrimes(input);
		calculator.setNumberOfThreads(8);
		int[]  parallelResult;
		parallelResult = calculator.computeEntrywiseNumberOfPrimeDivisors();
		int[] expected = {0,1,1,2,1,2,1,3};
		assertArrayEquals(expected, parallelResult);
	}

}
