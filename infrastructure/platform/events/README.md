# Events

## Introduction

Events are just another form of communication between microservices; event sourcing for example, relies on the ability of microservices to listen to other microservices events. The CSDT Platform provides a centralized system, the Common Service **Event Manager**, and a SDK for simple Event communications.

Every Element can publish and subscribe to events using the platform services. In the following sections will be described how to and what restrictions apply.

## Configuration

This template folder contains the whole events configuration. The structure is the following one:

~~~c
|-- infrastructure/
    |-- platform/
        |-- events/
            |-- schema/
            |-- events-published.json
            |-- events-subscribed.json
~~~

### events-published.json

This file describes the events published metadata:

* event-name: is the name of the published event
* payload-version: is the version of the json payload published
* event-class: is an event macro category
* tags: is an array which contains tags in oder to make the search easier

**NOTE:** if the current microservice publish an event to the platform put this file under the /infastructure/events folder

see the example below:

file: **events-published.json**

~~~json
{
  "events": [
    {
      "event-name": "EchoEvent",
      "payload-version": 1,
      "target": "KAFKA",
      "partitions": 1,
      "replicas": 1
    },
    {
      "event-name": "EchoEvent",
      "payload-version": 1,
      "target": "SQS"
    }
  ]
}
~~~

### Schema folder

Platform event manager expects under the _schema_ folder the avro schema files description of the events payload. One file per event with the same name declared by the event-name metadata, plus "_" and the payload number associated.

Here an example for the ItemModificato event:

file: **ItemModificato_1.json**
~~~json
{
  "schema": {
    "fields": [
      {
        "name": "name",
        "type": {
          "fields": [
            {
              "name": "first_name",
              "type": [
                "null",
                "string"
              ]
            },
            {
              "name": "second_name",
              "type": "string"
            }
          ],
          "name": "name",
          "type": "record"
        }
      },
      {
        "name": "longField",
        "type": ["null", "long"]
      },
      {
        "logicalType": "date",
        "name": "date",
        "type": [
          "null",
          "int"
        ]
      }
    ],
    "name": "EchoEvent_1",
    "namespace": "com.acme.avro",
    "type": "record"
  },
  "schemaType": "AVRO",
  "references": []
}
~~~
A useful link is the official guide: [https://json-schema.org/](https://json-schema.org/)

### events-subscribed.json

This file describes the array of the events subscribed by this microservice:

* id-domain: domain of the microservice
* micro-service-name: the subscribed microservice name
* id-event: the subscribed event name
* callback-api: the API of the current microservice to call when arrived a subscribed event in the event manager 

**NOTE:** if the current microservice is subscribed to an event to the platform put this file under the /infastructure/events folder

file: **events-subscribed.json**
~~~json
{
  "events": [
    {
      "id-domain": "Slsnoop",
      "micro-service-name": "mstest012",
      "event-name": "EchoEvent",
      "payload-version": 2,
      "callback-api": "/Slsnoop/callback",
      "callback-port": "8080",
      "delay-base": 10,
      "delay-factor": 1,
      "retry-number": 5,
      "discard-callback": "/Slsnoop/discard-callback",
      "discard-callback-port": "8080"
    }
  ]
}

~~~
