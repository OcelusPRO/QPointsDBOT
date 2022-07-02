package pro.ftnl

import pro.ftnl.core.Bot
import pro.ftnl.core.Configuration
import java.io.File

val CONFIG = Configuration.loadConfiguration(File("./config.json"))


object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        Bot(CONFIG.token, listOf(  )).start()
    }
}