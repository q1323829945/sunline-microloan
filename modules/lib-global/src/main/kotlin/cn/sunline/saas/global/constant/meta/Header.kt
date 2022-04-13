package cn.sunline.saas.global.constant.meta

/**
 * @title: Header
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 15:16
 */
enum class Header(key:String) {
    REQUEST_ID("X-Request-Id"),USER_AUTHORIZATION("X-Authorization-Username"),TENANT_AUTHORIZATION("X-Authorization-Tenant")
}