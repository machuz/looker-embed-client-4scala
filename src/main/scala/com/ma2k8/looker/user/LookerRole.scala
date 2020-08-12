package com.ma2k8.looker.looker.user

import com.ma2k8.looker.looker.user.LookerUserPermission._

sealed abstract class LookerRole(val value: String) {
  val permission: Seq[LookerUserPermission]
}

object LookerRole {
  case object SimpleViewer extends LookerRole("simple_viewer") {
    override val permission: Seq[LookerUserPermission] = Seq(
      ACCESS_DATA,
      SEE_LOOKS,
      SEE_LOOKML_DASHBOARDS,
      SEE_USER_DASHBOARDS,
      DOWNLOAD_WITH_LIMIT
    )
  }

  case object SimpleExplorer extends LookerRole("simple_explorer") {
    override val permission: Seq[LookerUserPermission] = Seq(
      ACCESS_DATA,
      SEE_LOOKS,
      SEE_LOOKML_DASHBOARDS,
      SEE_USER_DASHBOARDS,
      DOWNLOAD_WITH_LIMIT,
      EXPLORE,
      SEE_DRILL_OVERLAY,
      EMBED_BROWSE_SPACES,
      MANAGE_SPACES,
      SCHEDULE_LOOK_EMAILS,
      SEE_SQL
    )
  }

}
