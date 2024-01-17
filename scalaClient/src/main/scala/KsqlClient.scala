import Config._
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class KsqlClient {

  def query(streamExecutionJsonString: String): Future[List[JsObject]] = {
    Future(requests.post(s"http://$host:$port/query", data = streamExecutionJsonString, headers = headersQ))
      .map(x => Json.parse(x.text()).as[List[JsObject]])
  }

  def executeStatement(streamExecutionJsonString: String): Future[JsValue] = {
    Future(requests.post(s"http://$host:$port/ksql", data = streamExecutionJsonString, headers = headersR))
      .map(x => Json.parse(x.text()).as[JsValue])
  }
}
