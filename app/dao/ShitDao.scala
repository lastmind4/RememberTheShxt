package dao

import javax.inject.{Inject, Singleton}

import entity.Shit
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShitDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class ShitTable(tag: Tag) extends Table[Shit](tag, "shit") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def category = column[String]("category")

    def comment = column[String]("comment")

    def * = (id, name, category, comment) <> ((Shit.apply _).tupled, Shit.unapply)
  }

  private val shits = TableQuery[ShitTable]

  def create(name: String, category: String, comment: String): Future[Shit] = db.run {
    (shits.map(p => (p.name, p.category, p.comment))
      returning shits.map(_.id)
      into ((data, id) => Shit(id, data._1, data._2, data._3))
      ) += (name, category, comment)
  }

  def edit(id: Long, shit: Shit): Future[Int] = db.run {
    shits.filter(_.id === id).update(shit)
  }

  def remove(id: Long): Future[Int] = db.run {
    shits.filter(_.id === id).delete
  }

  def list(): Future[Seq[Shit]] = db.run {
    shits.result
  }
}
