package cn.sunline.saas.document.generation.factory.service


interface BaseFileConvert {

    fun convert(document: Any, filePath:String, fileName: String):String
}