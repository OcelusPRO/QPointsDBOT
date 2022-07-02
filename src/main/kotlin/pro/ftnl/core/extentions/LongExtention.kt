package pro.ftnl.core

import java.time.Duration

fun Long.toHumanTime(): String {
    var duration = Duration.ofMillis(this)
    val builder = StringBuilder()
    if (duration.toDays() >= 1) {
        builder.append("`${duration.toDays()}` jour(s) ")
        duration -= Duration.ofDays(duration.toDays())
    }
    if (duration.toHours() >= 1) {
        builder.append("`${duration.toHours()}` heure(s) ")
        duration -= Duration.ofHours(duration.toHours())
    }
    if (duration.toMinutes() >= 1) {
        builder.append("`${duration.toMinutes()}` minute(s) ")
        duration -= Duration.ofMinutes(duration.toMinutes())
    }
    if (duration.toSeconds() >= 1) {
        builder.append("`${duration.toSeconds()}` seconde(s) ")
        duration -= Duration.ofSeconds(duration.toSeconds())
    }
    return builder.toString()
}