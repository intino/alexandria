package io.intino.alexandria.ui.documentation;

public class Person {
	private String firstName;
	private String lastName;

	public String firstName() {
		return firstName;
	}

	public Person firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String lastName() {
		return lastName;
	}

	public Person lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
}
