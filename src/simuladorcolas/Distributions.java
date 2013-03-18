package simuladorcolas;

import java.util.Random;

@SuppressWarnings("serial")
public class Distributions extends Random {

	public int nextPoisson(double lambda) {
		double elambda = Math.exp(-1 * lambda);
		double product = 1;
		int count = 0;
		double result = 0;
		while (product >= elambda) {
			product *= nextDouble();
			result = count;
			count++; // keep result one behind
		}
		return (int) Math.round(result);
	}

	public double nextExponential(double b) {
		double randx;
		double result;
		randx = nextDouble();
		result = -1 * b * Math.log(randx);
		return result;
	}
}
