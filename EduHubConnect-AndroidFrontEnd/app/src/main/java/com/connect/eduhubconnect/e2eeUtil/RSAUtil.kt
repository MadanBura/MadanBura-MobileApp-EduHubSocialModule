import android.content.Context
import android.util.Base64
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


object RSAUtil {
    fun generateKeyPair(): KeyPair {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        return generator.generateKeyPair()
    }

    fun encrypt(data: ByteArray, publicKey: PublicKey): ByteArray {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher.doFinal(data)
    }

    fun decrypt(encrypted: ByteArray, privateKey: PrivateKey): ByteArray {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return cipher.doFinal(encrypted)
    }

    fun saveKeyPairToKeystore(context: Context, keyPair: KeyPair) {
        val prefs = context.getSharedPreferences("keys", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString("privateKey", Base64.encodeToString(keyPair.private.encoded, Base64.DEFAULT))
            putString("publicKey", Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT))
            apply()
        }
    }

    fun loadKeyPair(context: Context): KeyPair? {
        val prefs = context.getSharedPreferences("keys", Context.MODE_PRIVATE)
        val privateEncoded = prefs.getString("privateKey", null)
        val publicEncoded = prefs.getString("publicKey", null)
        if (privateEncoded != null && publicEncoded != null) {
            val keyFactory = KeyFactory.getInstance("RSA")
            val privateSpec = PKCS8EncodedKeySpec(Base64.decode(privateEncoded, Base64.DEFAULT))
            val publicSpec = X509EncodedKeySpec(Base64.decode(publicEncoded, Base64.DEFAULT))
            val privateKey = keyFactory.generatePrivate(privateSpec)
            val publicKey = keyFactory.generatePublic(publicSpec)
            return KeyPair(publicKey, privateKey)
        }
        return null
    }
}
