package cn.sunline.saas.snowflake.config

const val INIT_TIMESTAMP:Long = 1645414066687L

const val CLOCK_BACK_BITS:Int = 2
const val DATACENTER_ID_BITS:Int = 4
const val WORKER_ID_BITS:Int = 4
const val SEQUENCE_BITS:Int = 12

const val MAX_CLOCK_BACK:Long = -1L xor (-1L shl CLOCK_BACK_BITS)
const val MAX_DATACENTER:Long = -1L xor (-1L shl DATACENTER_ID_BITS)
const val MAX_WORKER:Long = -1L xor (-1L shl WORKER_ID_BITS)
const val MAX_SEQUENCE:Long = -1L xor (-1L shl SEQUENCE_BITS)

const val CLOCK_BACK_SHIFT = SEQUENCE_BITS //12
const val DATACENTER_ID_SHIFT = CLOCK_BACK_BITS + CLOCK_BACK_SHIFT  //14
const val WORKER_ID_SHIFT = DATACENTER_ID_BITS + DATACENTER_ID_SHIFT  //18
const val TIMESTAMP_SHIFT = WORKER_ID_BITS + WORKER_ID_SHIFT  //22

const val SNOWFLAKE_REDIS_HASH = "snowflake_hash_map"
const val WORKER_ID_REDIS_KEY = "work_id_key_map"