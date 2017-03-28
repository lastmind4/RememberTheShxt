package services

class Box(val id: String, val name: String) {

}

object Box {
  val boxes = Map("1" -> new Box("1", "wahaha"), "2" -> new Box("2", "subara"))

  def get(id: String): Option[Box] = {
    boxes.get(id)
  }
}