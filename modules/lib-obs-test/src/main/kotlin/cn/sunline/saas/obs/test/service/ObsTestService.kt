package cn.sunline.saas.obs.test.service

import cn.sunline.saas.obs.api.*
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream

/**
 * @title: ObsTestService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/12 13:37
 */
@Service
class ObsTestService: ObsApi {
    override fun createBucket(bucketParams: BucketParams) {
    }

    override fun putBucketLifecycleConfiguration(lifecycleParams: LifecycleParams) {
    }

    override fun deleteBucket(bucketName: String) {
    }

    override fun putObject(putParams: PutParams) {

    }

    override fun getObject(getParams: GetParams): Any? {

        return FileInputStream(getParams.key)
    }

    override fun deleteObject(deleteParams: DeleteParams) {
    }
}