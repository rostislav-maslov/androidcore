package com.ub.security

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.Size
import java.nio.ByteBuffer
import java.security.Provider
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

interface AuthenticatedEncryption {
    fun encrypt(bytesToEncrypt: ByteArray, keyToEncrypt: ByteArray, associatedData: ByteArray? = null): ByteArray
    fun decrypt(bytesToDecrypt: ByteArray, keyToDecrypt: ByteArray, associatedData: ByteArray? = null): ByteArray
}

/**
 * Шифрование и дешифрование объектов с помощью алгоритма AES/GCM/NoPadding
 *
 * В случае возникновения вопросов по работе и использованию сей поделки, можно смело справшивать _сами_знаете_кого_
 *
 * @property secureRandom - служит для генерации псевдослучайных чисел при создании нового ключа
 * @property provider - служит для создания объекта [Cipher]. Может быть null, тогда используется стандартный [Provider]
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
class AesGcmEncryption(
    private val secureRandom: SecureRandom = SecureRandom(),
    private val provider: Provider? = null
) : AuthenticatedEncryption {

    /**
     * Шифрует переданные данные
     *
     * @param bytesToEncrypt - данные для шифрования
     * @param keyToEncrypt - ключ шифрования. Обязательно должен быть размером 16 байт
     * @param associatedData - опциональное поле дополнительной информацией, которая будет применена при шифровании
     */
    override fun encrypt(bytesToEncrypt: ByteArray,
                         @Size(16) keyToEncrypt: ByteArray,
                         associatedData: ByteArray?): ByteArray {
        val cipher = provider?.let { Cipher.getInstance(ALGORITHM, it) } ?: Cipher.getInstance(ALGORITHM)
        val iv = ByteArray(IV_SIZE).apply { secureRandom.nextBytes(this) }
        val parameterSpec = GCMParameterSpec(KEY_LENGTH_MEDIUM, iv)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(keyToEncrypt, "AES"), parameterSpec)
        associatedData?.let { cipher.updateAAD(it) }
        val cipherText = cipher.doFinal(bytesToEncrypt)
        val byteBuffer = ByteBuffer.allocate(4 + iv.size + cipherText.size).apply {
            putInt(iv.size)
            put(iv)
            put(cipherText)
        }
        return byteBuffer.array()
    }

    /**
     * Дешифрует переданные данные
     *
     * В процессе идет проверка на то, что в передаваемом массиве байт [bytesToDecrypt]
     * первое int-значение хранит в себе размерность initialization vector'а
     * Далее идет извлечение initialization vector'а из массива и с помощью него и [keyToDecrypt]
     * происходит расшифровка
     *
     * @param bytesToDecrypt - данные для дешифрования
     * @param keyToDecrypt - ключ дешифрования. Обязательно должен быть размером 16 байт
     * @param associatedData - опциональное поле дополнительной информацией, которая будет применена при дешифровании
     */
    override fun decrypt(bytesToDecrypt: ByteArray,
                         @Size(16) keyToDecrypt: ByteArray,
                         associatedData: ByteArray?): ByteArray {
        val cipherBuffer: ByteBuffer = ByteBuffer.wrap(bytesToDecrypt)
        val ivLength: Int = cipherBuffer.int
        require(ivLength == IV_SIZE) { "Invalid iv length. Expect $IV_SIZE, actual $ivLength" }
        val iv = ByteArray(ivLength)
        cipherBuffer.get(iv)
        val cipherText = ByteArray(cipherBuffer.remaining())
        cipherBuffer.get(cipherText)
        val cipher = provider?.let { Cipher.getInstance(ALGORITHM, it) } ?: Cipher.getInstance(ALGORITHM)
        val parameterSpec = GCMParameterSpec(KEY_LENGTH_MEDIUM, iv)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(keyToDecrypt, "AES"), parameterSpec)
        associatedData?.let { cipher.updateAAD(it) }
        return cipher.doFinal(cipherText)
    }

    companion object {
        private const val ALGORITHM = "AES/GCM/NoPadding"
        private const val IV_SIZE = 16
        const val KEY_LENGTH_MEDIUM = 128
    }
}

/**
 * Extension-функция для любого типа объектов, возвращающая экземпляр [ByteArray]
 * необходимой размерности, который нужен для создания экземпляра [SecretKeySpec]
 *
 * Процесс получению ключа:
 * 1. аллоцирует в памяти [ByteBuffer] необходмиой размерности [length]
 * 2. снимает [hashCode] от объекта
 * 3. преобразует его в строку с помощью [toString]
 * 4. создаёт на базе этой строки массив байт с помощью [toByteArray]
 * 5. записывает созданный массив байт в [ByteBuffer] из пункта 1
 * 6. преобразует [ByteBuffer] в массив байт и возвращает его
 *
 * @param length - размерность ключа. По-умолчанию имеет значение 16 бит
 */
fun Any.toSecretKey(length: Int = 16): ByteArray = ByteBuffer.allocate(length).apply {
    put(this@toSecretKey.hashCode().toString().toByteArray(charset("UTF-8")))
}.array()