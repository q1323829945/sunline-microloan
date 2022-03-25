package cn.sunline.saas.huaweicloud.services

import cn.sunline.saas.huaweicloud.exception.ObsBodyTypeException
import cn.sunline.saas.huaweicloud.exception.ObsUploadException
import cn.sunline.saas.obs.api.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HuaweiCloudServiceTest {
    @Autowired
    private lateinit var huaweiCloudService: ObsApi


    @Test
    fun `create bucket`(){
        val bucket = BucketParams("lizheng-test10", CreateBucketConfiguration("cn-east-3"))
        huaweiCloudService.createBucket(bucket)
    }

    /*
    @Test
    fun `delete bucket`(){
        huaweiCloudService.deleteBucket("lizheng-test13")
    }


    @Test
    fun `object path upload`(){
        val put = PutParams("lizheng-test","mimimi3.JPG","D:\\123.JPG")
        huaweiCloudService.putObject(put)
    }

    @Test
    fun `object stream upload`(){
        val file =FileInputStream(File("D:\\123.JPG"))

        val put = PutParams("lizheng-test","stream1234.JPG",file)
        huaweiCloudService.putObject(put)
    }
    */

    @Test
    fun `object upload body is error`(){
        val put = PutParams("my123.JPG",123)

        assertThrows<ObsBodyTypeException> {
            huaweiCloudService.putObject(put)
        }
    }

    @Test
    fun `get object`(){
        val get = GetParams("ok.pdf")

        val arrayInputStream = huaweiCloudService.getObject(get) as InputStream

        val outputStream = FileOutputStream(File("qqq.pdf"))

        val bytes = ByteArray(1024)
        var len = 0
        while (true){
            len = arrayInputStream.read(bytes)
            if(len == -1){
                break
            }

            outputStream.write(bytes,0,len)
        }

    }

    @Test
    fun `delete object`(){
        val del = DeleteParams("lizheng-test","mimimi2.JPG")
        huaweiCloudService.deleteObject(del)
    }
}