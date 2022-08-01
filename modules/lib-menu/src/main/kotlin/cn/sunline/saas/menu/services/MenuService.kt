package cn.sunline.saas.menu.services

import cn.sunline.saas.menu.modules.Menu
import cn.sunline.saas.menu.modules.ResultMenu
import org.springframework.stereotype.Service

@Service
class MenuService {

    private lateinit var menuList:List<Menu>


    init {
        val sysConfig = Menu("sysConfig","","","",1)
        val businessConfig = Menu("businessConfig","","","",2)
        val enterpriseConfig = Menu("enterpriseConfig","","","",3)
        val businessHandling= Menu("businessHandling","","","",4)
        val businessQuery = Menu("businessQuery","","","",5)

        val roleConfig = Menu("roleConfig","sysConfig","","/dashboard/role",1)
        val permissionConfig = Menu("permissionConfig","sysConfig","","/dashboard/permissions",2)
        val userConfig = Menu("userConfig","sysConfig","","/dashboard/user",3)
        val ratePlanConfig = Menu("ratePlanConfig","businessConfig","","/dashboard/ratePlan",4)
        val documentTemplateConfig = Menu("documentTemplateConfig","businessConfig","","/dashboard/documentTemplate",5)
        val loanProductConfig = Menu("loanProductConfig","businessConfig","","/dashboard/loanProduct",6)
        val riskControlRuleConfig = Menu("riskControlRuleConfig","businessConfig","","/dashboard/riskControlRule",7)
        val organisationConfig = Menu("organisationConfig","enterpriseConfig","","/dashboard/organisationConfig",8)
        val employeeConfig = Menu("employeeConfig","enterpriseConfig","","/dashboard/employeeConfig",9)
        val customerConfig = Menu("customerConfig","businessConfig","","/dashboard/customerConfig",10)
        val customerOfferConfig = Menu("customerOfferConfig","businessHandling","","/dashboard/customerOfferConfig",11)
        val loanAgreementManagementConfig = Menu("loanAgreementManagementConfig","businessHandling","","/dashboard/loanAgreementManagementConfig",12)
        val repaymentManagementConfig = Menu("repaymentManagementConfig","businessHandling","","/dashboard/repaymentManagementLoanConfig",13)
        val pdpaConfig = Menu("pdpaConfig","businessConfig","","/dashboard/pdpaConfig",14)
        val pdpaAuthorityConfig = Menu("pdpaAuthorityConfig","businessConfig","","/dashboard/pdpaAuthorizationConfig",15)
        val loanQuery = Menu("loanQuery","businessQuery","","/dashboard/businessQuery",17)



        menuList = listOf(businessConfig,sysConfig,enterpriseConfig,businessHandling,businessQuery
            ,roleConfig
            ,permissionConfig
            ,userConfig
            ,ratePlanConfig
            ,documentTemplateConfig
            ,loanProductConfig
            ,riskControlRuleConfig
            ,organisationConfig
            ,customerConfig
            ,customerOfferConfig
            ,loanAgreementManagementConfig
            ,employeeConfig
            ,repaymentManagementConfig
            ,pdpaConfig
            ,pdpaAuthorityConfig
            ,loanQuery)

    }


    fun getPermissionMenu(menuName: Set<String>): List<ResultMenu> {
        val baseMenuSet = getPermissionSelectMenu(menuName)

        return menuRecursion("", baseMenuSet)!!.sortedBy { it.sort }
    }

    private fun menuRecursion(parentName: String,menuSet:HashSet<Menu>):List<ResultMenu>?{
        val children:ArrayList<ResultMenu> = arrayListOf()
        for(menu in menuSet){
            if(menu.parentName == parentName){
                children.add(ResultMenu(menu.name,menu.parentName,menu.icon,menu.path,menuRecursion(menu.name,menuSet)?.sortedBy { it.sort },menu.sort))
            }
        }

        if(children.size == 0){
            return null
        }
        return children
    }

    private fun getPermissionSelectMenu(menuName:Set<String>):HashSet<Menu>{
        //得到选中菜单
        val permissionMenu = menuList.filter {
            menuName.contains(it.name)
        }.toHashSet()

        val resultMenu = hashSetOf<Menu>()
        resultMenu.addAll(permissionMenu)
        val tempMenu = hashSetOf<Menu>()
        while (true){
            tempMenu.clear()
            tempMenu.addAll(permissionMenu)
            permissionMenu.clear()
            var flag = true
            for(menu in tempMenu){
                if(menu.parentName != ""){
                    val parent = menuList.filter {
                        it.name == menu.parentName }[0]
                    permissionMenu.add(parent)
                    flag = false
                }
            }

            resultMenu.addAll(permissionMenu)

            if (flag){
                return resultMenu
            }
        }

    }



}
