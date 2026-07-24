pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'maven'
    }


    environment {

        IMAGE_NAME = "gouri22/stock-market"
        IMAGE_TAG = "${BUILD_NUMBER}"

    }


    stages {


        stage('Checkout Source') {

            steps {

                deleteDir()

                git(
                    branch: 'main',
                    url: 'https://github.com/boragi/Stock-Market.git'
                )

            }
        }



        stage('Build Spring Boot Application') {

            steps {

                sh '''
                    echo "Building Spring Boot Application"

                    mvn clean package -DskipTests

                    ls -la target
                '''

            }
        }




        stage('Build Docker Image') {

            steps {

                sh '''

                    echo "Building Docker Image"

                    docker build \
                    -t ${IMAGE_NAME}:${IMAGE_TAG} .

                '''

            }
        }




        stage('Docker Login & Push') {

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


                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest


                    docker push ${IMAGE_NAME}:${IMAGE_TAG}

                    docker push ${IMAGE_NAME}:latest


                    '''


                }

            }

        }




        stage('Deploy MySQL Database') {

            steps {

                sh '''

                echo "Deploying MySQL"


                kubectl apply -f kubernetes/mysql-deployment.yaml

                kubectl apply -f kubernetes/mysql-service.yaml


                echo "Waiting for MySQL"


                kubectl get pods


                '''

            }

        }





        stage('Deploy Spring Boot Application') {

            steps {

                sh '''

                echo "Deploying Application"


                kubectl apply -f kubernetes/app-deployment.yaml

                kubectl apply -f kubernetes/app-service.yaml


                '''

            }

        }




        stage('Update Kubernetes Image') {

            steps {


                sh '''

                echo "Updating Application Image"


                kubectl set image deployment/stock-market-app \
                stock-market-app=${IMAGE_NAME}:${IMAGE_TAG}



                '''

            }

        }




        stage('Verify Deployment') {

            steps {


                sh '''

                echo "Checking Pods"

                kubectl get pods



                echo "Checking Services"

                kubectl get svc



                echo "Checking Rollout Status"


                kubectl rollout status deployment/stock-market-app


                '''

            }

        }


    }



    post {


        success {

            echo "================================="
            echo " CI/CD Pipeline Successful "
            echo "================================="

        }


        failure {

            echo "================================="
            echo " Pipeline Failed "
            echo "Check Jenkins Console Logs"
            echo "================================="

        }


        always {

            cleanWs()

        }

    }

}
