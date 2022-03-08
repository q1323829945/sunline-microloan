package cn.sunline.saas.document.generation.convert.factory

import java.io.InputStream


interface BaseFileConvert {
    fun convert(document: Any):InputStream
}