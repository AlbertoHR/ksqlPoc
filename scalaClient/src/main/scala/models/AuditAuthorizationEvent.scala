package models

import java.util.UUID

case class AuditAuthorizationEvent(eventTm: String, authenticationContext: String, jti: UUID, clientId: String)

object AuditAuthorizationEvent {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val auditAuthorizationEventReads: Reads[AuditAuthorizationEvent] = (
    (__ \ "row" \ "columns")(0).read[String] and
      (__ \ "row" \ "columns")(1).read[String] and
      (__ \ "row" \ "columns")(2).read[UUID] and
      (__ \ "row" \ "columns")(3).read[String]
    )(AuditAuthorizationEvent.apply _)
}
