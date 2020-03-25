package io.intino.test;

import io.intino.alexandria.Envelope;
import io.intino.alexandria.Soap;
import org.junit.Assert;
import org.junit.Test;

public class Serializer {
	@Test
	public void serialize() {
		Soap soap = new Soap();
		PremiumEmployee premiumEmployee = new PremiumEmployee();
		premiumEmployee.code("aaa");
		premiumEmployee.name("bbb");
		premiumEmployee.salary(1000);
		String envelope = soap.writeEnvelope(premiumEmployee, "");
		System.out.println(envelope);
		Envelope object = soap.readEnvelope(envelope);
		PremiumEmployee schema = object.body().schema(PremiumEmployee.class);
		Assert.assertEquals(schema.code(), "aaa");
		Assert.assertEquals(schema.name(), "bbb");
		Assert.assertEquals(schema.salary(), 1000);
	}

}
