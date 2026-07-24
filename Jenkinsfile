pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    environment {
        IMAGE_NAME = "YOUR_DOCKER_USERNAME/YOUR_APP_NAME"
        BUILD_TAG = "${BUILD_NUMBER}"
    }

    stages {

        stage('Prepare Build') {
            steps {
                echo "Preparing Build..."
                deleteDir()

                git branch: 'main',
                    url: 'YOUR_GITHUB_URL'
            }
        }

        stage('Build Application') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                docker build -t ${IMAGE_NAME}:${BUILD_TAG} .
                """
            }
        }

        stage('Login to Docker Hub') {
            steps {
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
            }
        }

        stage('Change Tag Name') {
            steps {
                sh """
                docker tag ${IMAGE_NAME}:${BUILD_TAG} ${IMAGE_NAME}:latest
                """
            }
        }

        stage('Push Image to Docker Hub') {
            steps {
                sh """
                docker push ${IMAGE_NAME}:${BUILD_TAG}
                docker push ${IMAGE_NAME}:latest
                """
            }
        }

        stage('Clean System') {
            steps {
                sh """
                docker rmi ${IMAGE_NAME}:${BUILD_TAG} || true
                docker rmi ${IMAGE_NAME}:latest || true
                docker image prune -f
                """
            }
        }
    }

    post {
        success {
            echo "Docker Image Successfully Uploaded."
        }

        failure {
            echo "Pipeline Failed."
        }

        always {
            cleanWs()
        }
    }
}
