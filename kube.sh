#!/usr/bin/env bash

## BEGIN SHARED_LOGIC
export NEXUS_USER=vibrent-dev
export NEXUS_PASS=sGSv9bhjNwZM

case $(uname) in
MINGW*)
    os=WINDOWS
    docker_link="https://docs.docker.com/docker-for-windows"
    ;;
Darwin)
    os=MACOS
    docker_link="https://docs.docker.com/docker-for-mac"
    ;;
*)
    echo "Unsupported OS..."
    exit
    ;;
esac

function installDependencies() {
    git clone git@github.com:VibrentHealth/dev-tools.git && cd dev-tools
    ./scripts/install-deps.sh
    cd ../ && rm -rf dev-tools
}

function updateHosts() {
    local host_route="127.0.0.1 `kubectl get ingress -o=jsonpath={.items[*].spec.rules[*].host}`"
    case $(uname) in
    MINGW*)
      echo "Modify C:\Windows\System32\drivers\etc\hosts to expose applications at ingress hosts"
      cat /c/Windows/System32/drivers/etc/hosts | grep -v admin.localhost | tee /c/Windows/System32/drivers/etc/hosts
      echo $host_route | tee -a /c/Windows/System32/drivers/etc/hosts
        ;;
    *)
      echo "Modify /etc/hosts to expose applications at ingress hosts"
      cat /etc/hosts | grep -v admin.localhost | sudo tee /etc/hosts
      echo $host_route | sudo tee -a /etc/hosts
        ;;
  esac
}

function showHelp {
  # TODO: write help
  echo "Available options are:"
  echo "    elastic       - Install ElasticSearch into Local Kubernetes if required and not installed by default"
  echo "    minio         - Install S3 into Kubernetes if required and not already installed."
  echo "    ka            - Install Kafka into kubernetes if required and not already installed."
  echo "    install       - Install full dependencies"
  echo "    build|rebuild - Build or rebuild"
  echo "    quick-rebuild - Not implemented"
  echo "    run|start|dev - Run the application in dev mode"
  echo "    stop          - Not implemented"
  echo "    dump          - Dumps database if exists"
  echo "    clean         - Clean the build output"
  echo "    nuke          - Start everything afresh"
  echo "    update-hosts  - Update hosts entry"
  echo "    help          - Show this help"
  exit 1;
}

function checkForDocker() {
    which docker > /dev/null
    if [ "$?" -ne 0 ]; then
        echo "Docker is not installed! Please install Docker for Mac or Windows and enable Kubernetes"
        exit 1
    fi
    docker version > /dev/null
    if [ "$?" -ne 0 ]; then
        echo "Docker is not started! Please open Docker for Mac or Windows and enable Kubernetes"
        exit 1
    fi
}

function checkForKubernetesInDocker() {
    local CURRENT_CONTEXT=$(kubectl config current-context)
    if [[ "${CURRENT_CONTEXT}" != "docker-for-desktop" ]]; then
        echo "Your kubectl is facing ${CURRENT_CONTEXT}!
             "
        echo "Please enable kubernetes in docker-for-desktop &"
        echo "Please face your kubectl to docker-for-desktop using the Docker GUI or enter:

              kubectl config use-context docker-for-desktop
              "
        exit 1
    fi
}

function setupIngress() {
    helm upgrade ingress stable/nginx-ingress --version=1.26.2 --set controller.image.tag=0.26.1 -i --namespace=kube-system > /dev/null
}

function setupDashboard() {
    helm upgrade dashboard -i stable/kubernetes-dashboard \
            --namespace=kube-system \
            --set ingress.enabled=true \
            --set ingress.hosts[0]=dashboard.localhost \
            --set enableInsecureLogin=true \
            --set enableSkipLogin=true \
            > /dev/null
}


function addHelmRepo {
    echo "adding devcharts helm repository"
    helm repo add devcharts https://nex.vibrenthealth.com/repository/devcharts/ --username ${NEXUS_USER} --password ${NEXUS_PASS}
    echo "adding incubator helm repository"
    helm repo add incubator https://nex.vibrenthealth.com/repository/helmincubator/ --username ${NEXUS_USER} --password ${NEXUS_PASS}
    echo "updating helm repositories"
    helm repo update
}

