package cn.sunline.saas.underwriting.arrangement.factory

import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.underwriting.arrangement.model.ArrangementLifecycleStatus
import cn.sunline.saas.underwriting.arrangement.model.db.UnderwritingArrangement
import cn.sunline.saas.underwriting.arrangement.model.db.UnderwritingArrangementInvolvement
import cn.sunline.saas.underwriting.arrangement.model.dto.DTOUnderwritingArrangementAdd
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @title: LoanAgreementFactory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/22 19:09
 */
@Component
class UnderwritingArrangementFactory {

    @Autowired
    private lateinit var seq: Sequence

    fun instance(dtoUnderwritingArrangementAdd: DTOUnderwritingArrangementAdd): MutableList<UnderwritingArrangement> {
        val underwritingArrangements = mutableListOf<UnderwritingArrangement>()

        val agreementId = dtoUnderwritingArrangementAdd.agreementId
        dtoUnderwritingArrangementAdd.underwriting.forEach {

            val involvements = mutableListOf<UnderwritingArrangementInvolvement>()
            it.involvements.forEach { involve ->
                val involvement = UnderwritingArrangementInvolvement(
                    id = seq.nextId(),
                    partyId = involve.party,
                    primary = involve.primary
                )
                involvements.add(involvement)
            }

            val underwritingArrangement = UnderwritingArrangement(
                id = seq.nextId(),
                agreementId = agreementId,
                startDate = DateTime(it.startDate).toInstant(),
                endDate = DateTime(it.endDate).toInstant(),
                involvements = involvements,
                status = ArrangementLifecycleStatus.PROPOSED
            )

            underwritingArrangements.add(underwritingArrangement)
        }
        return underwritingArrangements
    }
}