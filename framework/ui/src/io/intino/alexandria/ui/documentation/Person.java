package io.intino.alexandria.ui.documentation;

public class Person {
	private String firstName;
	private String lastName;
	private Gender gender;
	private int age;

	public enum Gender { Male, Female;}
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

	public Gender gender() {
		return gender;
	}

	public Person gender(Gender gender) {
		this.gender = gender;
		return this;
	}

	public int age() {
		return age;
	}

	public Person age(int age) {
		this.age = age;
		return this;
	}
}
