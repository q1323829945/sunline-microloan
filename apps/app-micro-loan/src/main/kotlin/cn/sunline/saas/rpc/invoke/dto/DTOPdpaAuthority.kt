package cn.sunline.saas.rpc.invoke.dto

data class DTOPdpaAuthority(
    var isElectronicSignature: Boolean = false,
    var isFaceRecognition : Boolean = false,
    var isFingerprint: Boolean = false,
)