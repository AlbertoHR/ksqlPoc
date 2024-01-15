-- Here we are creating a Stream from the data of events_09_01, and taking the value of the json of the TokenInfo type messages
-- It contains null values for the rest of data in the stream
CREATE STREAM tokenInfo0901 (
    TokenInfo VARCHAR
) WITH (
    KAFKA_TOPIC='events_09_01',
    VALUE_FORMAT='JSON',
    partitions=1
);

SELECT * FROM tokenInfo0901 WHERE TokenInfo IS NOT NULL;

CREATE STREAM pepito0901 (
    Pepito VARCHAR
) WITH (
    KAFKA_TOPIC='events_09_01',
    VALUE_FORMAT='JSON'
);

SELECT * FROM pepito0901 WHERE Pepito IS NOT NULL;

SELECT
    EXTRACTJSONFIELD(TokenInfo, '$.event_tm') AS event_tm,
    EXTRACTJSONFIELD(TokenInfo, '$.authentication_context') AS authentication_context,
    EXTRACTJSONFIELD(TokenInfo, '$.jti') AS jti,
    EXTRACTJSONFIELD(TokenInfo, '$.client_id') AS client_id
FROM tokenInfo0901;


CREATE STREAM kafkaFormat0901 (
    message VARCHAR
) WITH (
    KAFKA_TOPIC='events_09_01',
    VALUE_FORMAT='KAFKA',
    partitions=1
);

SELECT
    EXTRACTJSONFIELD(message, '$.TokenInfo.event_tm') AS event_tm,
    EXTRACTJSONFIELD(message, '$.TokenInfo.authentication_context') AS authentication_context,
    EXTRACTJSONFIELD(message, '$.TokenInfo.jti') AS jti,
    EXTRACTJSONFIELD(message, '$.TokenInfo.client_id') AS client_id
FROM kafkaFormat0901
WHERE EXTRACTJSONFIELD(message, '$.TokenInfo') IS NOT NULL;


SELECT
    EXTRACTJSONFIELD(message, '$.Pepito.event_tm') AS event_tm,
    EXTRACTJSONFIELD(message, '$.Pepito.authentication_context') AS authentication_context,
    EXTRACTJSONFIELD(message, '$.Pepito.jti') AS jti,
    EXTRACTJSONFIELD(message, '$.Pepito.client_id') AS client_id
FROM kafkaFormat0901
WHERE EXTRACTJSONFIELD(message, '$.Pepito') IS NOT NULL;