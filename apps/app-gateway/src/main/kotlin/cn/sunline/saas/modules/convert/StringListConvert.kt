package cn.sunline.saas.modules.convert

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class StringListConvert:AttributeConverter<MutableList<String>,String>{
    private val splitChar = ";"

    override fun convertToDatabaseColumn(attribute: MutableList<String>?): String {
        return attribute?.run {
            var str = ""
            for(i in attribute.indices){
                str += attribute[i]
                if(i != attribute.size -1){
                    str += splitChar
                }
            }
            str
        }?: run {
            ""
        }
    }

    override fun convertToEntityAttribute(dbData: String?): MutableList<String> {
        return dbData?.run {
            this.split(splitChar).toMutableList()
        }?:run{
            mutableListOf()
        }
    }
}