package cn.sunline.saas.document.generation.factory.service.impl

import cn.sunline.saas.document.generation.factory.service.BaseFileConvert
import org.apache.poi.xwpf.converter.pdf.PdfConverter
import org.apache.poi.xwpf.converter.pdf.PdfOptions
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.nio.file.Files
import java.nio.file.Paths

class ConvertToPDF: BaseFileConvert {

    override fun convert(document: Any, filePath:String, fileName: String):String {
        if(document is XWPFDocument){
            val options = PdfOptions.create()
            val path = filePath + fileName
            val out = Files.newOutputStream(Paths.get(path))

            PdfConverter.getInstance().convert(document,out,options)

            return path
        }
        return ""
    }
}