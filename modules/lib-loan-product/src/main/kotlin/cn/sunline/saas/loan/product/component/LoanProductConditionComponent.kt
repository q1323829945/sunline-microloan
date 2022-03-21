package cn.sunline.saas.loan.product.component

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.loan.product.model.ConditionType
import cn.sunline.saas.rule.engine.model.Condition
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal

/**
 * @title: LoanProductConditionComponent
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/9 17:02
 */
@Component
class LoanProductConditionComponent(
    @Autowired private val seq: Sequence
) {

    fun amountCondition(productId:Long,maxAmount: BigDecimal?, minAmount: BigDecimal?): Condition {
        val condition = Condition(seq.nextId(), ConditionType.AMOUNT.name, ConditionType.AMOUNT.getMarker(),productId)
        condition.setValue(maxAmount, minAmount)
        return condition
    }


    fun termCondition(productId:Long,maxTerm: LoanTermType?, minTerm: LoanTermType?): Condition {
        val condition = Condition(seq.nextId(), ConditionType.TERM.name, ConditionType.TERM.getMarker(),productId)
        var maxCondition: BigDecimal? = null
        if (maxTerm != null) {
            maxCondition = BigDecimal(maxTerm.convertDays())
        }
        var minCondition: BigDecimal? = null
        if (minTerm != null) {
            minCondition = BigDecimal(minTerm.convertDays())
        }

        condition.setValue(maxCondition, minCondition)
        return condition
    }

    fun updateTermCondition(id:Long,maxTerm: LoanTermType?, minTerm: LoanTermType?,productId:Long): String {
        val condition = Condition(id, ConditionType.TERM.name, ConditionType.TERM.getMarker(),productId)
        var maxCondition: BigDecimal? = null
        if (maxTerm != null) {
            maxCondition = BigDecimal(maxTerm.convertDays())
        }
        var minCondition: BigDecimal? = null
        if (minTerm != null) {
            minCondition = BigDecimal(minTerm.convertDays())
        }

        condition.setValue(maxCondition, minCondition)
        return condition.description
    }

}