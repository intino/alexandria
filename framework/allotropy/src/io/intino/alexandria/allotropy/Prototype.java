package io.intino.alexandria.allotropy;

import io.intino.alexandria.allotropy.prototype.Seed;
import io.intino.alexandria.allotropy.prototype.Sprout;

import java.util.List;

public interface Prototype {
	Seed seed();
	List<Sprout> sprouts();
}
