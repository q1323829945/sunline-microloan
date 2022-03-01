package cn.sunline.saas.document.generation.factory

import java.io.InputStream


interface BaseFileConvert {

    fun convert(document: Any):InputStream
}