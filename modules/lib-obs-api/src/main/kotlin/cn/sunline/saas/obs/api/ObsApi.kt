package cn.sunline.saas.obs.api

/**
 * @title: ObsApi
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/21 17:24
 */

data class CreateBucketConfiguration(val locationConstraint: String)
data class BucketParams(val bucketName: String, val createBucketConfiguration: CreateBucketConfiguration)
data class LifecycleConfiguration(val expiration: Int)
data class LifecycleParams(val bucketName: String, val lifecycleConfiguration: LifecycleConfiguration)

data class PutParams(val key: String, val body: Any)
data class GetParams(val key: String)
data class DeleteParams(val key: String)

interface ObsApi {

    fun createBucket(bucketParams: BucketParams)

    fun putBucketLifecycleConfiguration(lifecycleParams: LifecycleParams)

    fun deleteBucket(bucketName: String)

    fun putObject(putParams: PutParams)

    fun getObject(getParams: GetParams): Any?

    fun deleteObject(deleteParams: DeleteParams)

}