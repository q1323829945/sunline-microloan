package cn.sunline.saas.product.controller.dto

import cn.sunline.saas.loan.product.model.dto.DTOAmountLoanProductConfiguration
import cn.sunline.saas.loan.product.model.dto.DTOTermLoanProductConfiguration


class DTOBaseLoanProductView (
        var id:String,
        val identificationCode:String,
        val name:String,
        val version:String
)