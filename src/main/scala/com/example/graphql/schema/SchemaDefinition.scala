package com.example.graphql.schema

import sangria.schema._
import sangria.schema.Action._
import com.example.graphql.model.Role
import com.example.graphql.model.User
import com.example.graphql.service.Repository
import com.example.graphql.model.Address
import com.example.graphql.model.Company
import scala.concurrent.Future

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

  val AddressType = ObjectType(
    "Address",
    "Address Type",
    interfaces[Unit, Address](),
    fields[Unit, Address](
      Field("street", StringType, Some("Street name"), resolve = _.value.street),
      Field("city", StringType, Some("CityName"), resolve = _.value.city),
      Field("state", StringType, Some("State or Province"), resolve = _.value.state),
      Field("zip", StringType, Some("Postal Code"), resolve = _.value.zip),
      Field("country", StringType, Some("Country"), resolve = _.value.country)
    ))
    
  val CompanyType = ObjectType(
    "Company",
    "Company Type",
    interfaces[Unit, Company](),
    fields[Unit, Company](
      Field("id", LongType, Some("Company id"), tags = ProjectionName("_id") :: Nil, resolve = _.value.id),
      Field("address", AddressType, Some("User address"), resolve = _.value.address),
      Field("name", StringType, Some("Company name"), resolve = _.value.name)
    ))
    
  val UserType = ObjectType(
    "User",
    "User Type",
    interfaces[Unit, User](),
    fields[Unit, User](
      Field("id", LongType, Some("User id"), tags = ProjectionName("id") :: Nil, resolve = _.value.id),
      Field("email", StringType, Some("User email address"), resolve = _.value.email),
      Field("created", LocalDateTimeType, Some("User creation date"), resolve = _.value.created),
      Field("locale", LocaleType, Some("User locale"), resolve = _.value.locale),
      Field("company", CompanyType, Some("User company"), resolve = _.value.company),
      Field("address", AddressType, Some("User address"), resolve = _.value.address),
      Field("deleted", BooleanType, Some("User is deleted"), resolve = _.value.deleted),
      Field("activated", BooleanType, Some("User is activated"), resolve = _.value.activated),
      Field("firstName", OptionType(StringType), Some("User first name"), resolve = _.value.firstName),
      Field("lastName", OptionType(StringType), Some("User last name"), resolve = _.value.lastName),
      Field("roles", ListType(RoleEnumType), Some("User roles"), resolve = _.value.roles)
    ))

  val ID = Argument("id", LongType, description = "The ID of the user")
  val RolesArg = Argument("roles", ListInputType(RoleEnumType), description = "User roles")
  val EmailArg = Argument("email", StringType, description = "User email address")
  val FirstNameArg = Argument("firstName", OptionInputType(StringType), description = "User first name")
  val LastNameArg = Argument("lastName", OptionInputType(StringType), description = "User last name")
  
  val Query = ObjectType(
    "Query", fields[Repository, Unit](
      Field("user", OptionType(UserType), arguments = ID :: Nil, resolve = (ctx) => ctx.ctx.findById(ctx.arg(ID))),
      Field("users", ListType(UserType), resolve = Projector((ctx, f) => ctx.ctx.findAll(f.map(_.name))))
    ))
    
  val Mutation = ObjectType(
    "Mutation", fields[Repository, Unit](
      Field("activateUser", OptionType(UserType), arguments = ID :: Nil, 
        resolve = (ctx) => ctx.ctx.activateById(ctx.arg(ID))),
      Field("addUser", OptionType(UserType), arguments = EmailArg :: FirstNameArg :: LastNameArg :: RolesArg :: Nil, 
        resolve = (ctx) => ctx.ctx.addUser(ctx.arg(EmailArg), ctx.arg(FirstNameArg), ctx.arg(LastNameArg), ctx.arg(RolesArg)))
    )) 

  val UserSchema = Schema(Query, Some(Mutation))
}
