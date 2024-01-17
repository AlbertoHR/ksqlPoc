
import Config._
import models.AuditAuthorizationEvent
import play.api.libs.json.{JsObject, JsString}

import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  def main(args: Array[String]): Unit = {

    val ksqlClient = new KsqlClient

    // I want to create a stream from a kafka topic
    // populate it with JSONs extracted from an env variable
    // Do the queries with the extract json
    // Parse the response

    println(s"host: $host, port: $port")
    println(s"initStatement: $initStatement, query: $query")

    val streamExecutionJson: JsObject = new JsObject(
      Map("ksql" -> JsString(initStatement), "streamsProperties" -> new JsObject(Map.empty))
    )

    val queryExecutionJson: JsObject = new JsObject(
      Map("ksql" -> JsString(query), "streamsProperties" -> new JsObject(Map("ksql.streams.auto.offset.reset" -> JsString("earliest"))))
    )

    ksqlClient.executeStatement(streamExecutionJson.toString()).foreach(println)

    println("Sleeping for a minute until environment up and running")
    Thread.sleep(10000) // 60 seconds delay

    while (true) {
      Thread.sleep(1000)

      (for  {
       response <- ksqlClient.query(queryExecutionJson.toString())
       _ = println(response)
       rows = response.filter(json => json.keys.contains("row") )
        parsedRows = rows.map(_.as[AuditAuthorizationEvent])
      } yield parsedRows)
        .recover { case e: Exception => e.getMessage }.foreach(println)
    }
  }
}
