# Countries

## Introduction

Each microservice can be used for a country, a list of countries, or globally.

### countries.yml

This file lists the countries code supported, and it must be placed under the /infrastructure/platform/catalog/countries folder.
In case the microservice supports all countries, the country code "GLO"  (global) has to to assigned.

The standard chosen is

https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3 


An example below:

~~~yml
---
countries:
- ITA
- ESP
- COL
~~~
