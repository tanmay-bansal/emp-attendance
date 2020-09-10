{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "main.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "main.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "main.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}


{{- define "empattendance.labels" }}
app: empattendance
service: empattendance
release: {{ .Release.Name }}
chart: {{ .Chart.Name }}-{{ .Chart.Version | replace "+" "_" }}
{{- end -}}

{{- define "pullPolicy" -}}
{{- if hasKey .Values.global "pullPolicy" -}}
{{ .Values.global.pullPolicy }}
{{- else -}}
{{ .Values.pullPolicy }}
{{- end -}}
{{- end -}}

{{- define "empattendance_reg" -}}
{{- if hasKey .Values.global "empattendance_reg" -}}
{{ .Values.global.empattendance_reg }}
{{- else -}}
{{ .Values.empattendance_reg }}
{{- end -}}
{{- end -}}

{{- define "empattendance_version" -}}
{{- if hasKey .Values.global "empattendance_version" -}}
{{ .Values.global.empattendance_version }}
{{- else -}}
{{ .Values.empattendance_version }}
{{- end -}}
{{- end -}}


{{- define "cluster" -}}
{{- if hasKey .Values.global "cluster" -}}
{{ .Values.global.cluster }}
{{- else -}}
{{ .Values.cluster }}
{{- end -}}
{{- end -}}


{{- define "kafka.service" -}}
{{- if hasKey .Values.global "kafkaService" -}}
{{ .Values.global.kafkaService }}
{{- else -}}
kafka
{{- end -}}
{{- end -}}



{{- define "empattendance.configmap" -}}
{{- printf "%s-config" .Values.empattendanceDatabase.fullnameOverride }}
{{- end -}}

{{- define "empattendance.secret" -}}
{{- printf "%s-secret" .Values.empattendanceDatabase.fullnameOverride }}
{{- end -}}

