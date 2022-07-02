package pro.ftnl.core.extentions

import net.dv8tion.jda.api.entities.Member
import pro.ftnl.core.QPointAPI

fun Member.addQpoint(qpoint: Int) = QPointAPI().addQpoints(qpoint, listOf(this.id))
fun Member.removeQpoint(qpoint: Int) = QPointAPI().removeQpoints(qpoint, listOf(this.id))
fun Member.setQpoint(qpoint: Int) = QPointAPI().setQpoints(qpoint, listOf(this.id))
fun Member.getMemberData() = QPointAPI().getMemberData(this.id)