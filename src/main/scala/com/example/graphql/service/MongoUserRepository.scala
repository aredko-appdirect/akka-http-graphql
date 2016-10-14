package com.example.graphql.service

import reactivemongo.core.commands.FindAndModify

import reactivemongo.bson.BSONDocumentReader

import reactivemongo.bson.Macros

import reactivemongo.api.MongoConnection
import reactivemongo.api.MongoDriver
import com.example.graphql.model.User
import reactivemongo.bson.BSONDocumentWriter
import com.example.graphql.model.Address
import com.example.graphql.model.Company
import scala.concurrent.Future
import reactivemongo.api.DefaultDB
import java.time.LocalDateTime
import java.util.Locale
import com.example.graphql.model.Role
import reactivemongo.api.commands._  

class MongoUserRepository extends Repository {
  import scala.concurrent.ExecutionContext.Implicits.global
  import reactivemongo.bson._
  
  val mongoUri = "mongodb://192.168.99.100:27017/mydb?authMode=scram-sha1"
  val connection = MongoConnection.parseURI(mongoUri).map(MongoDriver().connection(_))
  val futureConnection = Future.fromTry(connection)
  
  def db: Future[DefaultDB] = futureConnection.flatMap(_.database("mydb"))
  def collection = db.map(_.collection("users"))
  
  implicit def addressWriter: BSONDocumentWriter[Address] = Macros.writer[Address]
  implicit def addressReader: BSONDocumentReader[Address] = Macros.reader[Address]
  implicit def companyWriter: BSONDocumentWriter[Company] = Macros.writer[Company]
  implicit def companyReader: BSONDocumentReader[Company] = Macros.reader[Company]
  implicit def userWriter: BSONDocumentWriter[User] = Macros.writer[User]
  implicit def userReader: BSONDocumentReader[User] = Macros.reader[User]
  
  def findAll(fields: Seq[String]): Future[Seq[User]] = collection.flatMap(
      _.find(document())
        //.projection(fields.foldLeft(document())((doc, field) => doc.add(field -> BSONInteger(1))))
        .cursor[User]()
        .collect[Seq]()
    )
      
   def findById(id: Long): Future[Option[User]] = collection.flatMap(
      _.find(document("id" -> BSONLong(id))).one[User]
    )
    
  def addUser(email: String, firstName: Option[String], lastName: Option[String], roles: Seq[Role.Value]): Future[User] = {
    val user = User(BSONObjectID.generate().time, email, LocalDateTime.now(), firstName, lastName, roles, false, false, Locale.CANADA, Company(1, "Tommyknockers, Inc.", Address()), Address())
    collection.flatMap(_.insert(user).map(_ => user))
  }
  
   def activateById(id: Long): Future[Option[User]] =
    collection.flatMap(_.findAndUpdate(document("id" -> BSONLong(id)), 
      document("$set" -> document("activated" -> BSONBoolean(true))), fetchNewObject = true).map { _.result[User] })
}