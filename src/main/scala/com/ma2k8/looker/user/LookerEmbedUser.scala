package com.ma2k8.looker.looker.user

import com.ma2k8.looker.operator.OperatorId
import com.ma2k8.looker.provider.ProviderId

import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
  *
  * @param externalUserId   Lookerを埋め込むアプリケーション内のユーザーの一意の識別子
  * @param firstName        ユーザーの名。空白のままにfirst_nameすると、最後のリクエストの値が保持されます。名が設定されていない場合は「埋め込み」されます
  * @param lastName         ユーザーの姓。空白のままにlast_nameすると、最後のリクエストの値が保持されます。または、姓が設定されていない場合は「埋め込み」されます
  * @param sessionLength    ユーザーがLookerにログインしたままにする必要がある秒数。0〜2,592,000秒（30日）の間です。
  *
  * @param permissions      ユーザーが持つべき許可のリスト。
  * @param models           ユーザーがアクセスできるモデル名のリスト。
  * @param groupIds         ユーザーがメンバーである必要があるLookerグループのリスト（ある場合）。グループ名の代わりにグループIDを使用します。
  * @param externalGroupId  必要に応じて、Lookerを埋め込むアプリケーションでユーザーが属するグループの一意の識別子。
  *                         コンテンツを保存し、外部グループIDを共有する権限を持つユーザーは、「グループ」と呼ばれる共有Lookerフォルダーでコンテンツを
  *                         保存および編集できます。
  *
  * @param userAttributes   ユーザーが持つ必要があるユーザー属性のリスト（ある場合）。
  *                         ユーザー属性名とそれに続くユーザー属性値のリストが含まれます。
  *
  * @param userTimezone     ユーザー固有のタイムゾーンを有効にした場合、
  *                         これにより、組み込みのLookまたはダッシュボードの[ タイムゾーン]ドロップダウンにある[ビューアタイムゾーン]オプションの値が設定されます。
  *                         このパラメーターは、コンテンツが表示されるタイムゾーンを直接変更しません。ユーザーはドロップダウンから希望のタイムゾーンを選択する必要があります。
  *                         タイムゾーンのページで有効な値を参照してください
  */
case class LookerEmbedUser(
  externalUserId: LookerExternalUserId,
  firstName: Option[String],
  lastName: Option[String],
  sessionLength: Long,
  permissions: Seq[LookerUserPermission],
  models: Seq[LookerModel],
  groupIds: Seq[LookerGroupId],
  externalGroupId: LookerExternalGroupId,
  userAttributes: Seq[LookerUserAttributes],
  userTimezone: TimeZone
) {

  // Looker 3.10以降非推奨とのことだが、パラメーターには含める必要がある。
  // Document(https://docs.looker.com/reference/embedding/sso-embed)にある通り空のプレースホルダーを入れておく。
  val accessFilters = "{}"

  // 最初にログインした権限を引きずってしまう事象を確認したのでtrueにする
  val forceLogoutLogin = true

  import io.circe.syntax._
  val jsonStrExternalUserId   = externalUserId.value.asJson.noSpaces
  val jsonStrFirstName        = firstName.asJson.noSpaces
  val jsonStrLastName         = lastName.asJson.noSpaces
  val jsonStrSessionLength    = sessionLength.asJson.noSpaces
  val jsonStrForceLogoutLogin = forceLogoutLogin.asJson.noSpaces
  val jsonStrPermissions      = permissions.map(_.value).asJson.noSpaces
  val jsonStrModels           = models.map(_.value).asJson.noSpaces
  val jsonStrGroupIds         = groupIds.map(_.value).asJson.noSpaces
  val jsonStrExternalGroupId  = externalGroupId.value.asJson.noSpaces
  val jsonStrUserAttributes   = userAttributes.flatMap(_.toMap).toMap.asJson.noSpaces
  val jsonStrUserTimezone     = userTimezone.getID.asJson.noSpaces
}

object LookerEmbedUser {

  def applySimpleLooker(
    providerId: ProviderId,
    operatorId: OperatorId,
    models: Seq[LookerModel],
    userAttributes: Seq[LookerUserAttributes],
    lookerGroupIds: Seq[LookerGroupId],
    userTimezone: TimeZone
  ): LookerEmbedUser = {
    val role = LookerRole.SimpleViewer
    LookerEmbedUser(
      externalUserId = LookerExternalUserId(operatorId, role, userTimezone),
      firstName = None,
      lastName = None,
      sessionLength = TimeUnit.DAYS.toSeconds(1),
      permissions = role.permission,
      models = models,
      groupIds = lookerGroupIds,
      externalGroupId = LookerExternalGroupId(providerId, role),
      userAttributes = userAttributes,
      userTimezone = userTimezone
    )
  }

  def applySimpleExploler(
    providerId: ProviderId,
    operatorId: OperatorId,
    models: Seq[LookerModel],
    userAttributes: Seq[LookerUserAttributes],
    lookerGroupIds: Seq[LookerGroupId],
    userTimezone: TimeZone
  ): LookerEmbedUser = {
    val role = LookerRole.SimpleExplorer
    LookerEmbedUser(
      externalUserId = LookerExternalUserId(operatorId, role, userTimezone),
      firstName = None,
      lastName = None,
      sessionLength = TimeUnit.DAYS.toSeconds(1),
      permissions = role.permission,
      models = models,
      groupIds = lookerGroupIds,
      externalGroupId = LookerExternalGroupId(providerId, role),
      userAttributes = userAttributes,
      userTimezone = userTimezone
    )
  }
}
