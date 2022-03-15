# Metadata

## Introduction

Each microservice has to specify a list of associated metadata.

This file describes the parameters code supported, and it must be placed under the /infrastructure/platform/catalog folder.

Below the description of the parameters:

* minHA: HA minimum level between μS and underlying system
* ReleaseNotes: A description relative to the release and eventual bug resolved
* DateDeprecated: this is the deprecation date
* DeprecateStart: this is the date of deprecation. Starting from this date the microservice will be deprecated.
* tags: a list of tags related to the microservice.
* conflLink: this is a link to the release note confluence page.	
* SLAParameters: list of SLA Parameters (max simultaneous calls handled - warning threshold computed).
* underlyingAPMS: list of underlying virtual entity apms  (can be used for impact analysis purposes).
* DataGovernanceMetadata: this field is related to data governance metadata and has to be defined.

An example metadata.yml	
~~~yml
---
minHA: No-Gold
ReleaseNotes: A description relative to the release and eventual bug resolved
DateDeprecated: null
DeprecateStart: false
tags:
- tag1
- tag2
conflLink: "<url_written_from_developer>"
SLAParameters:
- nameSla: maxSimultaneousCall
  value: 10000
- nameSla: warning-threshold
  value: 80%
underlyingAPMS:
- eventualAPMVirtualEntityCode
DataGovernanceMetadata: "t.b.d."
~~~

