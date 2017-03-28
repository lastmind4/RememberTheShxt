package services

import entity.Shit

object ShitService {
  def getShit(id: String): Option[Shit] = {
    None
  }

  def getShits(): List[Shit] = {
    List()
  }
}