package com.example.graphql

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import scala.Left
import scala.Right
import scala.util.Failure
import scala.util.Success
import scala.util.Try

import sangria.ast.StringValue
import sangria.schema.ScalarType
import sangria.validation.ValueCoercionViolation
import java.util.Locale

package object schema {
  case object LocalDateTimeCoercionViolation extends ValueCoercionViolation("Date value expected")
  case object LocaleCoercionViolation extends ValueCoercionViolation("Locale value expected")

  private[schema] def parseLocalDateTime(s: String) = Try(LocalDateTime.parse(s)) match {
    case Success(date) => Right(date)
    case Failure(_) => Left(LocalDateTimeCoercionViolation)
  }

  private[schema] def parseLocale(s: String) = Try(Locale.forLanguageTag(s)) match {
    case Success(locale) => Right(locale)
    case Failure(_) => Left(LocaleCoercionViolation)
  }

  val LocalDateTimeType = ScalarType[LocalDateTime]("LocalDateTime",
    coerceOutput = (value, caps) => value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    coerceUserInput = {
      case s: String => parseLocalDateTime(s)
      case _ => Left(LocalDateTimeCoercionViolation)
    },
    coerceInput = {
      case StringValue(s, _, _) => parseLocalDateTime(s)
      case _ => Left(LocalDateTimeCoercionViolation)
    })
    
  val LocaleType = ScalarType[Locale]("Locale",
    coerceOutput = (value, caps) => value.toLanguageTag,
    coerceUserInput = {
      case s: String => parseLocale(s)
      case _ => Left(LocaleCoercionViolation)
    },
    coerceInput = {
      case StringValue(s, _, _) => parseLocale(s)
      case _ => Left(LocaleCoercionViolation)
    })
}