package cn.sunline.saas.document.generation.config

import cn.sunline.saas.document.generation.factory.ConvertFactory
import cn.sunline.saas.document.generation.models.FileType
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import org.apache.poi.xwpf.usermodel.XWPFRun
import org.apache.poi.xwpf.usermodel.XWPFTable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class FileGeneration {
    @Autowired
    private lateinit var convertFactory: ConvertFactory

    fun generation(templateStream: InputStream, params: Map<String, String>, convertType: FileType):InputStream {

        val document = fillTemplate(templateStream, params)

        val convert = convertFactory.instance(convertType)

        return convert.convert(document)
    }


    private fun fillTemplate(inputStream:InputStream,params:Map<String,String>): XWPFDocument{

        val document = XWPFDocument(inputStream)
        //paragraphs
        val documentParagraphs = document.paragraphs
        replaceParagraphs(documentParagraphs,params)

        //tables
        val documentTables = document.tables
        replaceTables(documentTables,params)

        return document

    }


    private fun replaceParagraphs(paragraphs:List<XWPFParagraph>,params:Map<String,String>){
        paragraphs.forEach{ paragraph ->
            replaceInParagraph(paragraph,params)
            }
        }

    private fun replaceTables(tables:List<XWPFTable>,params: Map<String, String>){
        tables.forEach{ table ->
            table.rows.forEach{ row ->
                row.tableCells.forEach{ cell ->
                    cell.paragraphs.forEach{ paragraph ->
                        replaceInParagraph(paragraph,params)
                    }
                }
            }
        }
    }

    /*
        solve
        ----> $
        ----> {
        ----> value
        ----> }
     */
    private fun replaceInParagraph(paragraph:XWPFParagraph,params: Map<String, String>){
        val runSet = HashSet<XWPFRun>()
        var tempStr = ""
        var lastChar:Char? = null

        for(run in paragraph.runs){
            var text = run.getText(0) ?: continue
            text = replaceByParam(text,params)
            run.setText("",0)
            run.setText(text,0)
            for(ch in text){
                if(ch == '$'){
                    runSet.clear()
                    runSet.add(run)
                    tempStr = text
                } else if (ch == '{'){
                    if(lastChar == '$'){
                        if(!runSet.contains(run)){
                            runSet.add(run)
                            tempStr += text
                        }
                    } else {
                        runSet.clear()
                        tempStr = ""
                    }
                } else if (ch == '}'){
                    if(tempStr != "" && tempStr.indexOf("\${") >= 0) {
                        if(!runSet.contains(run)){
                            runSet.add(run)
                            tempStr += text
                        }
                    } else {
                        runSet.clear()
                        tempStr = ""
                    }
                    if(runSet.size > 0){
                        val replace = replaceByParam(tempStr,params)
                            if(replace != tempStr){
                            var idx = 0
                            var aRun:XWPFRun? = null
                            runSet.forEach{ tempRun ->
                                tempRun.setText("",0)
                                if(idx == 0){
                                    aRun = tempRun
                                }
                                idx++
                            }
                            aRun!!.setText(replace,0)
                        }
                        runSet.clear()
                        tempStr = ""
                    }
                }else{
                    if(runSet.size <= 0) continue
                    if(runSet.contains(run)) continue
                    runSet.add(run)
                    tempStr += text
                }
                lastChar = ch
            }

        }

    }

    private fun replaceByParam(formatStr:String,params:Map<String,String>):String{
        var resultStr = formatStr
        params.forEach{ (k, v) ->
            run {
                if (resultStr.contains(k)) {
                    resultStr = resultStr.replace(k, v)
                }
            }
        }
        return resultStr
    }

}