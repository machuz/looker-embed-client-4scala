package com.ma2k8.looker.looker

sealed abstract class LookerEmbedUrlPath(val value: String) {
  val urlPath: String
}

object LookerEmbedUrlPath {

  case class EmbedLookUrlPath(id: Int) extends LookerEmbedUrlPath("look") {
    val urlPath: String = s"/embed/looks/$id"
  }

  case class EmbedDashboardUrlPath(id: String) extends LookerEmbedUrlPath("dashboard") {
    val urlPath: String = s"/embed/dashboards/$id"
  }

}
