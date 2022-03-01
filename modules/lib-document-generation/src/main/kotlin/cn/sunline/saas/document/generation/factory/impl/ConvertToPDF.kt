package cn.sunline.saas.document.generation.factory.impl

import cn.sunline.saas.document.generation.factory.BaseFileConvert
import org.apache.poi.xwpf.converter.pdf.PdfOptions
import org.apache.poi.xwpf.converter.pdf.internal.PdfMapper
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

class ConvertToPDF: BaseFileConvert {

    override fun convert(document: Any): InputStream {
        if(document is XWPFDocument) {
            val options = PdfOptions.create()

            val byteArrayOutputStream = ByteArrayOutputStream()
            val mapper = PdfMapper(document, byteArrayOutputStream, options, null)
            mapper.start()


            return ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        }else{
            throw Exception("document error")
        }
    }
}