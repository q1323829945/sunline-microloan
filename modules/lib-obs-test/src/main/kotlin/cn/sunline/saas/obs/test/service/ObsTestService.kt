package cn.sunline.saas.obs.test.service

import cn.sunline.saas.obs.api.*
import org.springframework.stereotype.Service

/**
 * @title: ObsTestService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/12 13:37
 */
@Service
class ObsTestService: ObsApi {
    override fun createBucket(bucketParams: BucketParams) {
        TODO("Not yet implemented")
    }

    override fun putBucketLifecycleConfiguration(lifecycleParams: LifecycleParams) {
        TODO("Not yet implemented")
    }

    override fun deleteBucket(bucketName: String) {
        TODO("Not yet implemented")
    }

    override fun putObject(putParams: PutParams) {
        TODO("Not yet implemented")
    }

    override fun getObject(getParams: GetParams): Any? {
        TODO("Not yet implemented")
    }

    override fun deleteObject(deleteParams: DeleteParams) {
        TODO("Not yet implemented")
    }
}