package pwr.swd.ahp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Pizza {

	@NotNull private String name;
	@NotNull private List<String> toppings;
	private double price;
	private int diameter;
	@NotNull private String pizzeriaName;
	private int deliveryTime;

	@JsonIgnore private double size;

	public Pizza() {}

	Pizza(final String name, final List<String> toppings, final double price,
	      final int diameter, final String pizzeriaName, final int deliveryTime) {
		this.name = name;
		this.toppings = toppings;
		this.price = price;
		this.diameter = diameter;
		this.pizzeriaName = pizzeriaName;
		this.deliveryTime = deliveryTime;
		this.size = Math.PI * Math.pow((diameter / 200.0), 2);
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<String> getToppings() {
		return toppings;
	}

	public void setToppings(final List<String> toppings) {
		this.toppings = toppings;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(final double price) {
		this.price = price;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(final int diameter) {
		this.diameter = diameter;
		this.size = Math.PI * Math.pow((diameter / 200.0), 2);
	}

	public String getPizzeriaName() {
		return pizzeriaName;
	}

	public void setPizzeriaName(final String pizzeriaName) {
		this.pizzeriaName = pizzeriaName;
	}

	public int getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(final int deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public double getSize() {
		return size;
	}

	@Override
	public String toString() {
		return String.format("%s, price: %.2f, diameter: %dcm, toppings: %s, pizzeria: %s, delivery in: %d",
		                     name, price, diameter, toppings, pizzeriaName, deliveryTime);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof Pizza)) return false;

		final Pizza pizza = (Pizza) o;

		if (Double.compare(pizza.getPrice(), getPrice()) != 0) return false;
		if (getDiameter() != pizza.getDiameter()) return false;
		if (getDeliveryTime() != pizza.getDeliveryTime()) return false;
		if (Double.compare(pizza.getSize(), getSize()) != 0) return false;
		if (!getName().equals(pizza.getName())) return false;
		if (!getToppings().equals(pizza.getToppings())) return false;
		return getPizzeriaName().equals(pizza.getPizzeriaName());
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = getName().hashCode();
		result = 31 * result + getToppings().hashCode();
		temp = Double.doubleToLongBits(getPrice());
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + getDiameter();
		result = 31 * result + getPizzeriaName().hashCode();
		result = 31 * result + getDeliveryTime();
		temp = Double.doubleToLongBits(getSize());
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

}
