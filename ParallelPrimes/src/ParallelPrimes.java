/**
 * This class computes the number of prime divisors of an array of given integers, i.e.
 * for each integer in the array. 
 * The computation is done in parallel with a number of threads specified by the user.
 * @author Michele Meziu
 *
 */
public class ParallelPrimes {
	
	private int numberOfThreads;
	private int[] inputNumbers;
	private ArraySplit[] inputPartition;
	private int[] entrywiseNumberOfPrimeDivisors;
	private PrimeDivisorsCounter[] counterThreads; 
	
	
	class PrimeDivisorsCounter extends Thread {
		
		private ArraySplit inputChunk;
		
		public PrimeDivisorsCounter(ArraySplit inputChunk) {
			this.inputChunk = inputChunk;
		}
		
		public void run() {
			int start = inputChunk.startIndex;
			int end = start + inputChunk.length;
			for (int i = start; i < end; i++) {
				int inputEntry = inputNumbers[i];
				entrywiseNumberOfPrimeDivisors[i] = computeNumberOfPrimeDivisors(inputEntry);
			}
		}
	};
	
	/**
	 * The number of prime divisors is computed with multiplicity: if prime
	 * p has power 2 in the factorization of n, then p is counted twice.
	 * 
	 * @return An array of the same length as {@code inputNumbers} where every entry is
	 * the number of prime numbers that divides the corresponding entry in {@code inputNumbers}.
	 */
	public int[] computeEntrywiseNumberOfPrimeDivisors() {
		divideInput();
		initializeThreads();
		for (int i = 0; i < numberOfThreads; i++) {
			counterThreads[i].start();
		}
		for (int i = 0; i < numberOfThreads; i++) {
			try {
				counterThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return entrywiseNumberOfPrimeDivisors;
	}
	
	private void initializeThreads() {
		counterThreads = new PrimeDivisorsCounter[numberOfThreads];
		for (int i = 0; i < numberOfThreads; i++) {
			ArraySplit myChunk = inputPartition[i];
			counterThreads[i] = new PrimeDivisorsCounter(myChunk);
		}
	}
	
	private void divideInput() {
		int length = inputNumbers.length;
		inputPartition = new ArraySplit[numberOfThreads];
		int ratio = (int) Math.round((double) length / numberOfThreads);
		for (int i = 0; i < numberOfThreads - 1; i++) {
			inputPartition[i] = new ArraySplit(i * ratio, ratio);
		}
		inputPartition[numberOfThreads - 1] = new ArraySplit((numberOfThreads - 1) * ratio,
				length - (numberOfThreads - 1) * ratio);
	}
	
	private int computeNumberOfPrimeDivisors(int number) {
		int primeFactors = 0;
		int n = turnToPositive(number);		
		for (int i = 2; i <= n; i++) {
			while (n % i == 0) {
				primeFactors++;
				n /= i;
			}
		}
		return primeFactors;
	}
	
	private int turnToPositive(int number) {
		if(number < 0) {
			number = -number;
		}
		return number;
	}
	
	ParallelPrimes(int[] inputNumbers){
		this.inputNumbers = inputNumbers;
		entrywiseNumberOfPrimeDivisors = new int[inputNumbers.length];
	}

	public void setInputNumbers(int[] inputNumbers) {
		this.inputNumbers = inputNumbers;
		entrywiseNumberOfPrimeDivisors = new int[inputNumbers.length];
	}

	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}
}
