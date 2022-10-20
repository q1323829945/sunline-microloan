package cn.sunline.saas.tools

object PathMappingTools {
    data class DTOMapping(
        val path:String,
        val server:String
    )

    private val map = mutableMapOf(
        "/microloan" to "app-micro-loan",
        "/gateway" to "app-gateway"
    )

    fun mapping(path:String):DTOMapping?{
        var mapKey = ""
        for(key in map.keys){
            if(path.startsWith(key)){
                mapKey = key
                break
            }
        }

        if(mapKey.isNotEmpty()){
            return DTOMapping(
                path = path.substringAfter(mapKey),
                server = map[mapKey]!!
            )
        }

        return null
    }
}