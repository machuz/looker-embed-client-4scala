package com.ma2k8.looker.looker.user

import com.ma2k8.looker.provider.{ ProviderId => DomainProviderId }

sealed abstract class LookerUserAttributes(val value: String) {
  def toMap: Map[String, String]
}

object LookerUserAttributes {

  case class ProviderId(underlay: DomainProviderId) extends LookerUserAttributes("provider_id") {
    override def toMap: Map[String, String] = Map(value -> underlay.value)
  }

  case class Locale(underlay: java.util.Locale) extends LookerUserAttributes("locale") {
    override def toMap: Map[String, String] = Map(value -> underlay.toString)
  }

  case class Environment(underlay: String) extends LookerUserAttributes("environment") {
    override def toMap: Map[String, String] = Map(value -> underlay)
  }

}
