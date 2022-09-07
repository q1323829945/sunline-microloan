package cn.sunline.saas.obs.huaweicloud.services

import cn.sunline.saas.obs.huaweicloud.exception.ObsBodyTypeException
import cn.sunline.saas.obs.api.DeleteParams
import cn.sunline.saas.obs.api.GetParams
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HuaweiCloudObsServiceTest {
    @Autowired
    private lateinit var huaweiCloudObsService: ObsApi

    /*
    @Test
    fun `create bucket`(){
        val bucket = BucketParams("lizheng-test11", CreateBucketConfiguration("cn-east-3"))
        huaweiCloudObsService.createBucket(bucket)
    }


    // 这个案例需要有一个空桶才能使用，测试案例是并行的，
    @Test
    fun `delete bucket`(){
        huaweiCloudObsService.deleteBucket("lizheng-test11")
    }
    */

    @Test
    @Disabled
    fun `object path upload`(){
        val put = PutParams("mimimi3.JPG","src\\test\\resources\\file\\123.JPG")
        huaweiCloudObsService.putObject(put)
    }

    @Test
    @Disabled
    fun `object stream upload`(){
        val file = FileInputStream(File("src\\test\\resources\\file\\123.JPG"))

        val put = PutParams("stream1234.JPG",file)
        huaweiCloudObsService.putObject(put)

    }


    @Test
    @Disabled
    fun `object upload body is error`(){
        val put = PutParams("my123.JPG",123)

        assertThrows<ObsBodyTypeException> {
            huaweiCloudObsService.putObject(put)
        }
    }

    @Test
    @Disabled
    fun `get object`(){
        val get = GetParams("stream1234.JPG")

        val arrayInputStream = huaweiCloudObsService.getObject(get) as InputStream

        val outputStream = FileOutputStream(File("src\\test\\resources\\file\\qqq.JPG"))

        val bytes = ByteArray(1024)
        while (true){
            val len = arrayInputStream.read(bytes)
            if(len == -1){
                break
            }

            outputStream.write(bytes,0,len)
        }

        outputStream.close()
        arrayInputStream.close()




    }

    @Test
//    @Disabled
    fun `delete object`(){
        val del = DeleteParams("mimimi3.JPG")
        huaweiCloudObsService.deleteObject(del)
    }


    @Test
    @Disabled
    fun `get picture view`(){
        val params = GetParams("48895297438892032/48950681445089280/33727192146395136/db85d8ac-0789-11eb-bda8-0242c0a84002.jpg")
        val url = huaweiCloudObsService.getPictureView(params)

        Thread.sleep(5000)
        val url2 = huaweiCloudObsService.getPictureView(params)
        Assertions.assertThat(url).isEqualTo(url2)
    }
}