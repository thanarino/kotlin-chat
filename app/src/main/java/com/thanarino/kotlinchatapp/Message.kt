package com.thanarino.kotlinchatapp

class Message() {
  var message: String? = null
  var senderId: String? = null

  constructor(message: String?, senderId: String?): this() {
    this.message = message
    this.senderId = senderId
  }
}