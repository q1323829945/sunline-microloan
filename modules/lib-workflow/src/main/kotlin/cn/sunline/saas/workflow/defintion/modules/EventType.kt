package cn.sunline.saas.workflow.defintion.modules

enum class EventType(val id:String,val description:String) {
    CHECK_CUSTOMER("checkCustomer","Check customer identity"),
    CHECK_DATA("checkData","Check whether the data is legal"),
    RECOMMEND_PRODUCT("recommendProduct","Recommend appropriate products"),
    COLLECT_INFORMATION("collectInformation","Collect detailed information"),
    CUSTOMER_ARCHIVE("customerArchive","Customer information archiving"),
    ASSETS_ARCHIVE("assetsArchive","Archive customer asset information"),
    PRE_APPROVAL("preApproval","Pre approval")
}
