package com.example.graphql.service

import java.util.Date
import java.util.Locale

import com.example.graphql.model.User
import com.example.graphql.model.Role
import com.example.graphql.model.Company
import com.example.graphql.model.Address
import javafx.util.converter.LocalDateStringConverter
import java.time.LocalDateTime
import scala.collection.mutable.ListBuffer

class UserRepository {
  val USERS = ListBuffer( 
    User(1, "a@b.com", LocalDateTime.now(), Some("Tom"), Some("Tommyknocker"),
      Seq(Role.CHANNEL_ADMIN), false, false, Locale.CANADA, Company(1, "Tommyknockers, Inc.", Address()), Address()),
    User(2, "c@d.com", LocalDateTime.now(), Some("Bob"), Some("Bobinec"),
      Seq(Role.USER), false, false, Locale.CANADA, Company(2, "Bobinec & Family", Address()), Address())
  )
  
  def activateById(id: Long): Option[User] = USERS.filter(_.id == id).headOption.map(_.copy(activated = true))
  def addUser(email: String, firstName: Option[String], lastName: Option[String], roles: Seq[Role.Value]): User = {    
    val user = User(USERS.last.id + 1, email, LocalDateTime.now(), firstName, lastName, roles, false, false, Locale.CANADA, Company(1, "Tommyknockers, Inc.", Address()), Address())
    USERS.append(user)
    user
  }
  
  def findById(id: Long): Option[User] = USERS.filter(_.id == id).headOption
  def findAll(): Seq[User] = USERS
}