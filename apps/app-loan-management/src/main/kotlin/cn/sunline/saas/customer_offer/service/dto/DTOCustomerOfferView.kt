package cn.sunline.saas.customer_offer.service.dto

import cn.sunline.saas.customer.offer.modules.dto.DTOCustomerOfferLoanView
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationView

data class DTOManagementCustomerOfferView(
    val organisation: DTOOrganisationView?,
    val customerOfferLoan: DTOCustomerOfferLoanView,
)