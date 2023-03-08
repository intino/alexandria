package io.intino.alexandria.datalake;

import com.amazonaws.services.s3.model.S3Object;

public interface AwsTub {

    String fileExtension();

    S3Object object();
}
