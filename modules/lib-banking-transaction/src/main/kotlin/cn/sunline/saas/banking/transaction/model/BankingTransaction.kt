package cn.sunline.saas.banking.transaction.model

import javax.persistence.Entity
import javax.persistence.Id

/**
 * @title: BankingTransaction
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/12 17:18
 */
@Entity
class BankingTransaction(
    @Id
    val id: Long
)