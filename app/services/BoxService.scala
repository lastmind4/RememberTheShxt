package services

object BoxService {
  def getBox(id: String): Option[Box] = {
    Box.get(id)
  }
}