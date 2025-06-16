pipeline {
    agent any

    parameters {
        choice(name: "service", choices: ["bank-app", "front-ui", "account-service", "blocker-service", "cash-service", "exchange-service", "exchange-generator-service", "notification-service", "transfer-service", "identity-provider", "postgre-db"], description: "Choose service for build and deploy")
        choice(name: "namespace", choices: ["dev", "test", "prod"], description: "Choose namespace")
        booleanParam(name: "skip_test", defaultValue: false, description: "Skip test on service build")
    }

    environment {
        HELM_RELEASE_EXIST = powershell(script: "helm status $params.service -n $params.namespace", returnStatus: true)
    }

    stages {
        stage('Build jar') {
            when { expression { return params.service != "bank-app" } }
            steps {
                isNeedBuild(params.service) {
                    dir(params.service) {
                        echo "Building $params.service"
                        script {
                            if (params.skip_test) {
                                powershell "mvn clean install -DskipTests"
                            } else {
                                powershell "mvn clean install"
                            }
                        }
                    }
                }
            }
        }
        stage('Build all depended jar') {
            when { expression { return params.service == "bank-app" } }
            steps {
                script {
                    dir("extra/helm/bank-app/charts") {
                        def files = findFiles()
                        files.each { f ->
                            dir("../../../../${f.name}") {
                                isNeedBuild(f.name) {
                                    echo "Building ${f.name}"
                                    script {
                                        if (params.skip_test) {
                                            powershell "mvn clean install -DskipTests"
                                        } else {
                                            powershell "mvn clean install"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        stage('Build docker image') {
            when { expression { return params.service != "bank-app" } }
            steps {
                isNeedBuild(params.service) {
                    dir(params.service) {
                        powershell "minikube image build -t $params.service ."
                    }
                }
            }
        }
        stage('Build all depended docker image') {
            when { expression { return params.service == "bank-app" } }
            steps {
                script {
                    dir("extra/helm/bank-app/charts") {
                        def files = findFiles()
                        files.each { f ->
                            dir("../../../../${f.name}") {
                                isNeedBuild(f.name) {
                                    powershell "minikube image build -t ${f.name} ."
                                }
                            }
                        }
                    }
                }
            }
        }
        stage('Deploy new release') {
            when { expression { return HELM_RELEASE_EXIST == "1" } }
            steps {
                script {
                    def targetDir = (params.service == "bank-app") ? "extra/helm" : "extra/helm/bank-app/charts"
                    dir(targetDir) {
                      powershell "helm install $params.service ./$params.service --namespace=$params.namespace"
                    }
                }
            }
        }
        stage('Deploy update') {
            when { expression { return HELM_RELEASE_EXIST == "0" } }
            steps {
                script {
                    def targetDir = (params.service == "bank-app") ? "extra/helm" : "extra/helm/bank-app/charts"
                    dir(targetDir) {
                      powershell "helm upgrade $params.service ./$params.service --namespace=$params.namespace"
                    }
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