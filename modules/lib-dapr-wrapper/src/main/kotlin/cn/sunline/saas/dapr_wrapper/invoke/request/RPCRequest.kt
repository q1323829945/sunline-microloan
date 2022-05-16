package cn.sunline.saas.dapr_wrapper.invoke.request

abstract class RPCRequest(val tenant: String? = null) {
    protected val ModuleManagement = "management-service"
    protected val ModuleTenant = "tenant-service"
    protected val ModuleBilling = "billing-service"

    protected val MethodSubscription = "subscriptions"
    protected val MethodTenant = "tenants"
    protected val MethodBundle = "bundles"
    protected val MethodBilling = "billing"
    protected val MethodProductApplication = "product-applications"

    abstract fun getQueryParams(): Map<String, String>
    abstract fun getHeaderParams(): Map<String, String>
    abstract fun getPayload(): Any?
    abstract fun getModuleName(): String
    abstract fun getMethodName(): String
}