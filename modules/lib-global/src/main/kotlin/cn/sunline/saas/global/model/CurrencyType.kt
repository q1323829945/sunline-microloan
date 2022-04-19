package cn.sunline.saas.global.model

enum class CurrencyType(
        val number:String,
        val englishName:String,
        val chineseName:String
) {
    CNY("156","Chinese Yuan","人民币"),
    USD("840","United States Dollar","美元"),
}