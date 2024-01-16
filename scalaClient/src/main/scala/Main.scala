
import play.api.libs.json.{JsNumber, JsObject, JsString}
import play.mvc.BodyParser.Json
import requests.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
object Main {
  def main(args: Array[String]): Unit = {

    val host = sys.env.getOrElse("KSQLDB_HOST", "ksqldb-server")
    val port = sys.env.getOrElse("KSQLDB_PORT", "8088").toInt
    val accept = sys.env.getOrElse("KSQLDB_ACCEPT", "application/vnd.ksql.v1+json")
    val initStatement = sys.env.getOrElse("KSQLDB_INIT_STATEMENT", "CREATE STREAM kafkaFormat0901 (message VARCHAR) WITH (KAFKA_TOPIC='events_09_01', VALUE_FORMAT='KAFKA', partitions=1);")
    val query = sys.env.getOrElse("KSQLDB_QUERY",
      "SELECT EXTRACTJSONFIELD(message, '$.TokenInfo.event_tm') AS event_tm, EXTRACTJSONFIELD(message, '$.TokenInfo.authentication_context') AS authentication_context, EXTRACTJSONFIELD(message, '$.TokenInfo.jti') AS jti, EXTRACTJSONFIELD(message, '$.TokenInfo.client_id') AS client_id FROM kafkaFormat0901 WHERE EXTRACTJSONFIELD(message, '$.TokenInfo') IS NOT NULL;"
    )

    val headersR = Map(
      "Accept" -> accept,
      "Content-Type" -> "application/json" // Set Content-Type
    )
    val headersQ = Map(
      "Accept" -> accept,
      "Content-Type" -> accept // Set Content-Type
    )

    println("Sleeping for a minute until environment up and running")

    println(s"host: $host, port: $port")
    println(s"initStatement: $initStatement, query: $query")

    val streamExecutionJson: JsObject = new JsObject(
      Map("ksql" -> JsString(initStatement), "streamProperties" -> new JsObject(Map.empty))
    )

    val queryExecutionJson: JsObject = new JsObject(
      Map("ksql" -> JsString(query), "streamProperties" -> new JsObject(Map.empty))
    )

    println(streamExecutionJson.toString())
    Thread.sleep(30000) // 60 seconds delay


    val response: Future[Response] = Future(requests.post(s"http://$host:$port/ksql", data = streamExecutionJson.toString(), headers = headersR))

    response.onComplete {
      case Failure(exception) => println(s"Error message: $exception")
      case Success(value) => println(s"${value.text()}")
    }

    while (true) {
      Thread.sleep(1000)
      val responseQuery = Future(requests.post(s"http://$host:$port/query", data = queryExecutionJson.toString(), headers = headersQ))

      responseQuery.onComplete {
        case Failure(exception) => println(s"Error on query. $exception")
        case Success(value) => println(s"The values are ${value.text()}")
      }
    }
  }
}
