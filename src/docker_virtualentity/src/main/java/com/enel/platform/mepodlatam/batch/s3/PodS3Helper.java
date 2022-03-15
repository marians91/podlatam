package com.enel.platform.mepodlatam.batch.s3;

import java.util.Optional;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.enel.platform.entity.utils.batch.S3Helper;

public class PodS3Helper extends S3Helper {

	private final AmazonS3 s3Client;
	private final String bucketName;
	private static final String BUCKET_DELIMITER = "/";

	public PodS3Helper(AmazonS3 s3Client, String bucketName, String entityName, String entityVersion) {
		super(s3Client, bucketName, entityName.concat(BUCKET_DELIMITER).concat(entityVersion));
		this.s3Client = s3Client;
		this.bucketName = bucketName;

	}

	public boolean s3KeysExist(String apiName, String apiVersion, Optional<String> appFilter) {
		String prefix = null;
		if (appFilter.isEmpty()) {
			prefix = apiName.concat(BUCKET_DELIMITER).concat(apiVersion).concat(BUCKET_DELIMITER);
		} else {
			prefix = apiName.concat(BUCKET_DELIMITER).concat(apiVersion).concat(BUCKET_DELIMITER)
					.concat(appFilter.get()).concat(BUCKET_DELIMITER);
		}

		ListObjectsV2Request objectsV2Request = getListObjectsV2Request(prefix);
		ListObjectsV2Result result = s3Client.listObjectsV2(objectsV2Request);
		return result.getKeyCount() > 0;

	}

	protected ListObjectsV2Request getListObjectsV2Request(String prefix) {
		return new ListObjectsV2Request().withPrefix(prefix).withDelimiter(BUCKET_DELIMITER).withBucketName(bucketName);
	}
}