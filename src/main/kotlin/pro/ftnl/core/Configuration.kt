package pro.ftnl.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

class ConfigurationException(message: String) : Exception(message)

enum class StatusType { PLAYING, LISTENING, WATCHING, STREAMING }
data class Status(
    val name: String = "",
    val url: String? = null,
    val type: StatusType = StatusType.LISTENING,
)


data class Configuration(
    val token: String = "Insert token here",
    val defaultStatus: Status = Status(),
) {
    companion object {
        fun loadConfiguration(file: File): Configuration {
            if (file.createNewFile()) {
                val config = Configuration()
                file.writeText(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(config))
                throw ConfigurationException("Veuillez remplir le fichier de configuration")
            }
            return try {
                val cfg = Gson().fromJson(file.readText(), Configuration::class.java)
                file.writeText(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(cfg))
                cfg
            } catch (e: Exception) {
                throw ConfigurationException("La configuration n'est pas valide")
            }
        }
    }
}
