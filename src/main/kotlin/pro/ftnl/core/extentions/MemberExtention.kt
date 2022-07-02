package pro.ftnl.core.extentions

import net.dv8tion.jda.api.entities.Member
import pro.ftnl.core.QPointAPI

fun Member.addQPoint(qPoints: Int) = QPointAPI().addQpoints(qPoints, listOf(this.id))
fun Member.removeQPoint(qPoints: Int) = QPointAPI().removeQpoints(qPoints, listOf(this.id))
fun Member.setQPoint(qPoints: Int) = QPointAPI().setQpoints(qPoints, listOf(this.id))
fun Member.getMemberData() = QPointAPI().getMemberData(this.id)