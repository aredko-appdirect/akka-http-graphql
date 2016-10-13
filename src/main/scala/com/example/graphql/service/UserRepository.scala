package com.example.graphql.service

import java.util.Date
import java.util.Locale

import com.example.graphql.model.User
import com.example.graphql.model.Role
import com.example.graphql.model.Company
import com.example.graphql.model.Address

class UserRepository {
  def findById(id: Long): User = User(id, "a@b.com", "APPDIRECT", new Date(), Some("Tom"), Some("Tommyknocker"),
    Seq(Role.CHANNEL_ADMIN), false, true, Locale.CANADA, Company(1, "Tommyknockers, Inc.", Address()), Address())
    
  def findAll(): Seq[User] = Seq( 
      User(1, "a@b.com", "APPDIRECT", new Date(), Some("Tom"), Some("Tommyknocker"),
        Seq(Role.CHANNEL_ADMIN), false, true, Locale.CANADA, Company(1, "Tommyknockers, Inc.", Address()), Address()),
      User(2, "c@d.com", "APPDIRECT", new Date(), Some("Bob"), Some("Bobinec"),
        Seq(Role.USER), false, true, Locale.CANADA, Company(2, "Bobinec & Family", Address()), Address())
    )
}