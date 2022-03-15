{{/* Generate configmap with static values(no rendered) */}}
{{- define "helm-pod-configuration.tpl"}}
  ports:
  - containerPort: {{ .Values.container.containerPort }}
    protocol: {{ .Values.container.protocol | quote }}
    name: http
  resources:
    requests:
      memory: {{ .Values.container.resources.requests.memory | quote }}
      cpu: {{ .Values.container.resources.requests.cpu | quote}}
    limits:
      memory: {{ .Values.container.resources.limits.memory | quote}}
      cpu: {{ .Values.container.resources.limits.cpu | quote }}
{{- end -}}

{{/* labels */}}
{{- define "helm-label-values.tpl"}}
  app: {{ .Values.automation.microserviceName | quote }}
  domain: {{ .Values.automation.platformDomain | quote }}
{{- end -}}

{{/* configmap-name */}}
{{- define "helm-configmap-name"}}
{{- list .Values.automation.microserviceName .Values.deploy.version | join "-" | replace "." "-" | replace "_" "-" | lower }}
{{- end -}}

