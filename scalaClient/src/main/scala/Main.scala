import io.confluent.ksql.api.client.{BatchedQueryResult, Client, ClientOptions}

import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
object Main {
  def main(args: Array[String]): Unit = {

    val host = sys.env.getOrElse("KSQLDB_HOST", "ksqldb-server")
    val port = sys.env.getOrElse("KSQLDB_PORT", "8088").toInt
    val initStatement = sys.env.getOrElse("KSQLDB_INIT_STATEMENT", "CREATE STREAM kafkaFormat0901 (message VARCHAR) WITH (KAFKA_TOPIC='events_09_01', VALUE_FORMAT='KAFKA', partitions=1);")
    val query = sys.env.getOrElse("KSQLDB_QUERY",
      "SELECT EXTRACTJSONFIELD(message, '$.TokenInfo.event_tm') AS event_tm, EXTRACTJSONFIELD(message, '$.TokenInfo.authentication_context') AS authentication_context, EXTRACTJSONFIELD(message, '$.TokenInfo.jti') AS jti, EXTRACTJSONFIELD(message, '$.TokenInfo.client_id') AS client_id FROM kafkaFormat0901 WHERE EXTRACTJSONFIELD(message, '$.TokenInfo') IS NOT NULL;"
    )

    println("Sleeping for a minute until environment up and running")

    println(s"host: $host, port: $port")
    println(s"initStatement: $initStatement, query: $query")

    Thread.sleep(60000) // 60 seconds delay

    val clientOptions: ClientOptions = ClientOptions.create().setHost(host).setPort(port)

    val client: Client = Client.create(clientOptions)
    var counter: Long = 0L

    def executionLoop(): Unit = {
      while (true) {
        Try {
          counter += 1
          println(s"This is the $counter iteration")
          val batchedQueryResult: Future[BatchedQueryResult] = Future(client.executeQuery(query))
          val rows = for {
            batchQueryResult <- batchedQueryResult
            _ = println( s" Printing the batchQueryResult $batchQueryResult")
            resultRows = batchQueryResult.get(5, TimeUnit.SECONDS).asScala.toList
          } yield resultRows

          rows.onComplete {
            case Failure(exception) => println(s"Error occurred on $counter iteration: $exception")
            case Success(rows) =>
              println("Received results. Num rows: " + rows.size)
              rows.foreach(row => println(row.values()))
          }

          Thread.sleep(10000) // 10 seconds delay
        } match {
          case Failure(exception) =>
            println(s"Error occurred on $counter iteration: $exception")
            Thread.sleep(10000) // 10 seconds delay

          case Success(_) => println(s"Finish iteration $counter")
        }
      }
    }

    Future(client.executeStatement(initStatement)).onComplete {
      case Failure(exception) => println(s"Error occurred initializing ksql scala client: $exception")
      case Success(_) =>
        println("Successfully initialized ksql scala client")
        executionLoop()
    }
  }
}
