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
                'Deploy',
                'Remove'
            ],
            description: 'Select pipeline action'
        )

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





        stage('Check Files') {

            steps {

                sh '''

                echo "Checking project files"

                ls -la


                echo "Checking Kubernetes files"

                ls -la kubernetes


                '''

            }

        }





        stage('Build Application') {

            when {

                expression {
                    params.ACTION == 'Build'
                }

            }


            steps {

                sh '''

                echo "Building Spring Boot Application"

                mvn clean package -DskipTests


                '''

            }

        }





        stage('Build Docker Image') {

            when {

                expression {
                    params.ACTION == 'Build'
                }

            }


            steps {

                sh '''

                echo "Building Docker Image"


                docker build \
                -t ${IMAGE_NAME}:${IMAGE_TAG} .


                '''

            }

        }





        stage('Push Docker Image') {

            when {

                expression {
                    params.ACTION == 'Build'
                }

            }


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


                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} \
                    ${IMAGE_NAME}:latest



                    docker push ${IMAGE_NAME}:${IMAGE_TAG}


                    docker push ${IMAGE_NAME}:latest


                    '''


                }

            }

        }





        stage('Deploy Kubernetes') {

            when {

                expression {
                    params.ACTION == 'Deploy'
                }

            }


            steps {

                sh '''

                echo "Deploying Application and Database"


                kubectl apply -f kubernetes/k8s-deployment.yaml



                echo "Checking Kubernetes Status"


                kubectl get pods

                kubectl get svc


                '''

            }

        }





        stage('Remove Kubernetes') {

            when {

                expression {
                    params.ACTION == 'Remove'
                }

            }


            steps {


                sh '''

                echo "Removing Application and Database"


                kubectl delete -f kubernetes/k8s-deployment.yaml \
                --ignore-not-found=true



                echo "Resources Removed"


                '''

            }

        }





        stage('Verify Deployment') {

            when {

                expression {
                    params.ACTION == 'Deploy'
                }

            }


            steps {

                sh '''

                echo "Deployment Status"


                kubectl get deployments


                kubectl get pods


                kubectl get svc


                '''

            }

        }


    }



    post {


        success {

            echo "================================"
            echo " Pipeline Completed Successfully "
            echo "================================"

        }


        failure {

            echo "================================"
            echo " Pipeline Failed "
            echo " Check Jenkins Logs "
            echo "================================"

        }


        always {

            cleanWs()

        }

    }

}
