package io.intino.test;

import io.intino.alexandria.datahub.datalake.DatalakeCloner;
import io.intino.alexandria.datahub.model.Configuration;
import org.junit.Test;

import java.util.Collections;

import static java.util.Collections.singletonList;

public class DatalakeCloner_ {

	@Test
	public void clone_test() {
		Configuration configuration = new Configuration("temp", null,
				new Configuration.Mirror("90.163.132.69:2023", "/home/qia/com.adquiver.qia_ness", "temp/mirror", "2019060101", "root", "", null), new Configuration.Tanks(Collections.emptyList(), singletonList(new Configuration.Tanks.Tank("qia.StandardFeedRecord", Configuration.Tanks.Tank.Type.Event, null))));
		DatalakeCloner cloner = new DatalakeCloner((Configuration.Mirror) configuration.dataSource(), configuration.tanks());
		cloner.execute();
	}
}
