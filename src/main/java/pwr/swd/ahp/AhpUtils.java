package pwr.swd.ahp;

import java.util.EnumMap;
import java.util.List;

import static pwr.swd.ahp.PreferencePair.*;

public class AhpUtils {

	public static final double[] RI_VALUES = new double[] {
			0.5247, 0.8816, 1.1086, 1.2479, 1.3417, 1.4057, 1.4499,                             // n = 3..9
			1.4854, 1.5140, 1.5365, 1.5551, 1.5713, 1.5838, 1.5978, 1.6086, 1.6181, 1.6265,     // n = 10..19
			1.6341, 1.6409, 1.6470, 1.6526, 1.6577, 1.6624, 1.6667, 1.6706, 1.6743, 1.6777,     // n = 20..29
			1.6809, 1.6839, 1.6867, 1.6893, 1.6917, 1.6940, 1.6962, 1.6982, 1.7002, 1.7020      // n = 30..39
	};

	public static double[][] getPreferenceMatrix(final EnumMap<PreferencePair, Double> preferences) {
		final double priceToTime = preferences.get(PRICE_TO_TIME);
		final double priceToSize = preferences.get(PRICE_TO_SIZE);
		final double priceToToppings = preferences.get(PRICE_TO_TOPPINGS);
		final double timeToSize = preferences.get(TIME_TO_SIZE);
		final double timeToToppings = preferences.get(TIME_TO_TOPPINGS);
		final double sizeToToppings = preferences.get(SIZE_TO_TOPPINGS);
		return new double[][] {
			new double[] {1.0,                      priceToTime,            priceToSize,            priceToToppings },
			new double[] {1.0 / priceToTime,        1.0,                    timeToSize,             timeToToppings  },
			new double[] {1.0 / priceToSize,        1.0 / timeToSize,       1.0,                    sizeToToppings  },
			new double[] {1.0 / priceToToppings,    1.0 / timeToToppings,   1.0 / sizeToToppings,   1.0             }
		};
	}

	public static double[][] getPriceMatrix(final List<Pizza> pizzas) {
		final int constantStep = 3;
		final int n = pizzas.size();
		final double[][] matrix =  new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				if (i == j) matrix[i][j] = 1.0;
				else {
					final double firstPrice = pizzas.get(i).getPrice();
					final double secondPrice = pizzas.get(j).getPrice();
					final double difference = Math.min(Math.max(1.0, Math.round(Math.abs(firstPrice - secondPrice) / constantStep) + 1.0), 9.0);
					final double weight = secondPrice > firstPrice ? difference : 1.0 / difference;
					matrix[i][j] = weight;
					matrix[j][i] = 1.0 / weight;
				}
			}
		}
		return matrix;
	}

	public static double[][] getTimeMatrix(final List<Pizza> pizzas) {
		final int constantStep = 10;
		final int n = pizzas.size();
		final double[][] matrix =  new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				if (i == j) matrix[i][j] = 1.0;
				else {
					final int firstTime = pizzas.get(i).getDeliveryTime();
					final int secondTime = pizzas.get(j).getDeliveryTime();
					final double difference = Math.min(Math.max(1, Math.abs(firstTime - secondTime) / constantStep + 1), 9);
					final double weight = secondTime > firstTime ? difference : 1.0 / difference;
					matrix[i][j] = weight;
					matrix[j][i] = 1.0 / weight;
				}
			}
		}
		return matrix;
	}

	public static double[][] getSizeMatrix(final List<Pizza> pizzas) {
		final double constantStep = 0.0175;
		final int n = pizzas.size();
		final double[][] matrix =  new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				if (i == j) matrix[i][j] = 1.0;
				else {
					final double firstSize = pizzas.get(i).getSize();
					final double secondSize = pizzas.get(j).getSize();
					final double difference = Math.min(Math.max(1.0, Math.round(Math.abs(firstSize - secondSize) / constantStep) + 1.0), 9.0);
					final double weight = firstSize > secondSize ? difference : 1.0 / difference;
					matrix[i][j] = weight;
					matrix[j][i] = 1.0 / weight;
				}
			}
		}
		return matrix;
	}

	public static double[][] getToppingsMatrix(final List<Pizza> pizzas) {
		final int constantStep = 1;
		final int n = pizzas.size();
		final double[][] matrix =  new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				if (i == j) matrix[i][j] = 1.0;
				else {
					final int firstToppings = pizzas.get(i).getToppings().size();
					final int secondToppings = pizzas.get(j).getToppings().size();
					final double difference = Math.min(Math.max(1, Math.abs(firstToppings - secondToppings) / constantStep + 1), 9);
					final double weight = firstToppings > secondToppings ? difference : 1.0 / difference;
					matrix[i][j] = weight;
					matrix[j][i] = 1.0 / weight;
				}
			}
		}
		return matrix;
	}

	public static double[] getPriorityVector(final double[][] matrix) {
		final int n = matrix.length;

		final double[] c = new double[n];
		for (int i = 0; i < n; i++) {
			c[i] = columnSum(matrix, i);
		}

		final double[][] normalizedMatrix = normalize(matrix, c);

		double[] s = new double[n];
		for (int i = 0; i < n; i++) {
			s[i] = rowSum(normalizedMatrix, i) / ((double) n);
		}

		final double consistency = consistency(c, s);

		if (consistency > 0.1) {
			s = new double[n];
			for (int i = 0; i < n; i++) {
				s[i] = priority(matrix, i);
			}
		}

		return s;
	}

	private static double rowSum(final double[][] weights, final int row) {
		double sum = 0.0;
		for (int j = 0; j < weights[0].length; j++) {
			sum += weights[row][j];
		}
		return sum;
	}

	private static double columnSum(final double[][] weights, final int column) {
		double sum = 0.0;
		for (int i = 0; i < weights.length; i++) {
			sum += weights[i][column];
		}
		return sum;
	}

	private static double[][] normalize(final double[][] weights, final double[] c) {
		final double[][] newWeights = new double[weights.length][weights[0].length];
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				newWeights[i][j] = weights[i][j] / c[j];
			}
		}
		return newWeights;
	}

	/*
	 *  In accordance with:
	 *  Saaty, Thomas L., and Luis G. Vargas. 
	 *  "Comparison of eigenvalue, logarithmic least squares and least squares methods in estimating ratios." 
	 *  Mathematical modelling 5.5 (1984): 309-324.
	 *  http://www.sciencedirect.com/science/article/pii/0270025584900083
	 */
	private static double priority(final double[][] weights, final int index) {
		double sum = 0.0;
		for (int i = 0; i < weights.length; i++) {
			sum += rowProduct(weights, i);
		}
		return rowProduct(weights, index) / sum;
	}

	private static double rowProduct(final double[][] weights, final int row) {
		double product = 1.0;
		final int n = weights[0].length;
		for (int j = 0; j < n; j++) {
			product *= weights[row][j];
		}
		return Math.pow(product, 1.0 / ((double) n));
	}

	private static double consistency(final double[] c, final double[] s) {
		double lambda = 0.0;
		final int n = c.length;
		if (n < 3) return 0.0;
		for (int i = 0; i < n; i++) {
			lambda += c[i] * s[i];
		}
		final double ci = (lambda - n) / ((double) (n - 1));
		return ci / RI_VALUES[n - 3];
	}

}
