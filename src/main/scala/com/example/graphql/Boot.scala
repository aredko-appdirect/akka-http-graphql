package com.example.graphql

import akka.http.scaladsl.Http

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.example.graphql.rest.UserRoute

object Boot extends App {
  implicit val system = ActorSystem("sangria-server")
  implicit val materializer = ActorMaterializer()

  Http().bindAndHandle(new UserRoute().route, "0.0.0.0", sys.props.get("http.port").map(_.toInt).getOrElse(48080))
}
