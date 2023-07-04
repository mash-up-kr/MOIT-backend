package com.mashup.moit.infra.aws.s3

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.mashup.moit.infra.aws.s3.S3Config.S3Properties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.aws.autoconfigure.context.properties.AwsCredentialsProperties
import org.springframework.cloud.aws.autoconfigure.context.properties.AwsRegionProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(S3Properties::class)
class S3Config {

    @Bean
    fun amazonS3(awsCredentialsProperties: AwsCredentialsProperties, awsRegionProperties: AwsRegionProperties, s3Properties: S3Properties): AmazonS3 {
        val awsCredentials = BasicAWSCredentials(awsCredentialsProperties.accessKey, awsCredentialsProperties.secretKey)
        return AmazonS3ClientBuilder.standard()
            .withRegion(awsRegionProperties.static)
            .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
            .build()
    }

    @ConfigurationProperties(prefix = "cloud.aws.s3")
    data class S3Properties(val domain: String, val bucketName: String)
}
