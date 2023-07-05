package com.mashup.moit.infra.aws.s3

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream
import java.util.UUID

@Service
class S3Service(
    private val amazonS3: AmazonS3,
    private val s3Properties: S3Config.S3Properties
) {

    fun upload(directory: String, imageFile: MultipartFile): String {
        val fileName = String.format("%s%s-%s", directory, UUID.randomUUID(), imageFile.originalFilename)
        val bucket: String = s3Properties.bucketName
        try {
            val objectMetadata = getObjectMetadataFromFile(imageFile)
            val inputStream: InputStream = imageFile.inputStream
            amazonS3.putObject(bucket, fileName, inputStream, objectMetadata)
        } catch (e: IOException) {
            throw IllegalStateException("S3 File I/O Error", e)
        } catch (e: AmazonServiceException) {
            throw IllegalStateException("Failed to upload the file ($fileName)", e)
        }
        return "${s3Properties.domain}/$fileName"
    }

    fun update(directory: String, outdatedFileUri: String, imageFile: MultipartFile): String {
        return upload(directory, imageFile)
    }

    private fun getObjectMetadataFromFile(imageFile: MultipartFile): ObjectMetadata {
        return ObjectMetadata().apply {
            this.contentType = imageFile.contentType
            this.contentLength = imageFile.size
        }
    }

}
