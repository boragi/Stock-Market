pipeline {
    agent any

    parameters {
        choice(
            name: 'ACTION',
            choices: ['DEPLOY', 'REMOVE'],
            description: 'Choose whether to deploy or remove the application'
        )
    }

    tools {
        maven 'maven'
    }

    environment {
        IMAGE_NAME = "stock"
        IMAGE_TAG = "latest"
    }

    stages {

        stage('Checkout Code') {
            when {
                expression { params.ACTION == 'DEPLOY' }
            }
            steps {
                echo "Checking out source code..."
                git branch: 'main',
                    url: 'https://github.com/boragi/Stock-Market.git'
            }
        }

        stage('Build JAR') {
            when {
                expression { params.ACTION == 'DEPLOY' }
            }
            steps {
                echo "Building Spring Boot project..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            when {
                expression { params.ACTION == 'DEPLOY' }
            }
            steps {
                echo "Building Docker image..."
                sh """
                    docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                """
            }
        }

        stage('Push Image to Docker Hub') {
            when {
                expression { params.ACTION == 'DEPLOY' }
            }
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'docker-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {

                    sh """
                        echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin

                        docker tag ${IMAGE_NAME}:${IMAGE_TAG} \$DOCKER_USERNAME/${IMAGE_NAME}:${IMAGE_TAG}

                        docker push \$DOCKER_USERNAME/${IMAGE_NAME}:${IMAGE_TAG}
                    """
                }
            }
        }

        stage('Deploy Application') {
            when {
                expression { params.ACTION == 'DEPLOY' }
            }
            steps {
                echo "Deploying application..."

                sh '''
                    docker compose down || true

                    docker image prune -f

                    docker compose up -d --build

                    docker ps
                '''
            }
        }

        stage('Remove Application') {
            when {
                expression { params.ACTION == 'REMOVE' }
            }
            steps {
                echo "Removing application..."

                sh '''
                    docker compose down || true

                    docker rm -f springboot-app mysql-container || true

                    docker image rm stock:latest || true

                    docker image prune -af

                    docker volume prune -f
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
            sh 'docker ps -a || true'
            echo "Pipeline completed."
        }
    }
}
