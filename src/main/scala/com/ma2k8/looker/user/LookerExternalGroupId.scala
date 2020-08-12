package com.ma2k8.looker.looker.user

import com.ma2k8.looker.provider.ProviderId

case class LookerExternalGroupId(providerId: ProviderId, role: LookerRole) {
  val value: String            = s"${providerId.value}"
  def toProviderId: ProviderId = ProviderId(value)
}
