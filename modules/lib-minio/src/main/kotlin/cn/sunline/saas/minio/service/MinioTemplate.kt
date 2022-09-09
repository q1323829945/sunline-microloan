package cn.sunline.saas.minio.service

import cn.sunline.saas.minio.config.MinioConfig
import io.minio.*
import mu.KotlinLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

@Component
class MinioTemplate(
    val minioConfig: MinioConfig,
) {
    private var logger = KotlinLogging.logger {}


    fun minioClient():MinioClient{
        return minioConfig.initMinoClient()
    }

    fun checkBucketExists(bucketName:String):Boolean{
        return minioClient().bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())
    }

    fun createBucket(bucketName: String){
        if(!checkBucketExists(bucketName)){
            minioClient().makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
        }
    }

    fun putObject(filePath:String,stream:InputStream,contentType:String = MediaType.APPLICATION_OCTET_STREAM_VALUE){
        minioClient().putObject(
            PutObjectArgs.builder()
                .bucket(minioConfig.bucket)
                .`object`(filePath)
                .stream(stream,stream.available().toLong(),-1)
                .contentType(contentType)
                .build()
        )
        stream.close()
    }

    fun putObject(filePath:String,bytes:ByteArray,contentType:String = MediaType.APPLICATION_OCTET_STREAM_VALUE){
        val stream = ByteArrayInputStream(bytes)
        putObject(filePath, stream, contentType)
    }

    fun putObject(filePath:String,file:File,contentType:String = MediaType.APPLICATION_OCTET_STREAM_VALUE){
        val stream = FileInputStream(file)
        putObject(filePath, stream, contentType)
    }

    fun getObject(filePath: String):InputStream?{
        try {
            if(checkBucketExists(minioConfig.bucket)){
                return minioClient()
                    .getObject(
                        GetObjectArgs.builder()
                            .bucket(minioConfig.bucket)
                            .`object`(filePath)
                            .build()
                    )
            }
        } catch (e:Exception){
            logger.error("file:[$filePath] is not in bucket:[${minioConfig.bucket}] / $filePath is not exists" )
            return null
        }
        return null
    }

    fun checkFileExists(filePath: String):Boolean{
        try {
            if(checkBucketExists(minioConfig.bucket)){
                minioClient().statObject(StatObjectArgs.builder().bucket(minioConfig.bucket).`object`(filePath).build())
                return true
            }
        } catch (e:Exception){
            logger.error("file:[$filePath] not in bucket:[${minioConfig.bucket}] / $filePath not exists" )
            return false
        }
        return false
    }

    fun removeObject(filePath: String){
        minioClient().removeObject(RemoveObjectArgs.builder().bucket(minioConfig.bucket).`object`(filePath).build())
    }
}