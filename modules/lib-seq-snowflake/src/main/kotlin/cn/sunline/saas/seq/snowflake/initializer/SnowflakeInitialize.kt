package cn.sunline.saas.seq.snowflake.initializer

import cn.sunline.saas.seq.snowflake.config.SnowflakeConfig

/**
 * @title: SnowflakeInitialize
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/1 14:26
 */
interface SnowflakeInitialize {

    fun getConfig(): SnowflakeConfig
}