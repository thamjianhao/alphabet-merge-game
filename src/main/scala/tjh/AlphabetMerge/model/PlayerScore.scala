package tjh.AlphabetMerge.model

import tjh.AlphabetMerge.util.Database
import scalafx.beans.property.StringProperty
import scalikejdbc._
import scala.util.Try

class PlayerScore(val nameS: String, val scoreI: Int) {
  val name = new StringProperty(nameS)
  val score = new StringProperty(scoreI.toString)

  def save(): Try[Int] = {
    Try {
      DB.autoCommit { implicit session =>
        sql"""
          insert into PlayerScore (name, score)
          values (${name.value}, ${score.value})
        """.update.apply()
      }
    }
  }
}

object PlayerScore extends Database {
  def apply(nameS: String, scoreI: Int): PlayerScore = new PlayerScore(nameS, scoreI)

  def initializeTable(): Unit = {
    DB.autoCommit { implicit session =>
      sql"""
        create table PlayerScore (
          id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
          name varchar(64),
          score int
        )
      """.execute.apply()
    }
  }

  def getAllPlayerScores: List[PlayerScore] = {
    DB.readOnly { implicit session =>
      sql"select * from PlayerScore order by score desc".map(rs =>
        PlayerScore(rs.string("name"), rs.int("score"))
      ).list.apply()
    }
  }
}