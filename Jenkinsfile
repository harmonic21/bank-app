pipeline {
    agent any

    parameters {
        choice(name: "service", choices: ["front-ui", "account-service"], description: "Choose service for build and deploy")
        choice(name: "namespace", choices: ["dev", "test", "prod"], description: "Choose namespace")
    }

    stages {
        stage('Build jar') {
            steps {
                dir(params.service) {
                    echo "Building $params.service"
                    powershell "mvn clean install"
                }
            }
        }
        stage('Build docker image') {
            steps {
                dir(params.service) {
                    powershell "minikube image build -t $params.service ."
                }
            }
        }
        stage('Deploy') {
            steps {
                dir('extra/helm') {
                    powershell "helm install $params.service ./$params.service --namespace=$params.namespace"
                }
            }
        }
    }
}