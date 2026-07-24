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
            description: 'Select Pipeline Action'
        )
    }


    environment {

        IMAGE_NAME = "gouri22/stock-market"
        IMAGE_TAG = "${BUILD_NUMBER}"

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

            when {

                expression {
                    params.ACTION == 'Build'
                }

            }


            steps {

                sh '''
                    mvn clean package -DskipTests
                '''

            }

        }





        stage('Build & Push Docker Image') {


            when {

                expression {
                    params.ACTION == 'Build'
                }

            }


            steps {


                sh '''

                docker build \
                -t ${IMAGE_NAME}:${IMAGE_TAG} .


                docker tag \
                ${IMAGE_NAME}:${IMAGE_TAG} \
                ${IMAGE_NAME}:latest

                '''



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


                    docker push ${IMAGE_NAME}:${IMAGE_TAG}

                    docker push ${IMAGE_NAME}:latest


                    '''

                }



                sh '''

                docker logout

                docker image prune -f

                '''

            }

        }





        stage('Deploy Database') {


            when {

                expression {
                    params.ACTION == 'Deploy Database'
                }

            }


            steps {


                sh '''

                kubectl apply \
                -f kubernetes/database.yaml

                '''

            }

        }





        stage('Deploy Application') {


            when {

                expression {
                    params.ACTION == 'Deploy Application'
                }

            }


            steps {


                sh '''

                kubectl apply \
                -f kubernetes/deployment.yaml


                kubectl apply \
                -f kubernetes/service.yaml


                '''

            }

        }





        stage('Remove Application') {


            when {

                expression {
                    params.ACTION == 'Remove Application'
                }

            }


            steps {


                sh '''

                kubectl delete \
                -f kubernetes/deployment.yaml \
                --ignore-not-found=true


                kubectl delete \
                -f kubernetes/service.yaml \
                --ignore-not-found=true


                '''

            }

        }





        stage('Remove Database') {


            when {

                expression {
                    params.ACTION == 'Remove Database'
                }

            }


            steps {


                sh '''

                kubectl delete \
                -f kubernetes/database.yaml \
                --ignore-not-found=true


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
