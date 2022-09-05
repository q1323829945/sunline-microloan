package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.loan.model.enum.LoanType
import java.math.BigDecimal


data class DTOLoanApplyAdd(
    val applicationId:String,
    val productType: ProductType,
    val term:LoanTermType? = null,
    val amount:BigDecimal? = null,
    val data:String,
)

data class DTOLoanApplyChange(
    val applicationId:String,
    val productType: ProductType,
    val term:LoanTermType? = null,
    val amount:BigDecimal? = null,
    val data:String,
)

data class DTOLoanApplyStatus(
    val applicationId:String,
    val status: ApplyStatus
)

data class DTOLoanApplyPageView(
    val applicationId:String,
    val name:String? = null,
    val amount: BigDecimal? = null,
    val productName:String? = null,
    val productType: ProductType? = null,
    val term:LoanTermType? = null,
    val date:String? = null,
    val status:ApplyStatus,
    val channelCode:String? = null,
    val channelName:String? = null,
    val supplement:String?
)



