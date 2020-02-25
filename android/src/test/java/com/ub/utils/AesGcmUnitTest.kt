package com.ub.utils

import com.ub.security.AesGcmEncryption
import com.ub.security.AuthenticatedEncryption
import com.ub.security.toSecretKey
import org.junit.Assert.*
import org.junit.Test
import java.nio.ByteBuffer
import java.security.InvalidKeyException
import javax.crypto.AEADBadTagException

class AesGcmUnitTest {

    @Test
    fun secretKeyCreationTest() {
        val secretKey = toSecretKey()
        assertTrue(secretKey.firstOrNull { it != 0.toByte() } != null)
    }

    @Test
    fun secretKeyCreationNegativeEqualityTest() {
        val secretKeyFromTestClass = toSecretKey()
        val secretKeyFromBigString = "testStringWithBigLengthSizeForSecretKeyCreation".toSecretKey()
        assertNotEquals(secretKeyFromBigString, secretKeyFromTestClass)
    }

    @Test
    fun secretKeyEqualityTest() {
        val secretKeyFromTestClass = toSecretKey()
        val secretKeyFromEqualsTestClass = toSecretKey()
        val secretKeyFromBigString = "testStringWithBigLengthSizeForSecretKeyCreation".toSecretKey()
        val secretKeyFromEqualsBigString = "testStringWithBigLengthSizeForSecretKeyCreation".toSecretKey()
        assertTrue(secretKeyFromTestClass.contentEquals(secretKeyFromEqualsTestClass))
        assertTrue(secretKeyFromBigString.contentEquals(secretKeyFromEqualsBigString))
    }

    @Test
    fun encryptionSuccessEmptyMessageTest() {
        val messageToEncrypt = ""
        val messageToEncryptInBytes = messageToEncrypt.toByteArray(charset("UTF-8"))
        val keyToEncrypt = toSecretKey()
        val encryption: AuthenticatedEncryption = AesGcmEncryption()
        val encryptionBytes = encryption.encrypt(
            messageToEncryptInBytes,
            keyToEncrypt
        )
        assertEquals(messageToEncryptInBytes.size + 16 + 4 + keyToEncrypt.size, encryptionBytes.size)
    }

    @Test
    fun encryptionSuccessTest() {
        val messageToEncrypt = "This is test message"
        val messageToEncryptInBytes = messageToEncrypt.toByteArray(charset("UTF-8"))
        val keyToEncrypt = toSecretKey()
        val encryption: AuthenticatedEncryption = AesGcmEncryption()
        val encryptionBytes = encryption.encrypt(
            messageToEncryptInBytes,
            keyToEncrypt
        )
        assertEquals(messageToEncryptInBytes.size + 16 + 4 + keyToEncrypt.size, encryptionBytes.size)
    }

    @Test
    fun encryptionSuccessWithAADTest() {
        val messageToEncrypt = "This is test message"
        val messageToEncryptInBytes = messageToEncrypt.toByteArray(charset("UTF-8"))
        val associatedData = "Optional message"
        val associatedDataInBytes = associatedData.toByteArray(charset("UTF-8"))
        val keyToEncrypt = toSecretKey()
        val encryption: AuthenticatedEncryption = AesGcmEncryption()
        val encryptionBytes = encryption.encrypt(
            messageToEncryptInBytes,
            keyToEncrypt,
            associatedDataInBytes
        )
        assertEquals(messageToEncryptInBytes.size + 16 + 4 + keyToEncrypt.size, encryptionBytes.size)
    }

    @Test(expected = InvalidKeyException::class)
    fun encryptionFailSecretKeyTest() {
        val messageToEncrypt = "This is test message"
        val associatedData = "Optional message"
        val keyToEncrypt = "test".toByteArray(charset("UTF-8"))
        val encryption: AuthenticatedEncryption = AesGcmEncryption()
        encryption.encrypt(
            messageToEncrypt.toByteArray(charset("UTF-8")),
            keyToEncrypt,
            associatedData.toByteArray(charset("UTF-8"))
        )
    }

    @Test
    fun decryptionSuccessTest() {
        val messageToEncrypt = "This is test message"
        val keyToEncrypt = toSecretKey()
        val encryption: AuthenticatedEncryption = AesGcmEncryption()
        val encryptionBytes = encryption.encrypt(
            messageToEncrypt.toByteArray(charset("UTF-8")),
            keyToEncrypt
        )
        val decryption: AuthenticatedEncryption = AesGcmEncryption()
        val keyToDecrypt = toSecretKey()
        val decryptedBytes = decryption.decrypt(
            encryptionBytes,
            keyToDecrypt
        )
        assertEquals(messageToEncrypt, String(decryptedBytes))
    }

    @Test(expected = AEADBadTagException::class)
    fun decryptionFailInvalidSecretKeyTest() {
        val messageToEncrypt = "This is test message"
        val keyToEncrypt = toSecretKey()
        val encryption: AuthenticatedEncryption = AesGcmEncryption()
        val encryptionBytes = encryption.encrypt(
            messageToEncrypt.toByteArray(charset("UTF-8")),
            keyToEncrypt
        )
        val decryption: AuthenticatedEncryption = AesGcmEncryption()
        val keyToDecrypt = "anotherTag".toSecretKey()
        decryption.decrypt(
            encryptionBytes,
            keyToDecrypt
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun decryptionFailInvalidEncryptedMessageTest() {
        val messageToEncrypt = "This is test message"
        val decryption: AuthenticatedEncryption = AesGcmEncryption()
        val keyToDecrypt = "anotherTag".toSecretKey()
        decryption.decrypt(
            messageToEncrypt.toByteArray(charset("UTF-8")),
            keyToDecrypt
        )
    }

    @Test(expected = AEADBadTagException::class)
    fun decryptionFailInvalidEncryptedMessageWithCorrectIVLengthTest() {
        val messageToEncrypt = "This is test message"
        val messageToEncryptInBytes = messageToEncrypt.toByteArray(charset("UTF-8"))
        val messageToEncryptInBytesWithIv = ByteBuffer.allocate(messageToEncryptInBytes.size * 2 + 4).apply {
            putInt(16)
            put(messageToEncryptInBytes)
            put(messageToEncryptInBytes)
        }.array()
        val decryption: AuthenticatedEncryption = AesGcmEncryption()
        val keyToDecrypt = "tag".toSecretKey()
        decryption.decrypt(
            messageToEncryptInBytesWithIv,
            keyToDecrypt
        )
    }
}