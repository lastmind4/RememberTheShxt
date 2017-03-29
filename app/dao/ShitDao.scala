package dao

import javax.inject.{Inject, Singleton}

import entity.Shit
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShitDao @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class ShitTable(tag: Tag) extends Table[Shit](tag, "shit") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def comment = column[String]("comment")

    def * = (id, name, comment) <> ((Shit.apply _).tupled, Shit.unapply)
  }

  private val shits = TableQuery[ShitTable]

  def create(name: String, comment: String): Future[Shit] = db.run {
    (shits.map(p => (p.name, p.comment))
      returning shits.map(_.id)
      into ((nameComment, id) => Shit(id, nameComment._1, nameComment._2))
      ) += (name, comment)
  }

  def list(): Future[Seq[Shit]] = db.run {
    shits.result
  }
}
