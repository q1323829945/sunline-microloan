package cn.sunline.saas.channel.menu.services

import cn.sunline.saas.channel.menu.modules.Menu
import cn.sunline.saas.channel.menu.modules.ResultMenu
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
        val statisticsQuery = Menu("statisticsQuery","","","",6)
        val channelConfig = Menu("channelConfig","","","",7)

        val roleConfig = Menu("roleConfig","sysConfig","","/dashboard/role",1)
        val permissionConfig = Menu("permissionConfig","sysConfig","","/dashboard/permissions",2)
        val userConfig = Menu("userConfig","sysConfig","","/dashboard/user",3)
        val ratePlanConfig = Menu("ratePlanConfig","businessConfig","","/dashboard/ratePlan",4)
        val questionnairesConfig = Menu("questionnairesConfig","businessConfig","","/dashboard/questionnaires",5)
        val loanProductConfig = Menu("loanProductConfig","businessConfig","","/dashboard/loanProduct",6)
        val loanApplySupplementConfig = Menu("loanApplySupplementConfig","businessHandling","","/dashboard/loanApplySupplement",7)
        val loanApplyQueryConfig = Menu("loanApplyQueryConfig","businessQuery","","/dashboard/loanApplyQuery",8)
        val commissionStatisticsConfig = Menu("commissionStatisticsConfig","statisticsQuery","","/dashboard/commissionStatisticsConfig",9)
        val loanApplicationStatisticsConfig = Menu("loanApplicationStatisticsConfig","statisticsQuery","","/dashboard/loanApplicationStatisticsConfig",10)
        val channelQueryConfig = Menu("channelQueryConfig","channelConfig","","/dashboard/channelQueryConfig",11)
        val positionConfig = Menu("positionConfig","sysConfig","","/dashboard/positionConfig",12)
        val pdpaConfig = Menu("pdpaConfig","businessConfig","","/dashboard/pdpaConfig",13)
        val pdpaAuthorityConfig = Menu("pdpaAuthorityConfig","businessConfig","","/dashboard/pdpaAuthorizationConfig",14)
        val workflowConfig = Menu("workflowConfig","businessConfig","","/dashboard/workflowConfig",15)
        val workflowQuery = Menu("workflowQuery","businessConfig","","/dashboard/workflowQuery",16)
        val eventHandle = Menu("eventHandle","businessConfig","","/dashboard/eventHandle",17)
        val eventHandleQuery = Menu("eventHandleQuery","businessConfig","","/dashboard/eventHandleQuery",18)


        menuList = listOf(businessConfig,sysConfig,enterpriseConfig,businessHandling,businessQuery,statisticsQuery,channelConfig
            ,roleConfig
            ,permissionConfig
            ,userConfig
            ,ratePlanConfig
            ,questionnairesConfig
            ,loanProductConfig
            ,loanApplySupplementConfig
            ,loanApplyQueryConfig
            ,commissionStatisticsConfig
            ,loanApplicationStatisticsConfig
            ,channelQueryConfig
            ,positionConfig
            ,pdpaConfig
            ,pdpaAuthorityConfig
            ,workflowConfig
            ,workflowQuery
            ,eventHandle
            ,eventHandleQuery)

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
