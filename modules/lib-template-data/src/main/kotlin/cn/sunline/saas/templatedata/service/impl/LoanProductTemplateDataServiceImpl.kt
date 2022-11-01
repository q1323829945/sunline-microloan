package cn.sunline.saas.templatedata.service.impl

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.templatedata.service.TemplateDataService
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses


class LoanProductTemplateDataServiceImpl: TemplateDataService() {
    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime


//    var id: String?,
//    var identificationCode: String,
//    var name: String,
//    var version: String,
//    var description: String,
//    var loanProductType: LoanProductType,
//    var loanPurpose: String,
//    var businessUnit: String,
//    var status: BankingProductStatus?,
//    var amountConfiguration: DTOAmountLoanProductConfiguration,
//    var termConfiguration: DTOTermLoanProductConfiguration,
//    var interestFeature: DTOInterestFeature,
//    var repaymentFeature: DTORepaymentFeature,
//    var feeFeatures: MutableList<DTOFeeFeature>?,
//    var loanUploadConfigureFeatures: List<Long>? = listOf(),
//    var documentTemplateFeatures:List<Long>? = listOf()

    override fun <T: Any> getTemplateData(type: KClass<T>,defaultMapData: Map<String, Any>?, overrideDefaults: Boolean): T {
        val constructor = type.primaryConstructor!!
        val parameters = constructor.parameters
//        if (!parameters.all { param -> (param.type.classifier == String::class || param.isOptional)||
//                    (param.type.classifier == Long ::class || param.isOptional)}){
//            error("Class $type primary constructor has required non-String parameters.")
//        }
        val mapData =  mutableMapOf<KParameter, Any?>()
        constructor.parameters.forEach { param ->
            if(param.type.classifier == Long::class){
                mapData[param] = sequence.nextId()
            }
            if(param.type.classifier == String::class){
                if(param.name!!.contains("id")){
                    mapData[param] = sequence.nextId().toString()
                }else {
                    mapData[param] = "888811L"
                }
            }
            if(param.type.classifier == Date::class){
                mapData[param] = tenantDateTime.now().toDate()
            }
            if(param.type.classifier == Boolean::class){
                mapData[param] = false
            }
            if((param.type.classifier as KClass<*>).superclasses.first() == Enum::class){
                mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
            }
        }

        return constructor.callBy(mapData)

    }
}