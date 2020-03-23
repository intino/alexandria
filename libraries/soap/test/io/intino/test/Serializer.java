package io.intino.test;

import io.intino.alexandria.Envelope;
import io.intino.alexandria.Soap;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.annotation.XmlRootElement;

public class Serializer {
	@Test
	public void serialize() {
		Soap soap = new Soap();
		Employee employee = new Employee();
		employee.setCode("aaa");
		employee.setName("bbb");
		employee.setSalary(1000);
		String envelope = soap.writeEnvelope(employee);
		Envelope object = soap.readEnvelope(envelope);
		Employee schema = object.body().schema(Employee.class);
		Assert.assertEquals(schema.code, "aaa");
		Assert.assertEquals(schema.name, "bbb");
		Assert.assertEquals(schema.salary, 1000);
	}

	@Test
	public void deserialize() {

	}

	@XmlRootElement
	static class Employee {
		private String code;
		private String name;
		private int salary;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getSalary() {
			return salary;
		}

		public void setSalary(int population) {
			this.salary = population;
		}
	}
}
