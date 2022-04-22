package cn.sunline.saas.risk.control.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class RiskControlRuleNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.RISK_CONTROL_RULE_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)