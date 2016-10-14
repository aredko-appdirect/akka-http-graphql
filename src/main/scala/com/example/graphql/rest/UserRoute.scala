package com.example.graphql.rest

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import sangria.parser.QueryParser
import sangria.execution.{ErrorWithResolver, QueryAnalysisError, Executor}
import sangria.marshalling.sprayJson._

import spray.json._

import scala.util.{Success, Failure}
import com.example.graphql.schema.SchemaDefinition
import com.example.graphql.service.UserRepository
import akka.actor.ActorSystem
import com.example.graphql.service.MongoUserRepository

class UserRoute(implicit val system: ActorSystem) {
  import system.dispatcher
  
  val route: Route =
    (post & path("users")) {
      entity(as[String]) { document =>
        QueryParser.parse(document) match {
          case Success(queryAst) =>
            complete(Executor.execute(SchemaDefinition.UserSchema, queryAst, new MongoUserRepository)
              .map(OK -> _)
              .recover {
                case error: QueryAnalysisError => BadRequest -> error.resolveError
                case error: ErrorWithResolver => InternalServerError -> error.resolveError
              })

          case Failure(error) => complete(BadRequest, JsObject("error" -> JsString(error.getMessage)))
        }
      }
    }

}