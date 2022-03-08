package cn.sunline.saas.document.generation.convert.factory.impl

import cn.sunline.saas.document.generation.convert.factory.BaseFileConvert
import org.apache.poi.xwpf.converter.pdf.PdfOptions
import org.apache.poi.xwpf.converter.pdf.internal.PdfMapper
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

class ConvertToPDF: BaseFileConvert {

    override fun convert(document: Any): InputStream {
        return if(document is XWPFDocument) {
            docxConvert(document)
        } else if (document is InputStream){
            document
        } else {
            throw Exception("document error")
        }
    }


    private fun docxConvert(document: XWPFDocument):InputStream{
        val options = PdfOptions.create()

        val byteArrayOutputStream = ByteArrayOutputStream()
        val mapper = PdfMapper(document, byteArrayOutputStream, options, null)
        mapper.start()
        return ByteArrayInputStream(byteArrayOutputStream.toByteArray())
    }
}