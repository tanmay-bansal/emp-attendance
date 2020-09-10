@Library('acadiaBuildTools@develop') _

import com.vibrenthealth.jenkinsLibrary.Utils

def utils = new Utils(this)
def pullSecrets = ['reg.vibrenthealth.com', 'dockergroup.vibrenthealth.com']
def project = "empattendance"

def label = "${project}-${env.BRANCH_NAME.replaceAll(/\//, "-")}-${env.BUILD_NUMBER}"
def branch = env.BRANCH_NAME.replace(/\//,'-')
def version = "$branch-${env.BUILD_NUMBER}"

def artifacts = [[
   "name":"publish empattendance",
   "path":"target/empattendance-0.0.1-SNAPSHOT.jar",
   "group":"empattendance",
   "artifact":"${branch}",
   "version":"0.0.1"
 ]]

def serviceVersion

def slaveContainers = kubeUtils.getCiContainers(containerList: ["maven", "maven11", "python", "docker", "helm", "sonar-scanner", "kubectl"])

podTemplate(
  name: label,
  cloud:'default',
  label:label,
  imagePullSecrets: pullSecrets,
  containers: slaveContainers,
    volumes: [
        hostPathVolume(mountPath:'/var/run/docker.sock', hostPath:'/var/run/docker.sock'),
        hostPathVolume(mountPath: '/root/.m2', hostPath: '/data/m2repo')
    ],
    idleTimeout: 30
) {
    node(label) {
        env.PROJECT = project
        def stackNameRegex = "[^A-Za-z0-9-]"
        def stackName = "${env.PROJECT}-${version}".replaceAll(/(feature-|release-)/, "").replaceAll(stackNameRegex, "-").toLowerCase().take(60)
        ciPipeline (
            project: project,
            ciImages: [[ name: "vibrent/empattendance" ]],
            versionedArtifacts: [env.PROJECT],
            ciArtifacts: artifacts,
            umbrellaCharts: [], //['acadia'],
            addToUmbrella: false, //true,
            checkout: {
                checkout scm
            },
            charts: {
                [[
                    "chart": "charts/empattendance",
                    "ciOverrides": [

                      "empattendanceKafka": ["enabled": true],

                      "empattendance_reg": "cireg",
                      "empattendance_version": version,
                      "global": ["cluster": true, "pullPolicy": "Always"]
                    ],
                    "devOverrides": [
                      "empattendance_reg": "reg",
                      "empattendance_version": "${branch}-${utils.getShortCommitSha()}",
                      "global": ["cluster": true, "pullPolicy": "Always"]
                    ]
                ]]
            },
            build: { failableStage ->
                failableStage('Build') {
                    container('maven11') {
                        sh "mvn -T8 clean install -DskipTests"
                    }
                }
            },
            unitTest: { failableStage ->
                container('maven11') {
                    sh "mvn -T8 test"
                }
            },
            sonar: { failableStage ->
                runSonarAnalysis (project: env.PROJECT, tool: 'scanner', sonarBranch: env.BRANCH_NAME, projectVersion: version)
            },
            deploy: { failableStage ->
                helmUtils.installChart(
                    chart: 'devcharts/secrets', namespace: stackName,
                    releaseName: "${stackName}-secrets", timeout: 300,
                    stageFunc: failableStage
                )
                helmUtils.installChart(chart: "charts/empattendance", namespace: stackName, releaseName: stackName, valuesFile: 'charts/empattendance/values.yaml', timeout: 1000, stageFunc: failableStage)

            },
            test: { failableStage ->
                container('docker') {
                  sh "./tests/integration.sh"
                }

            }
        )
    }
}
