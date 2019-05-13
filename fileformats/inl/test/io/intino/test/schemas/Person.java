package io.intino.test.schemas;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Person {
	public String name;
	public double money;
	public Instant birthDate;
	public Country country;
	public List<Phone> phones;

	public Person() {
	}

	public Person(String name, double money, Instant birthDate, Country country) {
		this.name = name;
		this.money = money;
		this.birthDate = birthDate;
		this.country = country;
	}

	public void add(Phone phone) {
		if (phones == null) phones = new ArrayList<>();
		phones.add(phone);
	}
}
