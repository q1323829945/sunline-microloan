package cn.sunline.saas.minio.config

import cn.sunline.saas.minio.model.EndpointType
import cn.sunline.saas.minio.model.EndpointType.*
import io.minio.MinioClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "minio")
class MinioConfig(
    var endpoint:String = "192.168.2.10",
    var port:Int = 30109,
    var accessKey:String = "sunline",
    var secretKey:String = "sunline300348",
    var endpointType: EndpointType = IP,
    var bucket:String = "sunline"
) {
    @Bean
    fun initMinoClient():MinioClient{
        return when(endpointType){
            URL -> MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build()
            IP -> MinioClient.builder().endpoint(endpoint,port,false).credentials(accessKey, secretKey).build()
        }
    }
}