package cn.sunline.saas.money.transfer.instruction.model

import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: MoneyTransferInstruction
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/19 16:39
 */

@Entity
@Table(name = "money_transfer_instruction")
class MoneyTransferInstruction(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "money_transfer_instruction_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val moneyTransferInstructionType: MoneyTransferInstructionType,

    @NotNull
    @Column(name="money_transfer_instruction_amount",nullable = false, scale = 19, precision = 2, columnDefinition = "decimal(19,2) not null")
    val moneyTransferInstructionAmount: BigDecimal,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "money_transfer_instruction_currency", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val moneyTransferInstructionCurrency: CurrencyType,

    @Column(name="money_transfer_instruction_purpose",nullable = true, length = 128, columnDefinition = "varchar(128) null")
    val moneyTransferInstructionPurpose: String?,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "money_transfer_instruction_status", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var moneyTransferInstructionStatus: InstructionLifecycleStatus,

    @Column(name="payee_account",nullable = true, length = 64, columnDefinition = "varchar(64) null")
    val payeeAccount: String?,

    @Column(name="payer_account",nullable = true, length = 64, columnDefinition = "varchar(64) null")
    val payerAccount: String?,

    @NotNull
    @Column(name = "agreement_id", nullable = false, columnDefinition = "bigint not null")
    val agreementId: Long,

    @NotNull
    @Column(name = "business_unit", nullable = false, columnDefinition = "bigint not null")
    val businessUnit:Long,

    @NotNull
    @Column(name = "reference_id", nullable = true, columnDefinition = "bigint null")
    val referenceId: Long?

) : MultiTenant {

    @NotNull
    @Column(name = "tenant_id", nullable = false, columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long? {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}