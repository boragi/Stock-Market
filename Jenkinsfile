pipeline {

    agent any


    tools {
        jdk 'JDK21'
        maven 'maven'
    }


    environment {

        IMAGE_NAME = "gouri22/stock-market"
        IMAGE_TAG  = "${BUILD_NUMBER}"

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



        stage('Verify Project Files') {

            steps {

                sh '''

                echo "Checking Project Structure"

                pwd

                ls -la

                echo "Checking Kubernetes Files"

                ls -la kubernetes

                '''

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





        stage('Push Docker Image') {

            steps {


                withCredentials([

                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )

                ]) {


                    sh '''

                    echo "Docker Login"


                    echo $DOCKER_PASS | docker login \
                    -u $DOCKER_USER \
                    --password-stdin



                    echo "Tagging Image"


                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} \
                    ${IMAGE_NAME}:latest



                    echo "Pushing Images"



                    docker push ${IMAGE_NAME}:${IMAGE_TAG}


                    docker push ${IMAGE_NAME}:latest


                    '''

                }

            }

        }





        stage('Deploy Kubernetes') {

            steps {


                sh '''

                echo "Deploying Kubernetes Resources"



                kubectl apply -f kubernetes/k8s-deployment.yaml



                echo "Kubernetes Resources Created"



                kubectl get pods

                kubectl get svc



                '''

            }

        }





        stage('Update Application Image') {

            steps {


                sh '''

                echo "Updating Application Image"



                kubectl set image deployment/stock-market-app \
                stock-market-app=${IMAGE_NAME}:${IMAGE_TAG} || true



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



                echo "Checking Deployment Status"



                kubectl get deployments



                '''

            }

        }


    }



    post {


        success {

            echo "================================="
            echo " CI/CD Pipeline Completed "
            echo "================================="

        }


        failure {

            echo "================================="
            echo " Pipeline Failed "
            echo " Check Jenkins Console Logs "
            echo "================================="

        }


        always {

            cleanWs()

        }


    }

}
