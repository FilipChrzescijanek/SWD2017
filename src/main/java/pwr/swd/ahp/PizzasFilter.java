package pwr.swd.ahp;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PizzasFilter {

	private final double maxPrice;
	private final int maxDeliveryTime;
	private final List<String> wantedToppings;
	private final List<String> unwantedToppings;

	public PizzasFilter(final double maxPrice, final int maxDeliveryTime,
	                    final List<String> wantedToppings, final List<String> unwantedToppings) {
		this.maxPrice = maxPrice;
		this.maxDeliveryTime = maxDeliveryTime;
		this.wantedToppings = wantedToppings;
		this.unwantedToppings = unwantedToppings;
	}

	public List<Pizza> filter(final List<Pizza> pizzas) {
		return pizzas
				.stream()
				.filter(this::checkPrice)
				.filter(this::checkDeliveryTime)
				.filter(this::checkWantedToppings)
				.filter(this::checkUnwantedToppings)
				.limit(39)
				.collect(Collectors.toList());
	}

	private boolean checkPrice(final Pizza pizza) {
		return Double.compare(pizza.getPrice(), maxPrice) <= 0;
	}

	private boolean checkDeliveryTime(final Pizza pizza) {
		return pizza.getDeliveryTime() <= maxDeliveryTime;
	}

	private boolean checkWantedToppings(final Pizza pizza) {
		return pizza.getToppings().containsAll(wantedToppings);
	}

	private boolean checkUnwantedToppings(final Pizza pizza) {
		return Collections.disjoint(pizza.getToppings(), unwantedToppings);
	}

}
