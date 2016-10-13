package com.example.graphql.schema

import sangria.schema._
import sangria.schema.Action.defaultAction
import com.example.graphql.model.Role
import com.example.graphql.model.User
import com.example.graphql.service.UserRepository

/**
 * Defines a GraphQL schema for the current project
 */
object SchemaDefinition {
  val RoleEnumType = EnumType(
    "Role",
    Some("List of roles"),
    List(
      EnumValue("USER", value = Role.USER, description = Some("User")),
      EnumValue("RESELLER", value = Role.RESELLER, description = Some("Reseller")),
      EnumValue("CHANNEL_ADMIN", value = Role.CHANNEL_ADMIN, description = Some("Channel Admin")),
      EnumValue("SUPERUSER", value = Role.SUPERUSER, description = Some("Superuser"))))

  val UserType = ObjectType(
    "User",
    "User Type",
    interfaces[Unit, User](),
    fields[Unit, User](
      Field("id", LongType, Some("User id"), tags = ProjectionName("_id") :: Nil, resolve = _.value.id),
      Field("email", StringType, Some("User email address"), resolve = _.value.email),
      Field("activated", BooleanType, Some("User email address"), resolve = _.value.activated),
      Field("firstName", OptionType(StringType), Some("User first name"), resolve = _.value.firstName),
      Field("lastName", OptionType(StringType), Some("User last name"), resolve = _.value.lastName),
      Field("roles", ListType(RoleEnumType), Some("User roles"), resolve = _.value.roles)
    ))

  val ID = Argument("id", LongType, description = "The ID of the user")
  val RoleArg = Argument("role", OptionInputType(RoleEnumType), description = "If omitted, returns all roles")

  val Query = ObjectType(
    "Query", fields[UserRepository, Unit](
      Field("user", OptionType(UserType), arguments = ID :: Nil, resolve = (ctx) => ctx.ctx.findById(ctx.arg(ID))),
      Field("users", ListType(UserType), resolve = (ctx) => ctx.ctx.findAll())
    ))
    
  val Mutation = ObjectType(
    "Mutation", fields[UserRepository, Unit](
      Field("activateUser", OptionType(UserType), arguments = ID :: Nil, resolve = (ctx) => ctx.ctx.activateById(ctx.arg(ID)))
    )) 

  val UserSchema = Schema(Query, Some(Mutation))
}
