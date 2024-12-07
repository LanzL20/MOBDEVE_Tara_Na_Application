package com.mobdeve.s13.lim.pacheco.tan.tarana

import java.security.SecureRandom

object PasswordHashing {
    //function to encrypt password use PBKDF2WithHmacSHA1
    private const val PBKDF2_ITERATIONS = 1000
    private const val PBKDF2_KEY_LENGTH = 24
    private const val SALT_LENGTH = 24

    public fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        random.nextBytes(salt)
        return salt
    }

    public fun hashPassword(password: String, salt: ByteArray): ByteArray {
        val pbkdf2 = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val spec = javax.crypto.spec.PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, PBKDF2_KEY_LENGTH * 8)
        return pbkdf2.generateSecret(spec).encoded
    }

    public fun verifyPassword(password: String, salt: ByteArray, expectedHash: ByteArray): Boolean {
        val actualHash = hashPassword(password, salt)
        return actualHash.contentEquals(expectedHash)
    }

    public fun byteArrayToHexString(array: ByteArray): String {
        val hexString = StringBuilder(2 * array.size)
        for (byte in array) {
            val hex = Integer.toHexString(0xff and byte.toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        return hexString.toString()
    }

    public fun hexStringToByteArray(hexString: String): ByteArray {
        val bytes = ByteArray(hexString.length / 2)
        for (i in 0 until hexString.length step 2) {
            bytes[i / 2] = hexString.substring(i, i + 2).toInt(16).toByte()
        }
        return bytes
    }
}