package cn.sunline.saas.minio.service

import io.minio.*
import okhttp3.OkHttpClient
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MinioServiceTest {

    @Autowired
    private lateinit var minioTemplate: MinioTemplate

    @Test
    @Disabled
    fun `put object`(){
        minioTemplate.createBucket("test")
        val file = FileInputStream(File("src\\test\\resources\\file\\123.JPG"))
        minioTemplate.putObject("test.jpg",file)

        val exists = minioTemplate.checkFileExists("test.jpg")
        Assertions.assertThat(exists).isEqualTo(true)
    }

    @Test
    @Disabled
    fun `get object`(){
        minioTemplate.createBucket("test")
        val stream = minioTemplate.getObject("test.jpg")
        val file = File("src\\test\\resources\\file\\getFile.JPG")
        stream?.run { FileUtils.copyInputStreamToFile(stream,file) }

    }

    @Test
    @Disabled
    fun `delete object`(){
        minioTemplate.removeObject("test.jpg")

        val exists = minioTemplate.checkFileExists("test.jpg")
        Assertions.assertThat(exists).isEqualTo(false)
    }


}