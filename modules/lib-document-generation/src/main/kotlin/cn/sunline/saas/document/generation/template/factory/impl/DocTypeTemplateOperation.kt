package cn.sunline.saas.document.generation.template.factory.impl

import cn.sunline.saas.document.generation.template.factory.BaseTemplateOperation
import com.lowagie.text.Document
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfWriter
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.extractor.WordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

fun main() {
    val doc = DocTypeTemplateOperation()

    val file = FileInputStream(File("D:\\新建文件夹\\合同填充\\移动CA中心个人数字证书申请表.doc"))

    val params = mapOf(Pair("\${name}","授权人"),Pair("\${idNo}","身份证"),Pair("\${year}","2022")
            ,Pair("\${month}","3"),Pair("\${day}","1"),Pair("\${sign}","注册了一波"),Pair("\${time}","整个日期"))
    doc.fillTemplate(file,params)
}

class DocTypeTemplateOperation : BaseTemplateOperation {

    override fun fillTemplate(inputStream: InputStream, params: Map<String, String>): Any {

        val document = HWPFDocument(inputStream)
        val range = document.range

        params.forEach{ (k, v) ->
            range.replaceText(k,v)
        }

        val doc = Document()
        val pdfWriter = PdfWriter.getInstance(doc,FileOutputStream(File("D:\\docTopdf.pdf")))
        doc.open();
        pdfWriter.setPageEmpty(true);

        val we = WordExtractor(document)

        doc.newPage();
        pdfWriter.setPageEmpty(true);
        for(page in 0 until range.numParagraphs()){
            val paragraph = range.getParagraph(page)
            doc.add(Paragraph(we.paragraphText[page]))

            println(we.paragraphText[page].toString())
            var j = 0
//            while (true){
//                val run = paragraph.getCharacterRun(j++)
//                println("border:${run.border}")
//                println("color:${run.color}")
//                println("Font size:${run.fontSize}")
//                println("Font Name:${run.fontName}")
//                println("${run.isBold} ${run.isItalic} ${run.underlineCode}")
//                println("text ${run.text()}")
//                if(run.endOffset == paragraph.endOffset){
//                    break
//                }
//            }
        }

        doc.close()

//        val doc = Document

//        val byteArrayOutputStream = ByteArrayOutputStream()
//        document.write(byteArrayOutputStream)
//
//        val doc = Document()
//        val writer = PdfWriter.getInstance(doc,byteArrayOutputStream)
//
////
////
//        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
//
//
//        val docx = XWPFDocument(byteArrayInputStream)
//
//        val ph = docx.paragraphs
//
//        for (p in ph){
//            println(p.text)
//        }


        return ""

    }


}