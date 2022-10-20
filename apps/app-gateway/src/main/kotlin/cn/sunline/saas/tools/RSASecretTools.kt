package cn.sunline.saas.tools

import mu.KotlinLogging
import org.apache.tomcat.util.codec.binary.Base64
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


object RSASecretTools {
    private var logger = KotlinLogging.logger {  }

    data class KeyStore(
        val accessKey: String,
        val secretKey: String
    )

    fun generatorKey(): KeyStore {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(512)
        val keyPair = keyPairGenerator.genKeyPair()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey
        val accessKey = String(Base64.encodeBase64(publicKey.encoded))
        val secretKey = String(Base64.encodeBase64(privateKey.encoded))
        return KeyStore(accessKey, secretKey)
    }

    private fun encrypt(accessKey:String, value:String):String?{
        return try {
            val decode = Base64.decodeBase64(accessKey)
            val publicKey = KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(decode)) as RSAPublicKey
            val cipher = Cipher.getInstance("RSA")
            cipher.init(Cipher.ENCRYPT_MODE,publicKey)
            Base64.encodeBase64String(cipher.doFinal(value.toByteArray(Charsets.UTF_8)))
        } catch (e:Exception){
            logger.error { "encode error!!" }
            logger.error { e.localizedMessage }
            null
        }
    }

    private fun decrypt(secretKey: String, value:String):String?{
        return try {
            val bytes = Base64.decodeBase64(value.toByteArray(Charsets.UTF_8))
            val decode = Base64.decodeBase64(secretKey)
            val privateKey = KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(decode)) as RSAPrivateKey
            val cipher = Cipher.getInstance("RSA")
            cipher.init(Cipher.DECRYPT_MODE,privateKey)
            String(cipher.doFinal(bytes))
        } catch (e:Exception){
            logger.error { "decode error!!" }
            logger.error { e.localizedMessage }
            null
        }

    }

    fun checkKey(accessKey: String,secretKey: String,value:String):Boolean{
        val encode = encrypt(accessKey,value) ?: return false
        val decode = decrypt(secretKey,encode) ?: return false
        return value == decode
    }
}
