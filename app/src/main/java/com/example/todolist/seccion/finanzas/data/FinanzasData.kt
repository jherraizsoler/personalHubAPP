package com.example.todolist.seccion.finanzas.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

// Serializador personalizado para la clase UUID
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}

@Serializable
data class Transaccion(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val nombre: String,
    val monto: Double,
    val tipo: TransaccionTipo,
    val categoria: String,
    val fecha: Long = System.currentTimeMillis()
)

data class CategoriaFinanzas(
    val nombre: String,
    val color: Int // Usar un Int para representar un Color en Compose
)

enum class TransaccionTipo {
    INGRESO,
    GASTO
}

enum class TransaccionCategoria {
    SALARIO,
    INVERSION,
    GASTO_FIJO,
    GASTO_VARIABLE,
    OCIO
}