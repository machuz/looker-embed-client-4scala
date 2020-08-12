package com.ma2k8.looker.looker.user

import com.typesafe.scalalogging.LazyLogging

sealed abstract class LookerUserPermission(val value: String)

/**
  * Doc: https://docs.looker.com/reference/embedding/sso-embed
  */
object LookerUserPermission extends LazyLogging {
  case object ACCESS_DATA                   extends LookerUserPermission("access_data")
  case object SEE_LOOKML_DASHBOARDS         extends LookerUserPermission("see_lookml_dashboards")
  case object SEE_LOOKS                     extends LookerUserPermission("see_looks")
  case object SEE_USER_DASHBOARDS           extends LookerUserPermission("see_user_dashboards")
  case object EXPLORE                       extends LookerUserPermission("explore")
  case object CREATE_TABLE_CALCULATIONS     extends LookerUserPermission("create_table_calculations")
  case object SAVE_CONTENT                  extends LookerUserPermission("save_content")
  case object SEND_OUTGOING_WEBHOOK         extends LookerUserPermission("send_outgoing_webhook")
  case object SEND_TO_S3                    extends LookerUserPermission("send_to_s3")
  case object SEND_TO_SFTP                  extends LookerUserPermission("send_to_sftp")
  case object SCHEDULE_LOOK_EMAILS          extends LookerUserPermission("schedule_look_emails")
  case object SCHEDULE_EXTERNAL_LOOK_EMAILS extends LookerUserPermission("schedule_external_look_emails")
  case object SEND_TO_INTEGRATION           extends LookerUserPermission("send_to_integration")
  case object CREATE_ALERTS                 extends LookerUserPermission("create_alerts")
  case object DOWNLOAD_WITH_LIMIT           extends LookerUserPermission("download_with_limit")
  case object DOWNLOAD_WITHOUT_LIMIT        extends LookerUserPermission("download_without_limit")
  case object SEE_SQL                       extends LookerUserPermission("see_sql")
  case object SEE_DRILL_OVERLAY             extends LookerUserPermission("see_drill_overlay")
  case object EMBED_BROWSE_SPACES           extends LookerUserPermission("embed_browse_spaces")
  case object MANAGE_SPACES                 extends LookerUserPermission("manage_spaces")

  lazy val values = Seq(
    ACCESS_DATA,
    SEE_LOOKML_DASHBOARDS,
    SEE_LOOKS,
    SEE_USER_DASHBOARDS,
    EXPLORE,
    CREATE_TABLE_CALCULATIONS,
    SAVE_CONTENT,
    SEND_OUTGOING_WEBHOOK,
    SEND_TO_S3,
    SEND_TO_SFTP,
    SCHEDULE_LOOK_EMAILS,
    SCHEDULE_EXTERNAL_LOOK_EMAILS,
    SEND_TO_INTEGRATION,
    CREATE_ALERTS,
    DOWNLOAD_WITH_LIMIT,
    DOWNLOAD_WITHOUT_LIMIT,
    SEE_SQL,
    SEE_DRILL_OVERLAY,
    EMBED_BROWSE_SPACES
  )

  def valueOf(value: String): LookerUserPermission = {
    map.getOrElse(value, throw new IllegalArgumentException(s"no such Prefecture[$value]"))
  }
  private[this] val map: Map[String, LookerUserPermission] =
    values.map(p => p.value -> p).toMap

  def apply(value: String): LookerUserPermission = valueOf(value)
}
