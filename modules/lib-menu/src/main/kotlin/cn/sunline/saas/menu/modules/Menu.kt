package cn.sunline.saas.menu.modules


data class Menu (
        val name:String,
        val parentName:String,
        val icon:String,
        val path:String
)


data class ResultMenu(
        val name:String,
        val parentName:String,
        val icon:String,
        val path:String,
        val children:List<ResultMenu>?
)
