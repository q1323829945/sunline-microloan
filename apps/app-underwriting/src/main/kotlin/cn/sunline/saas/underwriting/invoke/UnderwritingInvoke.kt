package cn.sunline.saas.underwriting.invoke

import cn.sunline.saas.partner.integrated.model.dto.DTOPartnerIntegrated

/**
 * @title: UnderwritingInvoke
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 11:00
 */
interface UnderwritingInvoke {

    fun getPartnerIntegrated(): DTOPartnerIntegrated?
}