package io.intino.test;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PremiumEmployee {
	private String code;
	private String name;
	private int salary;

	public String code() {
		return code;
	}

	public void code(String code) {
		this.code = code;
	}

	public String name() {
		return name;
	}

	public void name(String name) {
		this.name = name;
	}

	public int salary() {
		return salary;
	}

	public void salary(int population) {
		this.salary = population;
	}
}
