object Config {
  val host = sys.env.getOrElse("KSQLDB_HOST", "ksqldb-server")
  val port = sys.env.getOrElse("KSQLDB_PORT", "8088").toInt
  val accept = sys.env.getOrElse("KSQLDB_ACCEPT", "application/vnd.ksql.v1+json")
  val initStatement = sys.env.getOrElse("KSQLDB_INIT_STATEMENT", "CREATE STREAM kafkaFormat0901 (message VARCHAR) WITH (KAFKA_TOPIC='events_09_01', VALUE_FORMAT='KAFKA', partitions=1);")
  val query = sys.env.getOrElse("KSQLDB_QUERY",
    "SELECT EXTRACTJSONFIELD(message, '$.TokenInfo.event_tm') AS event_tm," +
      " EXTRACTJSONFIELD(message, '$.TokenInfo.authentication_context') AS authentication_context," +
      " EXTRACTJSONFIELD(message, '$.TokenInfo.jti') AS jti, " +
      "EXTRACTJSONFIELD(message, '$.TokenInfo.client_id') AS client_id" +
      " FROM kafkaFormat0901 WHERE EXTRACTJSONFIELD(message, '$.TokenInfo') IS NOT NULL;"
  )

  val headersR = Map(
    "Accept" -> accept,
    "Content-Type" -> "application/json"
  )
  val headersQ = Map(
    "Accept" -> accept,
    "Content-Type" -> accept
  )


  val mockedJsons = sys.env.getOrElse("KSQLDB_JSONS", "[{\"TokenInfo\":{\"event_tm\":\"2024-01-16T14:54:25Z\",\"authentication_context\":[{\"identifier\":\"+34641387462\",\"type\":\"phone_number\",\"activated_roles\":[\"user\"]}],\"jti\":\"614481ca-9b03-50e2-bdd6-5b95492a2416\",\"client_id\":\"mock-client\",\"exp\":\"2024-01-16T14:54:25Z\",\"consents\":[],\"acr\":\"4\",\"activated_roles\":[\"user\"],\"iat\":\"2024-01-16T14:54:25Z\",\"sub\":\"839ba635-56c3-56cc-af56-90f52161e074\",\"may_act\":{\"sub\":\"delegated\"},\"aud\":\"http://foha.my/ohitewin\",\"identifier_bound_scopes\":[{\"identifier\":\"+34675778420\",\"type\":\"phone_number\",\"scopes\":[\"internal:health\"]},{\"identifier\":\"david.villa\",\"type\":\"username\",\"scopes\":[\"internal:health\"]}],\"amr\":[\"sms\",\"pwd\"],\"iss\":\"http://bajburov.cu/tujezvu\",\"grant_type\":{\"name\":\"authorization_code\",\"scopes\":[\"internal:health\"],\"purposes\":[]},\"corr\":\"4afddd0c-b729-5dc9-acaa-e93fa7ca29e7\"}},{\"TokenInfo\":{\"event_tm\":\"2024-01-16T14:54:25Z\",\"authentication_context\":[{\"identifier\":\"+34629747130\",\"type\":\"phone_number\",\"activated_roles\":[\"admin\"]}],\"jti\":\"0f1469a3-6739-55fd-af84-25b686df9b91\",\"client_id\":\"baikal-api\",\"exp\":\"2024-01-16T14:54:25Z\",\"consents\":[],\"acr\":\"4\",\"activated_roles\":[\"user\"],\"iat\":\"2024-01-16T14:54:25Z\",\"sub\":\"d9fe04e2-a4b2-5ec7-a1c9-21b121468c46\",\"may_act\":{\"sub\":\"delegated\"},\"aud\":\"http://cekjop.ad/ro\",\"identifier_bound_scopes\":[{\"identifier\":\"+34690574627\",\"type\":\"phone_number\",\"scopes\":[\"internal:health\"]},{\"identifier\":\"david.villa\",\"type\":\"username\",\"scopes\":[\"internal:health\"]}],\"amr\":[\"sms\",\"pwd\"],\"iss\":\"http://keumu.ir/epdabsit\",\"grant_type\":{\"name\":\"authorization_code\",\"scopes\":[\"internal:health\"],\"purposes\":[]},\"corr\":\"a3f8a747-8d5d-55e2-a31e-517265433f23\"}},{\"TokenInfo\":{\"event_tm\":\"2024-01-16T14:54:25Z\",\"authentication_context\":[{\"identifier\":\"+34735487323\",\"type\":\"phone_number\",\"activated_roles\":[\"owner\"]}],\"jti\":\"238ead3f-1310-5345-8298-8f75d54cc664\",\"client_id\":\"mock-client\",\"exp\":\"2024-01-16T14:54:25Z\",\"consents\":[],\"acr\":\"4\",\"activated_roles\":[\"user\"],\"iat\":\"2024-01-16T14:54:25Z\",\"sub\":\"6cc9c085-b731-50f2-8f5c-47d410b76f25\",\"may_act\":{\"sub\":\"delegated\"},\"aud\":\"http://hitze.zw/raga\",\"identifier_bound_scopes\":[{\"identifier\":\"+34663692855\",\"type\":\"phone_number\",\"scopes\":[\"internal:health\"]},{\"identifier\":\"andres.iniesta\",\"type\":\"username\",\"scopes\":[\"internal:health\"]}],\"amr\":[\"sms\",\"pwd\"],\"iss\":\"http://ikbo.gov/sozetrav\",\"grant_type\":{\"name\":\"authorization_code\",\"scopes\":[\"internal:health\"],\"purposes\":[]},\"corr\":\"1c6395b4-4893-5c44-8d40-289fb04144bc\"}}]")

}
