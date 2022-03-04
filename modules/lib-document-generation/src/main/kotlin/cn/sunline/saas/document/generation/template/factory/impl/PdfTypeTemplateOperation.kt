package cn.sunline.saas.document.generation.template.factory.impl

import cn.sunline.saas.document.generation.template.factory.BaseTemplateOperation
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Font
import com.itextpdf.text.pdf.*
import com.itextpdf.text.pdf.parser.ImageRenderInfo
import com.itextpdf.text.pdf.parser.PdfReaderContentParser
import com.itextpdf.text.pdf.parser.RenderListener
import com.itextpdf.text.pdf.parser.TextRenderInfo
import java.io.*

class PdfTypeTemplateOperation : BaseTemplateOperation {

    override fun fillTemplate(inputStream: InputStream, params: Map<String, String>): Any {
        val reader = PdfReader(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        val stamper = PdfStamper(reader,byteArrayOutputStream)
        val coordinateList = getWordsCoordinate(reader)
        coordinateList.forEach{
            val canvas = stamper.getOverContent(it.page)
            val x = it.x
            val y = it.y
            val w = it.w
            canvas.saveState()
            canvas.setColorFill(BaseColor.WHITE)

            canvas.rectangle(x,y-2,w,20f)
            canvas.fill()
            canvas.restoreState()

            canvas.beginText()
            val bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            val font = Font(bf,10f,Font.BOLD)
            canvas.setFontAndSize(font.baseFont,9f)
            canvas.setTextMatrix(x,y)
            canvas.showText(params[it.key])
            canvas.endText()
        }
        stamper.close()
        reader.close()

        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        byteArrayOutputStream.close()

        return byteArrayInputStream
    }


    private fun getWordsCoordinate(reader: PdfReader):List<Coordinate>{

        val pageNum = reader.numberOfPages

        val pdfReaderContentParser = PdfReaderContentParser(reader)
        val coordinateList = ArrayList<Coordinate>()
        for(page in 1 .. pageNum){
            val pdfRenderListener = PDfRenderListener()
            pdfRenderListener.page = page
            pdfReaderContentParser.processContent(page,pdfRenderListener)
            coordinateList.addAll(pdfRenderListener.keyList)
        }
        return coordinateList
    }


}

data class Coordinate(
        val key:String,
        val x:Float,
        val y:Float,
        val w:Float,
        val h:Float,
        val page:Int
)

class PDfRenderListener:RenderListener {
    private var keyWord = StringBuffer()
    private var flag = false
    private var x:Float = -1f
    private var y:Float = -1f
    private var h:Float = -1f


    val keyList = ArrayList<Coordinate>()
    var page = 0

    override fun beginTextBlock() {
    }

    override fun renderText(renderInfo: TextRenderInfo?) {
        renderInfo?.text?.run {
            //start is "$" and end is "}"
            if(this.contains("$") && this.contains("}") && !flag){

                keyWord = StringBuffer()
                var idxStart = 0
                for(i in this.indices){
                    if('$' == this[i]){
                        idxStart = i
                        break
                    }
                }
                var idxEnd = 0
                for(i in this.indices){
                    if('}' == this[i]){
                        idxEnd = i
                        break
                    }
                }
                if(idxEnd+1 > idxStart) {
                    keyWord.append(this.substring(idxStart,idxEnd+1))

                    val boundingRectange = renderInfo.baseline.boundingRectange
                    val x = boundingRectange.x
                    val y = boundingRectange.y
                    val h = boundingRectange.height
                    val w = boundingRectange.width

                    val coordinate = Coordinate(keyWord.toString(),x,y,w,h,page)
                    keyList.add(coordinate)
                    keyList
                }
                return
            }

            //start is "$"
            if(this.contains("$") && !flag){

                val boundingRectange = renderInfo.baseline.boundingRectange
                x = boundingRectange.x
                y = boundingRectange.y
                h = boundingRectange.height

                keyWord = StringBuffer()
                var idx = 0
                for(i in this.indices){
                    if('$' == this[i]){
                        idx = i
                        break
                    }
                }
                val t = this.substring(idx)
                keyWord.append(this.substring(idx))
                flag = true
                return
            }


            //end is "}"
            if(this.contains("}") && flag){
                var idx = 0
                for(i in this.indices){
                    if('}' == this[i]){
                        idx = i
                        break
                    }
                }
                keyWord.append(this.substring(0,idx+1))
                flag = false

                val boundingRectange = renderInfo.baseline.boundingRectange
                val w = boundingRectange.x - x +boundingRectange.width
                val coordinate = Coordinate(keyWord.toString(),x,y,w,h,page)
                keyList.add(coordinate)

                return
            }

            if(flag){
                keyWord.append(this)
            }
        }





    }

    override fun endTextBlock() {
    }

    override fun renderImage(renderInfo: ImageRenderInfo?) {
    }
}