pipeline {
    agent any

    parameters {
        choice(name: "namespace", choices: ["dev", "test", "prod"], description: "Choose namespace")
        booleanParam(name: "need_deploy_to_namespace", defaultValue: false, description: "Job create new template and deploy kafka to namespace")
        booleanParam(name: "create_topic", defaultValue: false, description: "If enable - auto-create topic")
        string(name: "topic_name")
    }

    environment {
        HELM_RELEASE_EXIST = powershell(script: "helm status kafka -n $params.namespace", returnStatus: true)
    }

    stages {
        stage('Deploy new release') {
            when { expression { return HELM_RELEASE_EXIST == "1" && params.need_deploy_to_namespace } }
            steps {
                script {
                    def targetDir = "extra/helm/bank-app/charts"
                    dir(targetDir) {
                      powershell "helm install kafka ./kafka --namespace=$params.namespace"
                    }
                }
            }
        }
        stage('Deploy update') {
            when { expression { return HELM_RELEASE_EXIST == "0" && params.need_deploy_to_namespace } }
            steps {
                script {
                    def targetDir = "extra/helm/bank-app/charts"
                    dir(targetDir) {
                      powershell "helm upgrade kafka ./kafka --namespace=$params.namespace"
                    }
                }
            }
        }
        stage('Create topic') {
            when { expression { return params.create_topic } }
            steps {
                powershell "kubectl exec -n $params.namespace kafka-0 -- ./opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka-svc:9092 --create --topic $params.topic_name"
            }
        }
    }
}