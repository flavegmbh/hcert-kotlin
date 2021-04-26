package ehn.techiop.hcert.kotlin.data

import ehn.techiop.hcert.data.Eudgc
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class GreenCertificate(
    @SerialName("ver")
    val schemaVersion: String,

    @SerialName("nam")
    val subject: Person,

    @SerialName("dob")
    @Serializable(with = LocalDateSerializer::class)
    val dateOfBirth: LocalDate,

    @SerialName("v")
    val vaccinations: List<Vaccination?>? = listOf(),

    @SerialName("r")
    val recoveryStatements: List<RecoveryStatement?>? = listOf(),

    @SerialName("t")
    val tests: List<Test?>? = listOf(),
) {
    companion object {
        @JvmStatic
        fun fromEuSchema(input: Eudgc): GreenCertificate? {
            if (input.nam == null || input.ver == null || input.dob == null) {
                return null
            }
            return try {
                GreenCertificate(
                    subject = Person.fromEuSchema(input.nam),
                    vaccinations = input.v?.let { it.map { Vaccination.fromEuSchema(it) } },
                    tests = input.t?.let { it.map { Test.fromEuSchema(it) } },
                    recoveryStatements = input.r?.let { it.map { RecoveryStatement.fromEuSchema(it) } },
                    dateOfBirth = LocalDate.parse(input.dob, DateTimeFormatter.ISO_DATE),
                    schemaVersion = input.ver,
                )
            } catch (e: Throwable) {
                null
            }
        }
    }
}