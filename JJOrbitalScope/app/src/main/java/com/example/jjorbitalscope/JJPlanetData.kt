import kotlinx.serialization.Serializable

@Serializable
data class Planet(
    val type: String,
    val name: String,
    val moonCount: String,
    val distanceFromSun: String,
    val diameterKm: String,
    val satellites: List<Satellite>? = null
)

@Serializable
data class Satellite(
    val name: String,
    val diameterKm: String
)
