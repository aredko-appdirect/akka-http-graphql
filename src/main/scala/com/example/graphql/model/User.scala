package com.example.graphql.model

import java.util.Locale
import java.util.Date

case class User(id: Long, email: String, partner: String, created: Date, firstName: Option[String], lastName: Option[String],
  roles: Seq[Role.Value], deleted: Boolean, activated: Boolean, locale: Locale, company: Company, address: Address)