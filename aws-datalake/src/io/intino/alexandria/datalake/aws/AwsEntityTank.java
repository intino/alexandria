package io.intino.alexandria.datalake.aws;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EntityStore.Tank;
import io.intino.alexandria.datalake.Datalake.EntityStore.Tub;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.datalake.aws.AwsDatalake.AwsDelimiter;

public class AwsEntityTank implements Tank{
    private final S3 s3;
    private final String bucketName;
    private final String prefix;

    public AwsEntityTank(S3 s3, String bucketName, String prefix) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.prefix = prefix;
    }

    @Override
    public String name() {
        String[] rootArray = prefix.split(AwsDelimiter);
        return rootArray[rootArray.length - 1];
    }

    @Override
    public Stream<Tub> tubs() {
        return s3.keysIn(bucketName, prefix)
                .map(prefix -> new AwsEntityTub(s3.getObjectFrom(bucketName, prefix)));
    }

    @Override
    public Tub first() {
        return tubs().findFirst().orElse(null);
    }

    @Override
    public Tub last() {
        return tubs().reduce((first, second) -> second).orElse(null);
    }

    @Override
    public Tub on(Timetag tag) {
        return new AwsEntityTub(s3.getObjectFrom(bucketName, tubNameOf(tag)));
    }

    @Override
    public Stream<Tub> tubs(int count) {
        return tubs().limit(count);
    }

    @Override
    public Stream<Tub> tubs(Timetag from, Timetag to) {
        return StreamSupport.stream(from.iterateTo(to).spliterator(), true).map(this::on);
    }

    private String tubNameOf(Timetag tag) {
        return prefix + tag.value() + AwsEntityStore.Extension;
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(AwsEntityTank.class)) return false;
        AwsEntityTank tank = (AwsEntityTank) obj;
        return this.bucketName.equals(tank.bucketName) &&
                this.prefix.equals(tank.prefix);
    }
}
