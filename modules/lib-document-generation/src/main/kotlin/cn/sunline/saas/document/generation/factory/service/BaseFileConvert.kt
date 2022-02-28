package cn.sunline.saas.document.generation.factory.service

import org.apache.poi.xwpf.usermodel.XWPFDocument

interface BaseFileConvert {

    fun convert(document: XWPFDocument, filePath:String, fileName: String):String
}