pipeline {
    agent any

    parameters {
        choice(name: "service", choices: ["front-ui", "account-service", "identity-provider", "postgre-db"], description: "Choose service for build and deploy")
        choice(name: "namespace", choices: ["dev", "test", "prod"], description: "Choose namespace")
    }

    environment {
        HELM_RELEASE_EXIST = powershell(script: "helm status $params.service -n $params.namespace", returnStatus: true)
    }

    stages {
        stage('Build jar') {
            steps {
                isNeedBuild(params.service) {
                    dir(params.service) {
                        echo "Building $params.service"
                        powershell "mvn clean install"
                    }
                }
            }
        }
        stage('Build docker image') {
            steps {
                isNeedBuild(params.service) {
                    dir(params.service) {
                        powershell "minikube image build -t $params.service ."
                    }
                }
            }
        }
        stage('Deploy new release') {
            when { expression { return HELM_RELEASE_EXIST == "1" } }
            steps {
                dir('extra/helm') {
                    powershell "helm install $params.service ./$params.service --namespace=$params.namespace"
                }
            }
        }
        stage('Deploy update') {
            when { expression { return HELM_RELEASE_EXIST == "0" } }
            steps {
                dir('extra/helm') {
                    powershell "helm upgrade $params.service ./$params.service --namespace=$params.namespace"
                }
            }
        }
    }
}

def isNeedBuild(serviceName, Closure steps) {
    if (serviceName == 'postgre-db' || serviceName == 'identity-provider') {
        echo "We not use source-code of $params.service and don't need to build it. Skip step"
    } else {
        steps.call()
    }
}