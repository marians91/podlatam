# Metric

## Introduction

This file contains the metric to be loaded to catalog. It has to be placed under /infrastructure/platform/catalog/metrics folder, and it is different from the /infrastructure/platform/metrics/metrics.yml, since the last is used for the grafana platform parameters load.

Every metric is described by: 
* name: a name chosen for the metric.
* unitOfMeasure: a unit to measure the above metric.
* description: a short description of the metric.

An example metrics.yml	
~~~yml
---
metrics:
- name: metric1
  unitOfMeasure: counter
  description: desc1
- name: metric2
  unitOfMeasure: counter
  description: desc2
~~~

