package com.example.graphql.model

import java.util.Locale
import java.time.LocalDateTime

case class User(id: Long, email: String, created: LocalDateTime, firstName: Option[String], lastName: Option[String],
  roles: Seq[Role.Value], deleted: Boolean, activated: Boolean, locale: Locale, company: Company, address: Address)