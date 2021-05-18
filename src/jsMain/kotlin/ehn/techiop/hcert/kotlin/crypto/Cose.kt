package ehn.techiop.hcert.kotlin.crypto

import Asn1js.fromBER
import Buffer
import Hash
import ehn.techiop.hcert.kotlin.chain.fromBase64
import ehn.techiop.hcert.kotlin.chain.toByteArray
import ehn.techiop.hcert.kotlin.chain.toUint8Array
import ehn.techiop.hcert.kotlin.trust.ContentType
import ehn.techiop.hcert.kotlin.trust.TrustedCertificate
import ehn.techiop.hcert.kotlin.trust.TrustedCertificateV2
import kotlinx.datetime.Instant
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import pkijs.src.Time.Time
import kotlin.js.Json
import kotlin.js.Promise

internal object Cose {
    private val cose = js("require('cose-js')")

    @Suppress("UNUSED_VARIABLE")
    private fun internalVerify(
        data: dynamic,
        verifier: dynamic
    ): Uint8Array {
        val c = cose // needed for JS-magic
        return js("c.sign.verify(data, verifier)") as Uint8Array

    }

    fun verify(signedBitString: ByteArray, pubKey: PublicKey<*>): ByteArray =
        internalVerify(Buffer.from(signedBitString), pubKey.toCoseRepresentation()).toByteArray()

    @Suppress("UNUSED_VARIABLE")
    private fun internalSign(header: dynamic, data: dynamic, signer: dynamic): Promise<ByteArray> {
        val c = cose // needed for JS-magic
        return (js("c.sign.create(header, data, signer)") as Promise<ByteArray>)
    }

    fun sign(header: Json, input: ByteArray, privateKey: PrivateKey<*>) =
        internalSign(header, input, privateKey.toCoseRepresentation()).then { it }
}


class CoseEcKey(xC: dynamic, yC: dynamic) {
    constructor(x: ByteArray, y: ByteArray) : this(
        xC = Buffer.from(x.toUint8Array()),
        yC = Buffer.from(y.toUint8Array())
    )

    val key = Holder(xC, yC)

    class Holder(val x: dynamic, val y: dynamic)
}

// TODO is "d" sufficient?
class CoseEcPrivateKey(d: ByteArray) {
    val key = Holder(Buffer.from(d.toUint8Array()))

    class Holder(val d: dynamic)
}

class CoseJsEcPubKey(val xCoord: dynamic, val yCoord: dynamic, override val curve: CurveIdentifier) :
    EcPubKey<dynamic> {
    constructor(x: ByteArray, y: ByteArray, curve: CurveIdentifier) : this(
        xCoord = Buffer.from(x.toUint8Array()),
        yCoord = Buffer.from(y.toUint8Array()),
        curve = curve
    )

    override fun toCoseRepresentation() = CoseEcKey(xC = xCoord, yC = yCoord)
}

class CoseJsPrivateKey(val d: ByteArray, val curve: CurveIdentifier) : PrivateKey<dynamic> {
    override fun toCoseRepresentation() = CoseEcPrivateKey(d)
}

class JsCertificate(val encoded: ByteArray) : Certificate<dynamic> {

    constructor(pem: String) : this(
        pem.lines().let { it.dropLast(1).drop(1) }.joinToString(separator = "").fromBase64()
    )

    private val cert = Uint8Array(encoded.toTypedArray()).let { bytes ->
        fromBER(bytes.buffer).result.let { pkijs.src.Certificate.Certificate(js("({'schema':it})")) }
    }


    override fun getValidContentTypes(): List<ContentType> {
        //TODO
        return listOf()
    }

    override fun getValidFrom(): Instant {
        val date = (cert.notBefore as Time).value
        return Instant.parse(date.toISOString())
    }

    override fun getValidUntil(): Instant {
        val date = (cert.notAfter as Time).value
        return Instant.parse(date.toISOString())
    }

    override fun getPublicKey(): PublicKey<*> {
        val keyInfo = (cert.subjectPublicKeyInfo as Json)["parsedKey"] as Json
        val x = keyInfo["x"]
        val y = keyInfo["y"]
        return CoseJsEcPubKey(
            xCoord = Uint8Array(buffer = x as ArrayBuffer),
            yCoord = Uint8Array(buffer = y as ArrayBuffer),
            curve = CurveIdentifier.P256
        )
    }

    override fun toTrustedCertificate(): TrustedCertificate {
        return TrustedCertificateV2(calcKid(), encoded)
    }

    override fun calcKid(): ByteArray {
        val hash = Hash()
        hash.update(encoded.toUint8Array())
        return hash.digest().toByteArray().copyOf(8)
    }
}
