package cn.sunline.saas.document.generation.factory.service.impl

import cn.sunline.saas.document.generation.factory.service.BaseFileConvert
import org.apache.poi.xwpf.converter.pdf.PdfConverter
import org.apache.poi.xwpf.converter.pdf.PdfOptions
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths

class ConvertToPDF: BaseFileConvert {

    override fun convert(document: XWPFDocument, filePath:String, fileName: String):String {
        val options = PdfOptions.create()
        val path = filePath + fileName
        val out = Files.newOutputStream(Paths.get(path))

        PdfConverter.getInstance().convert(document,out,options)

        return path
    }
}