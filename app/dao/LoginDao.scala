package dao

import javax.inject.{Inject, Singleton}

import entity.Login
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LoginDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class LoginTable(tag: Tag) extends Table[Login](tag, "login") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def password = column[String]("password")

    def * = (id, name, password) <> ((Login.apply _).tupled, Login.unapply)
  }

  private val logins = TableQuery[LoginTable]

  def create(name: String, password: String): Future[Login] = db.run {
    (logins.map(p => (p.name, p.password))
      returning logins.map(_.id)
      into ((namePassword, id) => Login(id, namePassword._1, namePassword._2))
      ) += (name, password)
  }

  def list(): Future[Seq[Login]] = db.run {
    logins.result
  }

  def login(name: String, password: String): Future[Option[Login]] = db.run {
    logins.filter(_.name == name).filter(_.password == password).result.headOption
  }
}