package com.ma2k8.looker.looker.user

import com.ma2k8.looker.operator.OperatorId

import java.util.TimeZone

case class LookerExternalUserId(operatorId: OperatorId, role: LookerRole, tz: TimeZone) {
  val value: String = s"${operatorId.value}_${role.value}_${tz.getID}"
}
