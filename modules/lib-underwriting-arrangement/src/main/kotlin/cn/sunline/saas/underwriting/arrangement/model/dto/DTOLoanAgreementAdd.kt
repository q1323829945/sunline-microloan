package cn.sunline.saas.underwriting.arrangement.model.dto

/**
 * @title: DTOUnderwritingArrangementAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/22 19:07
 */
data class DTOUnderwritingArrangementAdd(
    val agreementId: Long,
    val underwriting: MutableList<DTOUnderwritingArrangement>
)

data class DTOUnderwritingArrangement(
    val startDate: String,
    val endDate: String,
    val involvements: MutableList<DTOUnderwritingArrangementInvolvement>
)

data class DTOUnderwritingArrangementInvolvement(
    val party: Long,
    val primary: Boolean
)
