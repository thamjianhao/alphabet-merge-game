package tjh.AlphabetMerge.util

import scalikejdbc._
import tjh.AlphabetMerge.model.PlayerScore

trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true;"; // initialize JDBC driver & connection pool Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "me", "mine") // ad-hoc session provider on the REPL
  implicit val session = AutoSession
}

object Database extends Database {
  def setupDB(): Unit = {
    if (!hasDBInitialize)
      PlayerScore.initializeTable()

    def hasDBInitialize: Boolean = {
      DB getTable "PlayerScore" match {
        case Some(_) => true
        case None => false
      }
    }
  }
}