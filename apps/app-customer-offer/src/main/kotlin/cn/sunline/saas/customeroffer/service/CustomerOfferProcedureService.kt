package cn.sunline.saas.customeroffer.service

import cn.sunline.saas.customer.offer.modules.dto.DTOProductView
import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.pdpa.service.PDPAService
import cn.sunline.saas.product.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class CustomerOfferProcedureService {
    @Autowired
    private lateinit var pdpaService: PDPAService

    @Autowired
    private lateinit var productService: ProductService

    fun getProduct(productId:Long): DTOProductView{
        return productService.findById(productId)
    }


    fun pdpaSign(customerId:Long,pdpaTemplateId:Long,originalFilename:String,inputStream: InputStream):String{
        return pdpaService.sign(customerId,pdpaTemplateId,originalFilename,inputStream)
    }

    fun getPDPA(countryCode:String): PDPAInformation {
        return pdpaService.retrieve(countryCode)
    }
}