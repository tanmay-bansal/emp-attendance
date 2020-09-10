# empattendance

## Stack dependencies
* Java 11
* Docker
* Kubernetes
* Helm

## getting started
Perform basic setup and configuration of **Local Kubernetes cluster** environment, by following this guide [here](https://agile.vignetcorp.com:8086/confluence/display/DEVOPS/Kubernetes+Access#KubernetesAccess-CLIAccess)

## Installing Pre-requisites
The `kube.sh` script installs following dependencies which are required for **Local Kubernetes cluster**
* Docker
* Kubernetes
* Helm
* Tiller

```bash
./kube.sh install 
```

## Installing Minio [_Optional_]
Minio is an AWS S3 Replacement Service that we can use for development and testing purpose to avoid AWS usage costs.<br/>It is possible that Minio is already installed on the **Local Kubernetes cluster**.<br/>If Minio is not already available on the **Local Kubernetes cluster** it can be installed via following command
```bash
./kube.sh minio
```


## Installing Kafka [_Optional_]
Kafka is a Messaging Service that can be used for sending messages to other systems.<br/>It is possible that Kafka is already installed on the **Local Kubernetes cluster**.<br/>If Kafka is not already available on the **Local Kubernetes cluster** it can be installed via following command
```bash
./kube.sh kafka
```

## Installing ElasticSearch [_Optional_]
ElasticSearch is a Search/Indexing Service that is required by the application to index kafka based payloads.<br/>It is possible that ElasticSearch stack is already installed on the **Local Kubernetes cluster**.<br/>If ElasticSearch is not already available on the **Local Kubernetes cluster** it can be installed via following command
```bash
./kube.sh elastic
```

## How can I access the services or application running in my **Local Kubernetes cluster**

**Local Kubernetes cluster** can be used to run and host many services but it does not expose these services to the host machine.<br/>Often we need to use desktop tools to access various server-side services - eg.
* [Redis Desktop Client](https://github.com/patrikx3/redis-ui/releases) - To check values in Redis running in **Local Kubernetes cluster**
* [MySQL Admin Client UI](https://dev.mysql.com/downloads/workbench/) - to connect to MySQL Database running in **Local Kubernetes cluster**
* [MongoDb Desktop UI](https://robomongo.org/download)  - To Connect to MongoDb running in **Local Kubernetes cluster**
* [Postman](https://www.getpostman.com/downloads/) - To test REST endpoints of services deployed to **Local Kubernetes cluster** 
* [kafkacat](https://github.com/edenhill/kafkacat) - To test Kafka messaging on Kafka server running in **Local Kubernetes cluster**
* [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/install-macos.html) - To interact with S3 Clone(Minio) running in  **Local Kubernetes cluster** 

The commands below will enable apps like above, which are running natively on host machine, to access the services running in **Local Kubernetes cluster**: 

1. Install `kubefwd` tool that forwards all kubernetes services to localhosts
    ```bash
    brew install txn2/tap/kubefwd
    ```
2. Forward all services for host based access, check the output of the command below for hints on host:port combination to use
   ```bash
   sudo kubefwd svc
   ```
   * The above command will start forwarding all the services with their own host names and update the `/etc/hosts` file accordingly.
   * It will also display the hostnames and ports exposed for each service as output

3. The `etc/hosts` file will now be modified with host names for each service in the **local kubernetes cluster** and the GUI tool will have to be configured to use these hostnames/ports to connect to services in **Local Kubernetes Cluster**

## Accessing Minio from Local Machine
If the application is dependent on AWS S3 service, it is possible to simulate the AWS S3 service without actually connecting to AWS.<br/>If the Minio service is installed as described [here](#installing-minio-optional), the following process will provide a way to use [AWS CLI Commands](https://docs.aws.amazon.com/cli/latest/userguide/cli-services-s3-apicommands.html) on the minio running within **Local Kubernetes Cluster**
  
1. Install AWS CLI
    ```bash
    brew install awscli
    ```
2. Configure AWS profile for `aws-cli` such that aws `s3api` commands can be executed against minio running in kubernetes
    ```bash
    aws configure --profile kube-minio
    AWS Access Key ID [None]: SOME_ACCESS_KEY
    AWS Secret Access Key [None]: SOME_SECRET_KEY
    Default region name [None]: us-east-1
    Default output format [None]: text
    ```
3. Use aws commands to check **S3** files in Minio if needed, make sure the `sudo kubefwd svc` command has been started
   ```bash
   # Command to list the buckets
   aws s3api --endpoint-url http://s3:4567 --profile=kube-minio list-buckets | jq '.Buckets[].Name'
   
   # Command to list objects within a bucket
   aws s3api --endpoint-url http://s3:4567 --profile=kube-minio --bucket=root-bucket list-objects-v2 | jq '.Contents[] | .Key'
   ``` 
4. Minio provides a Web UI which can be accessed by opening browser at [http://s3:4567](http://s3:4567) 
   with user/password - `SOME_ACCESS_KEY`/`SOME_SECRET_KEY`

## Development Process

Follow the practices from [Developing and Contributing Guide](CONTRIBUTING.md) before submitting a pull request

## Build

```bash
./kube.sh build
```
The above commands will build and deploy the application to local kubernetes environment. As a fail-safe, a default build command ```mvn clean install``` will be executed if no customization is provided in the `kube.sh` script

## Running the application in **Local Kubernetes Cluster**

A helper script [kube.sh](kube.sh) is provided which has default commands to achieve most of the development tasks.
The available commands can be listed by executing the following:
```bash
./kube.sh help
``` 

Following command will build and deploy the application to **Local Kubernetes Cluster**. 

```bash
./kube.sh dev
```

To deploy this application to cloud environment it must be built through its CI [Continuous Integration] pipeline in Jenkins. 

This project is automatically wired to be picked up by Jenkins server for cloud deployment via a [Jenkinsfile](Jenkinsfile) with multiple steps to deploy the application in _**Remote Kubernetes Cluster**_ under the user's namespace . Any customization to Jenkins build will require modifications to the `Jenkinsfile`

# Code Coverage
The application quality checks are enforced via code coverage rules in the build file [pom.xml](pom.xml)
To customize the code coverage exclusions refer to the [COVERAGE.md](COVERAGE.md) file

# Application Monitoring
Application generates logs and they are pushed to a common ELK stack. Please read the [monitoring guide](MONITORING.md) for more details.
    
---


*This readme is generated using [generator-microsvc](https://github.com/VibrentHealth/generator-microsvc)*
