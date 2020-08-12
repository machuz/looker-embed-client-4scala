package com.ma2k8.looker.looker.user

sealed abstract class LookerModel(val value: String)

object LookerModel {
  case object TestMa2k8 extends LookerModel("ma2k8_test")
  case object Hoge   extends LookerModel("hoge")
}
