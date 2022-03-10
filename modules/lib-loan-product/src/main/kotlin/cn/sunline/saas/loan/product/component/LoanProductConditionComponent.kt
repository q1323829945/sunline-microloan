package cn.sunline.saas.loan.product.component

import cn.sunline.saas.loan.product.model.ConditionType
import cn.sunline.saas.loan.product.model.LoanTermType
import cn.sunline.saas.rule.engine.model.Condition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal
import cn.sunline.saas.seq.Sequence

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

    private var conditions: MutableList<Condition> = mutableListOf()

    fun amountCondition(maxAmount: BigDecimal?, minAmount: BigDecimal?): LoanProductConditionComponent {
        val condition = Condition(seq.nextId(), ConditionType.AMOUNT.name, ConditionType.AMOUNT.getMarker())
        condition.setValue(maxAmount, minAmount)
        conditions.add(condition)
        return this
    }

    fun termCondition(maxTerm: LoanTermType?, minTerm: LoanTermType?): LoanProductConditionComponent {
        val condition = Condition(seq.nextId(), ConditionType.TERM.name, ConditionType.TERM.getMarker())
        var maxCondition: BigDecimal? = null
        if (maxTerm != null) {
            maxCondition = BigDecimal(maxTerm.convertDays())
        }
        var minCondition: BigDecimal? = null
        if (minTerm != null) {
            minCondition = BigDecimal(minTerm.convertDays())
        }

        condition.setValue(maxCondition, minCondition)
        conditions.add(condition)
        return this
    }

    fun builder(): MutableList<Condition> {
        return conditions
    }
}