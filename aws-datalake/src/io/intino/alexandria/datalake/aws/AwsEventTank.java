package io.intino.alexandria.datalake.aws;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EventStore.Tank;
import io.intino.alexandria.datalake.Datalake.EventStore.Tub;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.AwsDatalake.AwsDelimiter;
import static io.intino.alexandria.zim.ZimReader.ZimExtension;

public class AwsEventTank implements Tank {
    private static final String EventExtension = ZimExtension;
    public final S3 s3;
    public final String bucketName;
    public final String prefix;

    public AwsEventTank(S3 s3, String bucketName, String prefix) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public String name() {
        String[] route = prefix.split(AwsDelimiter);
        return route[route.length - 1].replace(EventExtension, "");
    }

    @Override
    public Scale scale() {
        return first().timetag().scale();
    }

    @Override
    public Stream<Tub> tubs() {
        return s3.keysIn(bucketName, prefix)
                .map(prefix -> new AwsEventTub(s3.getObjectFrom(bucketName, prefix)));
    }

    @Override
    public Tub first() {
        return tubs().findFirst().orElse(currentTub());
    }

    @Override
    public Tub last() {
        List<String> keys = keysInTank();
        return keys.isEmpty() ? null : new AwsEventTub(s3.getObjectFrom(bucketName, keys.get(keys.size() - 1)));
    }

    @Override
    public Tub on(Timetag tag) {
        return new AwsEventTub(getObject(tag));
    }

    private AwsEventTub currentTub() {
        return new AwsEventTub(new S3Object());
    }

    private S3Object getObject(Timetag tag) {
        return s3.keysIn(bucketName, prefix)
                .filter(t -> t.contains(tag.value()))
                .findFirst()
                .map(key -> s3.getObjectFrom(bucketName, key))
                .orElse(new S3Object());
    }

    private List<String> keysInTank() {
        return s3.keysIn(bucketName, prefix).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(AwsEventTank.class)) return false;
        AwsEventTank tank = (AwsEventTank) obj;
        return this.bucketName.equals(tank.bucketName) && this.prefix.equals(tank.prefix);
    }
}
