pipeline {

    agent any


    tools {
        jdk 'JDK21'
        maven 'maven'
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


        stage('Build Spring Boot Application') {

            steps {

                sh '''
                    mvn clean package -DskipTests
                '''

            }
        }


        stage('Build Docker Image') {

            steps {

                sh """

                    docker build \
                    -t ${IMAGE_NAME}:${BUILD_TAG} .

                """

            }
        }



        stage('Login & Push Docker Image') {

            steps {


                withCredentials([

                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )

                ]) {


                    sh '''

                    echo $DOCKER_PASS | docker login \
                    -u $DOCKER_USER \
                    --password-stdin

                    '''

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




        stage('Deploy Database') {

            steps {

                sh '''

                    kubectl apply -f kubernetes/mysql-deployment.yaml

                    kubectl apply -f kubernetes/mysql-service.yaml

                '''

            }

        }




        stage('Deploy Application') {

            steps {

                sh '''

                    kubectl apply -f kubernetes/app-deployment.yaml

                    kubectl apply -f kubernetes/app-service.yaml

                '''

            }

        }




        stage('Verify Deployment') {

            steps {


                sh '''

                    echo "Checking Kubernetes Pods..."

                    kubectl get pods


                    echo "Checking Services..."

                    kubectl get svc

                '''

            }

        }


    }



    post {


        success {

            echo "CI/CD Pipeline completed successfully."

        }


        failure {

            echo "Pipeline failed. Check logs."

        }


        always {

            cleanWs()

        }


    }

}
