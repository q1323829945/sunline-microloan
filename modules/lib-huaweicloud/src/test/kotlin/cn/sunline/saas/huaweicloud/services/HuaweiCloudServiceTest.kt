package cn.sunline.saas.huaweicloud.services

import cn.sunline.saas.exceptions.UploadException
import cn.sunline.saas.huaweicloud.config.BUCKET_NAME
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HuaweiCloudServiceTest {
    @Autowired
    private lateinit var huaweiCloudService: ObsApi

    @Test
    fun `object path upload`(){
        val put = PutParams(BUCKET_NAME,"my123.JPG","D:\\123.JPG")
        huaweiCloudService.putObject(put)
    }

    @Test
    fun `object stream upload`(){
        val file =FileInputStream(File("D:\\123.JPG"))

        val put = PutParams(BUCKET_NAME,"stream123.JPG",file)
        huaweiCloudService.putObject(put)
    }

    @Test
    fun `object upload body is error`(){
        val put = PutParams(BUCKET_NAME,"my123.JPG",123)

        assertThrows<UploadException> {
            huaweiCloudService.putObject(put)
        }
    }


}