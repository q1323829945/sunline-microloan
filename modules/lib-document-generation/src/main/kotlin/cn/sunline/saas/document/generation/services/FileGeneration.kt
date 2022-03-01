package cn.sunline.saas.document.generation.services

import cn.sunline.saas.document.generation.models.FileType
import cn.sunline.saas.document.generation.models.TemplateType
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import org.apache.poi.xwpf.usermodel.XWPFTable
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

@Service
class FileGeneration {

    fun generation(templateType: TemplateType, params: Map<String, String>, convertType: FileType, filePath: String, fileName: String): String{
        return generation(templateType.url, params, convertType, filePath, fileName)
    }

    fun generation(path: String, params: Map<String, String>, convertType: FileType, filePath: String, fileName: String): String {
        val templateStream = FileInputStream(File(path))
        return generation(templateStream, params, convertType, filePath, fileName)
    }

    fun generation(templateStream: InputStream, params: Map<String, String>, convertType: FileType, filePath: String, fileName: String): String {

        val document = fillTemplate(templateStream, params)

        val convert = convertType.convent

        return convert.convert(document, filePath, fileName)
    }


    fun fillTemplate(inputStream:InputStream,params:Map<String,String>): XWPFDocument{

        val document = XWPFDocument(inputStream)
        //paragraphs
        val documentParagraphs = document.paragraphs
        replaceParagraphs(documentParagraphs,params)

        //tables
        val documentTables = document.tables
        replaceTables(documentTables,params)

        return document

    }


    fun replaceParagraphs(paragraphs:List<XWPFParagraph>,params:Map<String,String>){
        for(paragraph in paragraphs){
            for(run in paragraph.runs){
                val text = run.getText(run.textPosition)
                if(text != null){
                    val operaString = text.trim()
                    val resultString = replaceByParam(operaString,params)
                    run.setText(resultString,0)
                }
            }
        }
    }

    fun replaceTables(tables:List<XWPFTable>,params: Map<String, String>){
        for(table in tables){
            for(row in table.rows){
                for(cell in row.tableCells){
                    for(paragraph in cell.paragraphs){
                        for(run in paragraph.runs){
                            val text = run.getText(run.textPosition)
                            if(text != null){
                                val operaString = text.trim()
                                val resultString = replaceByParam(operaString,params)
                                run.setText(resultString,0)
                            }
                        }
                    }
                }
            }
        }
    }

    fun replaceByParam(formatStr:String,params:Map<String,String>):String{
        var resultStr = formatStr
        for((key,value) in params){
            if(resultStr.contains(key)){
                resultStr = resultStr.replace(key,value)
            }
        }
        return resultStr
    }
}