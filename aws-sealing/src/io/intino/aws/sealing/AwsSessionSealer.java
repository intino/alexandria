package io.intino.aws.sealing;

import io.intino.alexandria.datalake.Datalake.Store.Tank;
import io.intino.alexandria.datalake.aws.AwsDatalake;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.sealing.SessionSealer;

import java.io.File;
import java.util.function.Predicate;

public class AwsSessionSealer implements SessionSealer {

    private final AwsDatalake datalake;
    private final File stageDir;
    private final File tempDir;
    private final File treatedDir;

    public AwsSessionSealer(AwsDatalake datalake, File stageDir, File treatedDir) {
        this(datalake, stageDir, treatedDir, tempDir(stageDir));
    }

    public AwsSessionSealer(AwsDatalake datalake, File stageDir, File treatedDir, File tempDir) {
        this.datalake = datalake;
        this.stageDir = stageDir;
        this.treatedDir = treatedDir;
        this.tempDir = tempDir;
    }

    private static File tempDir(File stageDir) {
        File temp = new File(stageDir, "temp");
        temp.mkdir();
        return temp;
    }

    @Override
    public void seal(Predicate<Tank<? extends Event>> sortingPolicy) {
        try {
            sealEvents(sortingPolicy);
        } catch (Throwable e) {
            Logger.error(e);
        }
    }

    private void sealEvents(Predicate<Tank<? extends Event>> sortingPolicy) {
        new AwsEventSessionSealer(datalake, stageDir, treatedDir, tempDir).seal(t -> check(t, sortingPolicy));
        tempDir.delete();
    }

    private boolean check(String tank, Predicate<Tank<? extends Event>> sortingPolicy) {
        return sortingPolicy.test(datalake.messageStore().tank(tank))
                || sortingPolicy.test(datalake.measurementStore().tank(tank));
    }
}
