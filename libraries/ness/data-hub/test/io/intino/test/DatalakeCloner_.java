package io.intino.test;

import io.intino.alexandria.datahub.datalake.DatalakeCloner;
import io.intino.alexandria.datahub.model.Configuration;
import org.apache.log4j.Level;
import org.junit.Test;

import java.util.Collections;

import static io.intino.alexandria.datahub.model.Configuration.Tanks.Tank.Type.Event;
import static io.intino.alexandria.datahub.model.Configuration.Tanks.Tank.Type.Set;
import static java.util.Collections.singletonList;

public class DatalakeCloner_ {
	static {
		io.intino.alexandria.logger4j.Logger.init(Level.WARN);
	}

	@Test
	public void clone_events_test() {
		Configuration configuration = new Configuration("temp", null,
				new Configuration.Mirror("90.163.132.69:2023", "/home/qia/com.adquiver.qia_ness", "temp/mirror", "2019060101", "root", "", null),
				new Configuration.Tanks(Collections.emptyList(), singletonList(new Configuration.Tanks.Tank("qia.StandardFeedRecord", Event, null))));
		DatalakeCloner cloner = new DatalakeCloner((Configuration.Mirror) configuration.dataSource(), configuration.tanks());
		cloner.execute();
	}



	@Test
	public void clone_sets_test() {
		Configuration configuration = new Configuration("temp", null,
				new Configuration.Mirror("90.163.132.69:2024", "/home/moiras/com.adquiver.moiras_ness", "temp/mirror", "2019060101", "root", "", null),
				new Configuration.Tanks(Collections.emptyList(), singletonList(new Configuration.Tanks.Tank("Impressions", Set, null))));
		DatalakeCloner cloner = new DatalakeCloner((Configuration.Mirror) configuration.dataSource(), configuration.tanks());
		cloner.execute();
	}
}
