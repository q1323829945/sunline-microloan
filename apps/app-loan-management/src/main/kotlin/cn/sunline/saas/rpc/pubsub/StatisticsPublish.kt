package cn.sunline.saas.rpc.pubsub

import cn.sunline.saas.rpc.pubsub.dto.DTOCommissionDetail
import cn.sunline.saas.rpc.pubsub.dto.DTOLoanApplicationDetail

interface StatisticsPublish {

    fun addLoanApplicationDetail(dtoLoanApplicationDetail: DTOLoanApplicationDetail)

    fun addLoanApplicationStatistics()

    fun addCommissionDetail(dtoCommissionDetail: DTOCommissionDetail)

    fun addCommissionStatistics()
}