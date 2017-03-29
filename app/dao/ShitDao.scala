package dao

import javax.inject.Inject

import entity.Shit
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import slick.model.Table

import scala.concurrent.Future

class ShitDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  private val Shits = TableQuery[ShitsTable]

  def all(): Future[Seq[Shit]] = db.run(Shits.result)

  def insert(shit: Shit): Future[Unit] = db.run(Shits += shit).map { _ => () }

  private class ShitsTable(tag: Tag) extends Table[Shit](tag, "SHIT") {

    def name = column[String]("NAME", O.PrimaryKey)

    def color = column[String]("COLOR")

    def * = (name, color) <> (Shit.tupled, Shit.unapply _)
  }
}