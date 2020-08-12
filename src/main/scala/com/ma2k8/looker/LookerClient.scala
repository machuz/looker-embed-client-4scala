package com.ma2k8.looker.looker

import com.ma2k8.looker.config.{ DashboardWebConf, LookerConfig }
import com.ma2k8.looker.looker.user.LookerEmbedUser
import com.ma2k8.looker.dddSupport.PpError
import com.ma2k8.looker.eff.myEff._
import com.ma2k8.looker.eff.util.clock.java8.ClockM

import org.atnos.eff.Eff

import javax.inject.Inject

import java.math.BigInteger
import java.net.URL
import java.security.SecureRandom
import java.time.ZonedDateTime
import java.nio.charset.StandardCharsets

import scala.util.control.NonFatal

class LookerClient @Inject() (
  lookerConfig: LookerConfig,
  webConf: webConf
) {
  def createEmbedURL[R: _clockm: _ppErrorEither](
    lookerEmbedUser: LookerEmbedUser,
    targetLookerEmbedUrlPath: LookerEmbedUrlPath
  ): Eff[R, URL] = {
    for {
      now <- ClockM.zonedNow[R]()
      url <- fromPpError[R, URL] {
        try {
          Right(genEmbedUrl(lookerEmbedUser, targetLookerEmbedUrlPath, now))
        } catch {
          case NonFatal(e) => Left(PpError.LookerError(e))
        }
      }
    } yield url
  }

  private def genEmbedUrl(
    lookerEmbedUser: LookerEmbedUser,
    targetLookerEmbedUrlPath: LookerEmbedUrlPath,
    now: ZonedDateTime
  ) = {
    val path = "/login/embed/" + java.net.URLEncoder
      .encode(
        s"${targetLookerEmbedUrlPath.urlPath}?embed_domain=${webConf.baseUrl}",
        StandardCharsets.UTF_8.displayName()
      )

    val random = new SecureRandom()
    import io.circe.syntax._
    val nonce = new BigInteger(130, random).toString(32).asJson.noSpaces
    val time  = now.toEpochSecond.toString

    // Order of these here is very important!
    val urlToSign =
      s"""${lookerConfig.lookerHost}
         |$path
         |$nonce
         |$time
         |${lookerEmbedUser.jsonStrSessionLength}
         |${lookerEmbedUser.jsonStrExternalUserId}
         |${lookerEmbedUser.jsonStrPermissions}
         |${lookerEmbedUser.jsonStrModels}
         |${lookerEmbedUser.jsonStrGroupIds}
         |${lookerEmbedUser.jsonStrExternalGroupId}
         |${lookerEmbedUser.jsonStrUserAttributes}
         |${lookerEmbedUser.accessFilters}""".stripMargin // MEMO: 末尾に改行を入れるとsignatureが壊れる

    val signature = encodeString(urlToSign, lookerConfig.lookerSecret)

    val signedURL = s"nonce=${java.net.URLEncoder.encode(nonce, StandardCharsets.UTF_8.displayName())}" +
      s"&time=${java.net.URLEncoder.encode(time, StandardCharsets.UTF_8.displayName())}" +
      s"&session_length=${java.net.URLEncoder
        .encode(lookerEmbedUser.jsonStrSessionLength, StandardCharsets.UTF_8.displayName())}" +
      s"&external_user_id=${java.net.URLEncoder
        .encode(lookerEmbedUser.jsonStrExternalUserId, StandardCharsets.UTF_8.displayName())}" +
      s"&permissions=${java.net.URLEncoder.encode(lookerEmbedUser.jsonStrPermissions, StandardCharsets.UTF_8.displayName())}" +
      s"&models=${java.net.URLEncoder.encode(lookerEmbedUser.jsonStrModels, StandardCharsets.UTF_8.displayName())}" +
      s"&access_filters=${java.net.URLEncoder.encode(lookerEmbedUser.accessFilters, StandardCharsets.UTF_8.displayName())}" +
      s"&signature=${java.net.URLEncoder.encode(signature, StandardCharsets.UTF_8.displayName())}" +
      s"&first_name=${java.net.URLEncoder.encode(lookerEmbedUser.jsonStrFirstName, StandardCharsets.UTF_8.displayName())}" +
      s"&last_name=${java.net.URLEncoder.encode(lookerEmbedUser.jsonStrLastName, StandardCharsets.UTF_8.displayName())}" +
      s"&group_ids=${java.net.URLEncoder.encode(lookerEmbedUser.jsonStrGroupIds, StandardCharsets.UTF_8.displayName())}" +
      s"&external_group_id=${java.net.URLEncoder
        .encode(lookerEmbedUser.jsonStrExternalGroupId, StandardCharsets.UTF_8.displayName())}" +
      s"&user_attributes=${java.net.URLEncoder
        .encode(lookerEmbedUser.jsonStrUserAttributes, StandardCharsets.UTF_8.displayName())}" +
      s"&user_timezone=${java.net.URLEncoder
        .encode(lookerEmbedUser.jsonStrUserTimezone, StandardCharsets.UTF_8.displayName())}" +
      s"&force_logout_login=${java.net.URLEncoder
        .encode(lookerEmbedUser.jsonStrForceLogoutLogin, StandardCharsets.UTF_8.displayName())}"

    new URL(s"https://${lookerConfig.lookerHost}$path?$signedURL")
  }

  private def encodeString(stringToEncode: String, secret: String): String = {
    import javax.crypto.Mac
    import javax.crypto.spec.SecretKeySpec
    import java.util.Base64

    val keyBytes   = secret.getBytes(StandardCharsets.UTF_8.displayName())
    val signingKey = new SecretKeySpec(keyBytes, "HmacSHA1")
    val mac        = Mac.getInstance("HmacSHA1")
    mac.init(signingKey)
    val rawHmac = Base64.getEncoder.encode(mac.doFinal(stringToEncode.getBytes(StandardCharsets.UTF_8.displayName())))
    new String(rawHmac, StandardCharsets.UTF_8.displayName())
  }
}
