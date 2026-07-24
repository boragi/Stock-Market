pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'maven'
    }

    parameters {
        choice(
            name: 'ACTION',
            choices: [
                'Build',
                'Deploy Application',
                'Deploy Database',
                'Remove Application',
                'Remove Database'
            ],
            description: 'Select the action to perform'
        )
    }

    environment {
        IMAGE_NAME = "gouri22/stock-market"
        BUILD_TAG = "${BUILD_NUMBER}"
    }

    stages {

        stage('Checkout Source') {
            steps {
                deleteDir()

                git branch: 'main',
                    url: 'https://github.com/boragi/Stock-Market.git'
            }
        }

        stage('Build & Push Docker Image') {
            when {
                expression { params.ACTION == 'Build' }
            }

            steps {

                sh 'mvn clean package -DskipTests'

                sh """
                docker build -t ${IMAGE_NAME}:${BUILD_TAG} .
                """

                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {

                    sh """
                    echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                    """

                }

                sh """
                docker tag ${IMAGE_NAME}:${BUILD_TAG} ${IMAGE_NAME}:latest
                docker push ${IMAGE_NAME}:${BUILD_TAG}
                docker push ${IMAGE_NAME}:latest
                docker rmi ${IMAGE_NAME}:${BUILD_TAG} || true
                docker rmi ${IMAGE_NAME}:latest || true
                docker image prune -f
                """

            }
        }

        stage('Deploy Application') {
            when {
                expression { params.ACTION == 'Deploy Application' }
            }

            steps {

                sh '''
                kubectl apply -f kubernetes/k8s.yaml
                '''

            }
        }

        stage('Deploy Database') {
            when {
                expression { params.ACTION == 'Deploy Database' }
            }

            steps {

                sh '''
                kubectl apply -f kubernetes/k8s.yaml
                '''

            }
        }

        stage('Remove Application') {
            when {
                expression { params.ACTION == 'Remove Application' }
            }

            steps {

                sh '''
                kubectl delete -f kubernetes/service.yaml --ignore-not-found=true
                kubectl delete -f kubernetes/deployment.yaml --ignore-not-found=true
                '''

            }
        }

        stage('Remove Database') {
            when {
                expression { params.ACTION == 'Remove Database' }
            }

            steps {

                sh '''
                kubectl delete -f kubernetes/mysql-service.yaml --ignore-not-found=true
                kubectl delete -f kubernetes/mysql-statefulset.yaml --ignore-not-found=true
                kubectl delete -f kubernetes/mysql-pvc.yaml --ignore-not-found=true
                kubectl delete -f kubernetes/mysql-pv.yaml --ignore-not-found=true
                '''

            }
        }

    }

    post {

        success {
            echo "Pipeline executed successfully."
        }

        failure {
            echo "Pipeline execution failed."
        }

        always {
            cleanWs()
        }

    }
}
