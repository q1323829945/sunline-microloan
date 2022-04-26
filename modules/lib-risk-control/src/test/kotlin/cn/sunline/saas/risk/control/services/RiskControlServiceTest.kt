package cn.sunline.saas.risk.control.services

import cn.sunline.saas.risk.control.rule.modules.DataItem
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RelationalOperatorType
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRule
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRuleParam
import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RiskControlServiceTest {

    @Autowired
    private lateinit var riskControlService: RiskControlService

    @Test
    fun execute(){
        val list = getList()

        val result = riskControlService.execute(1,list)

        println(result.result)
        println(result.reason)
        println("----------------------")
        result.reports?.forEach {
            println(it.ruleName)
            println(it.result)
            println(it.logicalOperationType.symbol)
            println(it.executeEquation)
        }
        println("----------------------")

        assertThat(result.result).isEqualTo(true)

    }

    data class Data(
        val list:List<RiskControlRule>
    )

    private fun getList():List<RiskControlRule>{
        val json = "{\"list\":[{\n" +
                "\t\"id\": 23245421931937792,\n" +
                "\t\"name\": \"我的规则1\",\n" +
                "\t\"ruleType\": \"PERSONAL\",\n" +
                "\t\"sort\": 1,\n" +
                "\t\"logicalOperationType\": \"AND\",\n" +
                "\t\"remark\": \"备注2\",\n" +
                "\t\"description\": \"credit_risk <= 23344 && (credit_risk <= 23344 || credit_risk <= 23344) && credit_risk <= 23344\",\n" +
                "\t\"params\": [{\n" +
                "\t\t\"id\": 23246101585346560,\n" +
                "\t\t\"ruleId\": 23245421931937792,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": 23246101585346561,\n" +
                "\t\t\"ruleId\": 23245421931937792,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"OR\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": 23246101585346562,\n" +
                "\t\t\"ruleId\": 23245421931937792,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"OR\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": 23246101585346563,\n" +
                "\t\t\"ruleId\": 23245421931937792,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}],\n" +
                "\t\"created\": \"Apr 26, 2022, 2:56:47 PM\",\n" +
                "\t\"updated\": \"Apr 26, 2022, 2:59:29 PM\",\n" +
                "\t\"tenantId\": 0\n" +
                "}, {\n" +
                "\t\"id\": 23247102984077312,\n" +
                "\t\"name\": \"我的规则2\",\n" +
                "\t\"ruleType\": \"PERSONAL\",\n" +
                "\t\"sort\": 2,\n" +
                "\t\"logicalOperationType\": \"AND\",\n" +
                "\t\"remark\": \"备注2\",\n" +
                "\t\"description\": \"(credit_risk <= 23344 || credit_risk <= 23344)\",\n" +
                "\t\"params\": [{\n" +
                "\t\t\"id\": 23247103026020352,\n" +
                "\t\t\"ruleId\": 23247102984077312,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"OR\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": 23247103026020353,\n" +
                "\t\t\"ruleId\": 23247102984077312,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"OR\"\n" +
                "\t}],\n" +
                "\t\"created\": \"Apr 26, 2022, 3:03:28 PM\",\n" +
                "\t\"updated\": \"Apr 26, 2022, 3:03:28 PM\",\n" +
                "\t\"tenantId\": 0\n" +
                "}, {\n" +
                "\t\"id\": 23247147972182016,\n" +
                "\t\"name\": \"我的规则3\",\n" +
                "\t\"ruleType\": \"PERSONAL\",\n" +
                "\t\"sort\": 3,\n" +
                "\t\"logicalOperationType\": \"AND\",\n" +
                "\t\"remark\": \"备注2\",\n" +
                "\t\"description\": \"credit_risk <= 23344 && credit_risk <= 23344\",\n" +
                "\t\"params\": [{\n" +
                "\t\t\"id\": 23247148005736448,\n" +
                "\t\t\"ruleId\": 23247147972182016,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": 23247148005736449,\n" +
                "\t\t\"ruleId\": 23247147972182016,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}],\n" +
                "\t\"created\": \"Apr 26, 2022, 3:03:38 PM\",\n" +
                "\t\"updated\": \"Apr 26, 2022, 3:03:38 PM\",\n" +
                "\t\"tenantId\": 0\n" +
                "}, {\n" +
                "\t\"id\": 23247172836016128,\n" +
                "\t\"name\": \"我的规则4\",\n" +
                "\t\"ruleType\": \"PERSONAL\",\n" +
                "\t\"sort\": 4,\n" +
                "\t\"logicalOperationType\": \"OR\",\n" +
                "\t\"remark\": \"备注2\",\n" +
                "\t\"description\": \"credit_risk <= 23344 && credit_risk <= 23344\",\n" +
                "\t\"params\": [{\n" +
                "\t\t\"id\": 23247172882153472,\n" +
                "\t\t\"ruleId\": 23247172836016128,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": 23247172882153473,\n" +
                "\t\t\"ruleId\": 23247172836016128,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}],\n" +
                "\t\"created\": \"Apr 26, 2022, 3:03:44 PM\",\n" +
                "\t\"updated\": \"Apr 26, 2022, 3:03:44 PM\",\n" +
                "\t\"tenantId\": 0\n" +
                "}, {\n" +
                "\t\"id\": 23247183472771072,\n" +
                "\t\"name\": \"我的规则5\",\n" +
                "\t\"ruleType\": \"PERSONAL\",\n" +
                "\t\"sort\": 5,\n" +
                "\t\"logicalOperationType\": \"OR\",\n" +
                "\t\"remark\": \"备注2\",\n" +
                "\t\"description\": \"credit_risk <= 23344 && credit_risk <= 23344\",\n" +
                "\t\"params\": [{\n" +
                "\t\t\"id\": 23247183527297024,\n" +
                "\t\t\"ruleId\": 23247183472771072,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": 23247183527297025,\n" +
                "\t\t\"ruleId\": 23247183472771072,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}],\n" +
                "\t\"created\": \"Apr 26, 2022, 3:03:47 PM\",\n" +
                "\t\"updated\": \"Apr 26, 2022, 3:03:47 PM\",\n" +
                "\t\"tenantId\": 0\n" +
                "}, {\n" +
                "\t\"id\": 23247202334556160,\n" +
                "\t\"name\": \"我的规则6\",\n" +
                "\t\"ruleType\": \"PERSONAL\",\n" +
                "\t\"sort\": 6,\n" +
                "\t\"logicalOperationType\": \"AND\",\n" +
                "\t\"remark\": \"备注2\",\n" +
                "\t\"description\": \"credit_risk <= 23344 && credit_risk <= 23344\",\n" +
                "\t\"params\": [{\n" +
                "\t\t\"id\": 23247202380693504,\n" +
                "\t\t\"ruleId\": 23247202334556160,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": 23247202380693505,\n" +
                "\t\t\"ruleId\": 23247202334556160,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}],\n" +
                "\t\"created\": \"Apr 26, 2022, 3:03:51 PM\",\n" +
                "\t\"updated\": \"Apr 26, 2022, 3:03:51 PM\",\n" +
                "\t\"tenantId\": 0\n" +
                "}, {\n" +
                "\t\"id\": 23247215055880192,\n" +
                "\t\"name\": \"我的规则7\",\n" +
                "\t\"ruleType\": \"PERSONAL\",\n" +
                "\t\"sort\": 7,\n" +
                "\t\"logicalOperationType\": \"AND\",\n" +
                "\t\"remark\": \"备注2\",\n" +
                "\t\"description\": \"credit_risk <= 23344 && credit_risk <= 23344\",\n" +
                "\t\"params\": [{\n" +
                "\t\t\"id\": 23247215118794752,\n" +
                "\t\t\"ruleId\": 23247215055880192,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": 23247215118794753,\n" +
                "\t\t\"ruleId\": 23247215055880192,\n" +
                "\t\t\"dataItem\": \"CREDIT_RISK\",\n" +
                "\t\t\"relationalOperatorType\": \"LE\",\n" +
                "\t\t\"threshold\": \"23344\",\n" +
                "\t\t\"logicalOperationType\": \"AND\"\n" +
                "\t}],\n" +
                "\t\"created\": \"Apr 26, 2022, 3:03:54 PM\",\n" +
                "\t\"updated\": \"Apr 26, 2022, 3:03:54 PM\",\n" +
                "\t\"tenantId\": 0\n" +
                "}]}"

        val data = Gson().fromJson(json,Data::class.java)
        return data.list
    }
}
