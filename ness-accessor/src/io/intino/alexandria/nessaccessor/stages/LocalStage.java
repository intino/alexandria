package io.intino.alexandria.nessaccessor.stages;

import io.intino.ness.core.fs.FSStage;

import java.io.File;

public class LocalStage extends FSStage {
	public LocalStage(File root) {
		super(root);
	}
}