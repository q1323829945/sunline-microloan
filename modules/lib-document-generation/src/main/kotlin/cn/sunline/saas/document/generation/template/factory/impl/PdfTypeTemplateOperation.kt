package cn.sunline.saas.document.generation.template.factory.impl

import cn.sunline.saas.document.generation.template.factory.BaseTemplateOperation
import com.itextpdf.awt.geom.Rectangle2D
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Font
import com.itextpdf.text.pdf.*
import com.itextpdf.text.pdf.parser.ImageRenderInfo
import com.itextpdf.text.pdf.parser.PdfReaderContentParser
import com.itextpdf.text.pdf.parser.RenderListener
import com.itextpdf.text.pdf.parser.TextRenderInfo
import java.io.*



class PdfTypeTemplateOperation : BaseTemplateOperation {

    data class Coordinate(
            val key:String,
            val x:Float,
            val y:Float,
            val w:Float,
            val h:Float,
            val page:Int
    )

    override fun fillTemplate(inputStream: InputStream, params: Map<String, String>): Any {
        val reader = PdfReader(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        val stamper = PdfStamper(reader,byteArrayOutputStream)
        val coordinateList = getWordsCoordinate(reader)
        coordinateList.forEach{
            if(params.containsKey(it.key)){
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
                val bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, "utf-8", BaseFont.EMBEDDED);
                val font = Font(bf,10f,Font.BOLD)
                canvas.setFontAndSize(font.baseFont,9f)
                canvas.setTextMatrix(x,y)
                canvas.showText(params[it.key])
                canvas.endText()
            }

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
            val getKeyPositionListener = GetKeyPositionListener()
            getKeyPositionListener.page = page
            pdfReaderContentParser.processContent(page,getKeyPositionListener)

            coordinateList.addAll(getKeyPositionListener.keyList)
        }
        return coordinateList
    }

    private inner class GetKeyPositionListener:RenderListener{
        private var keyWord = StringBuffer()
        private var flag = false
        private var x:Float = -1f
        private var y:Float = -1f
        private var h:Float = -1f

        var page:Int = 0
        var keyList = ArrayList<Coordinate>()

        override fun renderText(renderInfo: TextRenderInfo?) {
            renderInfo?.text?.run {

                val boundingRectange = renderInfo.baseline.boundingRectange
                //start is "$" and end is "}"
                if(containsFullKey(this,boundingRectange)) return

                //start is "$"
                if(containsStartKey(this,boundingRectange)) return

                //end is "}"
                if(containsEndKey(this,boundingRectange)) return


                if(flag){
                    keyWord.append(this)
                }
            }
        }

        private fun containsEndKey(key: String,boundingRectange: Rectangle2D.Float):Boolean{
            if(key.contains("}") && flag){
                var idx = 0
                for(i in key.indices){
                    if('}' == key[i]){
                        idx = i
                        break
                    }
                }
                keyWord.append(key.substring(0,idx+1))
                flag = false

                val w = boundingRectange.x - x +boundingRectange.width
                val coordinate = Coordinate(keyWord.toString(),x,y,w,h,page)
                keyList.add(coordinate)

                return true
            }
            return false
        }

        private fun containsStartKey(key: String,boundingRectange: Rectangle2D.Float):Boolean{
            if(key.contains("$") && !flag){

                x = boundingRectange.x
                y = boundingRectange.y
                h = boundingRectange.height

                keyWord = StringBuffer()
                var idx = 0
                for(i in key.indices){
                    if('$' == key[i]){
                        idx = i
                        break
                    }
                }
                keyWord.append(key.substring(idx))
                flag = true
                return true
            }
            return false
        }

        private fun containsFullKey(key: String,boundingRectange: Rectangle2D.Float):Boolean{
            if(key.contains("$") && key.contains("}") && !flag){

                keyWord = StringBuffer()
                var idxStart = 0
                for(i in key.indices){
                    if('$' == key[i]){
                        idxStart = i
                        break
                    }
                }
                var idxEnd = 0
                for(i in key.indices){
                    if('}' == key[i]){
                        idxEnd = i
                        break
                    }
                }
                if(idxEnd+1 > idxStart) {
                    keyWord.append(key.substring(idxStart,idxEnd+1))

                    val x = boundingRectange.x
                    val y = boundingRectange.y
                    val h = boundingRectange.height
                    val w = boundingRectange.width

                    val coordinate = Coordinate(keyWord.toString(),x,y,w,h,page)
                    keyList.add(coordinate)
                }
                return true
            }
            return false
        }




        override fun beginTextBlock() {
            //do noting
        }


        override fun endTextBlock() {
            //do noting
        }

        override fun renderImage(renderInfo: ImageRenderInfo?) {
            //do noting
        }

    }

}