function checkForHelm() {
    which helm > /dev/null
    if [ "$?" -ne 0 ]; then
        echo "Helm is not installed! Please install Helm (https://helm.sh)"
        exit 1
    fi
    
    addHelmRepo
}

function checkForSkaffold() {
    which skaffold > /dev/null
    if [ "$?" -ne 0 ]; then
        echo "Skaffold is not installed! Please install Skaffold (https://github.com/VibrentHealth/skaffold)"
        exit 1
    fi
}

function commonSetup() {
    checkForDocker
    checkForKubernetesInDocker
    setupIngress
    setupDashboard
    checkForHelm
    checkForSkaffold
}

if [ $# -ne 1 ]; then
  showHelp
fi
## END SHARED_LOGIC


# APP SPECIFIC LOGIC

function installUmbrella() {
    # deploy the umbrella chart from the chart repository
    # disable mission control (for obvious resource reasons)
    # disable this repository apps (because we will be deploying using skaffold)
    helm upgrade default devcharts/acadia --version=0.1.0-develop -i --force --debug --namespace=default --wait --timeout=1400s \
        --set kafka.enabled=true \
        --set insights.enabled=false \
        --set portage.enabled=false \
        --set web-admin.enabled=false \
        --set web-subscriber.enabled=false \
        --set global.mission-control.enabled=false \
        --set global.baseClusterName="localhost" \
        ### ADD APP SPECIFICS HERE
}

function installKafkaStandalone() {
  helm upgrade default-kafka devcharts/kafka --version=2.0.1-vibrent-develop-a07b4ff -i --force --debug --namespace=default --wait -f ./pipeline/kafka-values.yaml
}

function installS3MinioStandalone() {
  helm upgrade secrets -i devcharts/secrets --namespace=default --force
  ## override initContainers cuz we don't expect any data to be in there when we deploy
  helm upgrade hda-s3 devcharts/s3 --set initContainers="" --version=0.2.1-develop -i --force --debug --namespace=default --wait
}

function installElasticStandalone() {
  helm install --name elasticsearch elastic/elasticsearch --set replicas=1 --set imageTag=7.4.0
}


function clean() {
  mvn clean
  helm delete default
  docker system prune
  kubectl delete jobs --all
}

### ADD APP SPECIFICS HERE

function build(){
  # Default build process using Maven
  # Replace line below to provide customized build with desired tool
  echo "Building the application via command mvn clean install"
  echo "  To customize the build process, update the function build() in the file kube.sh"
  mvn clean install
}


case "$1" in
"elastic")
  echo "Installs elastic standalone"
  installElasticStandalone
  ;;

"minio")
  echo "Installs minio as s3"
  installS3MinioStandalone
  ;;

"kafka")
  echo "Installs kafka standalone"
  installKafkaStandalone
  ;;

"install")
  echo "get these from the internet"
  installDependencies
  ;;

"build"|"rebuild")
  ### ADD APP SPECIFICS HERE
  build
  ;;

"quick-rebuild")
  ### ADD APP SPECIFICS HERE
  echo "Please implement a $1 command"
  ;;

"run"|"start"|"dev")
  commonSetup
  
  installKafkaStandalone
  
  skaffold dev
  ;;

"stop")
  echo "This command has been deprecated. Please quit docker-for-desktop to stop the system."
  ;;

"dump")
  kubectl exec $(kubectl get pods -l "service=database" -o name | cut -d"/" -f 2) -- mysqldump -h localhost --user=root --password=password acadia | tail -n +2
  ;;

"clean")
  clean
  ;;

"nuke")
  clean
  echo "
If it is taking extraordinarily long to build and deploy, please ensure that you have allocated enough memory and cpu to the docker-for-desktop application.

If you are having issues, try resetting your kubernetes cluster from the docker-for-desktop GUI Preferences > Reset > Reset Kubernetes Cluster

See ${docker_link} for more details
"
  ;;

"update-hosts")
  updateHosts
  ;;

"help")
  showHelp
  ;;

*)
  echo "$1 is not a valid command"
  showHelp
  ;;

esac

