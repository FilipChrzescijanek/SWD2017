package pwr.swd.ahp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static pwr.swd.ahp.PreferencePair.*;

public class AhpAlgorithmTest {
	
	private EnumMap<PreferencePair, Double> preferences;
	private List<Pizza> pizzas;
	private PizzasFilter filter;

	@Before
	public void setUp() throws Exception {
		preferences = new EnumMap<>(PreferencePair.class);
		pizzas = new ObjectMapper().readValue(getClass().getResource("/data.json"), new TypeReference<List<Pizza>>() {});
	}

	@After
	public void tearDown() throws Exception {
		preferences = null;
		pizzas = null;
		filter = null;
	}

	@Test
	public void jsonReaderTest() throws Exception {
		final List<Pizza> expectedPizzas = new ArrayList<>();
		expectedPizzas.add(new Pizza("Texas", Arrays.asList("cheese", "salami"), 15.00, 30, "Da Grasso", 15));
		expectedPizzas.add(new Pizza("Texas", Arrays.asList("cheese", "salami"), 20.00, 40, "Da Grasso", 15));
		expectedPizzas.add(new Pizza("California", Arrays.asList("cheese", "ham"), 18.00, 30, "Da Grasso", 15));
		expectedPizzas.add(new Pizza("Hawaii", Arrays.asList("cheese", "salami", "pineapple"), 25.00, 40, "Da Grasso", 15));
		expectedPizzas.add(new Pizza("California", Arrays.asList("cheese", "ham"), 17.00, 40, "Pizza Hut", 30));
		expectedPizzas.add(new Pizza("Hawaii", Arrays.asList("cheese", "salami", "pineapple"), 24.00, 40, "Pizza Hut", 30));
		assertEquals(expectedPizzas, pizzas);
	}

	@Test
	public void rank1() throws Exception {
		preferences.put(PRICE_TO_TIME, 0.2);
		preferences.put(PRICE_TO_SIZE, 0.5);
		preferences.put(PRICE_TO_TOPPINGS, 0.33);
		preferences.put(TIME_TO_SIZE, 4.0);
		preferences.put(TIME_TO_TOPPINGS, 3.0);
		preferences.put(SIZE_TO_TOPPINGS, 0.5);

		filter = new PizzasFilter(20.00, 30, Arrays.asList("cheese"), Arrays.asList("pineapple"));

		final List<Pizza> ranked = new AhpAlgorithm(preferences).rank(pizzas, filter);
//		ranked.forEach(System.out::println);
		assertEquals(Arrays.asList(pizzas.get(1), pizzas.get(0), pizzas.get(2), pizzas.get(4)), ranked);
	}

	@Test
	public void rank2() throws Exception {
		preferences.put(PRICE_TO_TIME, 1.0);
		preferences.put(PRICE_TO_SIZE, 2.0);
		preferences.put(PRICE_TO_TOPPINGS, 2.0);
		preferences.put(TIME_TO_SIZE, 2.0);
		preferences.put(TIME_TO_TOPPINGS, 2.0);
		preferences.put(SIZE_TO_TOPPINGS, 1.0);

		filter = new PizzasFilter(25.00, 30, Arrays.asList("cheese"), Collections.emptyList());

		final List<Pizza> ranked = new AhpAlgorithm(preferences).rank(pizzas, filter);
//		ranked.forEach(System.out::println);
		assertEquals(Arrays.asList(pizzas.get(0), pizzas.get(1), pizzas.get(3), pizzas.get(2), pizzas.get(4), pizzas.get(5)), ranked);
	}

}