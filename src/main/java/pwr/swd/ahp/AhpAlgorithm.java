package pwr.swd.ahp;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import static pwr.swd.ahp.AhpUtils.*;

public class AhpAlgorithm {

	private final double[][] preferenceMatrix;

	public AhpAlgorithm(final EnumMap<PreferencePair, Double> preferences) {
		this.preferenceMatrix = getPreferenceMatrix(preferences);
	}

	public List<Pizza> rank(final List<Pizza> pizzas, final PizzasFilter filter) {
		final List<Pizza> filtered = filter.filter(pizzas);
		if (filtered.size() < 2) return filtered;
		else {
			final double[] s0 = getPriorityVector(preferenceMatrix);
			final double[] s1 = getPriorityVector(getPriceMatrix(filtered));
			final double[] s2 = getPriorityVector(getTimeMatrix(filtered));
			final double[] s3 = getPriorityVector(getSizeMatrix(filtered));
			final double[] s4 = getPriorityVector(getToppingsMatrix(filtered));

			final int n = filtered.size();
			final double[] r = new double[n];
			for (int i = 0; i < n; i++) {
				r[i] = s0[0] * s1[i] + s0[1] * s2[i] + s0[2] * s3[i] + s0[3] * s4[i];
			}
			return filtered
					.stream()
					.sorted((p1, p2) -> Double.compare(r[filtered.indexOf(p2)], r[filtered.indexOf(p1)]))
					.collect(Collectors.toList());
		}
	}

}
