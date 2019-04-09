package dev.entao.base.ex

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Created by entaoyang@163.com on 16/4/27.
 * https://my.oschina.net/Jacker/blog/86383
 * cipher:  AES/ECB/NoPadding等
 *
 * 算法/模式/填充                16字节加密后数据长度        不满16字节加密后长度
 * AES/CBC/NoPadding             16                          不支持
 * AES/CBC/PKCS5Padding          32                          16
 * AES/CBC/ISO10126Padding       32                          16
 * AES/CFB/NoPadding             16                          原始数据长度
 * AES/CFB/PKCS5Padding          32                          16
 * AES/CFB/ISO10126Padding       32                          16
 * AES/ECB/NoPadding             16                          不支持
 * AES/ECB/PKCS5Padding          32                          16
 * AES/ECB/ISO10126Padding       32                          16
 * AES/OFB/NoPadding             16                          原始数据长度
 * AES/OFB/PKCS5Padding          32                          16
 * AES/OFB/ISO10126Padding       32                          16
 * AES/PCBC/NoPadding            16                          不支持
 * AES/PCBC/PKCS5Padding         32                          16
 * AES/PCBC/ISO10126Padding      32                          16
 */
class AES(val cipher: String) {

    fun encode(key: ByteArray, data: ByteArray): ByteArray {
        return doAES(Cipher.ENCRYPT_MODE, key, data)
    }


    fun decode(key: ByteArray, data: ByteArray): ByteArray {
        return doAES(Cipher.DECRYPT_MODE, key, data)
    }

    private fun doAES(mode: Int, key: ByteArray, data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(cipher)
        cipher.init(mode, SecretKeySpec(key, "AES"))
        return cipher.doFinal(data)
    }

    companion object {
        val ECB = AES("AES/ECB/NoPadding")
        val ECB_PKCS5 = AES("AES/ECB/PKCS5Padding")
        val ECB_ISO10126 = AES("AES/ECB/ISO10126Padding")

        val CBC = AES("AES/CBC/NoPadding")
        val CBC_PKCS5 = AES("AES/CBC/PKCS5Padding")
        val CBC_ISO10126 = AES("AES/CBC/ISO10126Padding")

        val CFB = AES("AES/CFB/NoPadding")
        val CFB_PKCS5 = AES("AES/CFB/PKCS5Padding")
        val CFB_ISO10126 = AES("AES/CFB/ISO10126Padding")


        val OFB = AES("AES/OFB/NoPadding")
        val OFB_PKCS5 = AES("AES/OFB/PKCS5Padding")
        val OFB_ISO10126 = AES("AES/OFB/ISO10126Padding")

        val PCBC = AES("AES/PCBC/NoPadding")
        val PCBC_PKCS5 = AES("AES/PCBC/PKCS5Padding")
        val PCBC_ISO10126 = AES("AES/PCBC/ISO10126Padding")


    }

}
