package io.intino.alexandria.sealing.aws;

import com.amazonaws.services.s3.AmazonS3;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.aws.AwsDatalake;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.sealing.SessionSealer;

import java.io.File;
import java.util.function.Predicate;

public class AwsSessionSealer implements SessionSealer {

    private final AwsDatalake datalake;
    private final String bucketName;
    private final File stageDir;
    private final File tempDir;
    private final AmazonS3 client;
    private final File treatedFile;

    public AwsSessionSealer(AwsDatalake datalake, String bucketName, File stageDir, File tempDir, AmazonS3 client, File treatedFile) {
        this.datalake = datalake;
        this.bucketName = bucketName;
        this.stageDir = stageDir;
        this.tempDir = tempDir;
        this.client = client;
        this.treatedFile = treatedFile;
    }

    @Override
    public void seal(Predicate<Datalake.Store.Tank<? extends Event>> sortingPolicy) {
        try {
            sealEvents(sortingPolicy);
        } catch (Throwable e) {
            Logger.error(e);
        }
    }

    private void sealEvents(Predicate<Datalake.Store.Tank<? extends Event>> sortingPolicy) {
        new AwsEventSessionSealer(datalake, bucketName, stageDir, tempDir, client, treatedFile).seal(t -> check(t, sortingPolicy));
        tempDir.delete();
    }

    private boolean check(String tank, Predicate<Datalake.Store.Tank<? extends Event>> sortingPolicy) {
        return sortingPolicy.test(datalake.messageStore().tank(tank))
                || sortingPolicy.test(datalake.measurementStore().tank(tank));
    }
}
