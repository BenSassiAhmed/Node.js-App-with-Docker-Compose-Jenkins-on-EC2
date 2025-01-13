#!/usr/bin/env groovy

def gv

pipeline{
    agent any
    environment {
        IMAGE_TAG = "${BUILD_NUMBER}"
        docker_credentials = 'docker-hub-repo'
        imageName = 'bensassiahmed/node-app'
    }
    stages{

        stage("init"){
            steps{
                script{
                    gv = load "script.groovy"
                    echo "Groovy script loaded: ${gv}"
                    
                }
            }
        } 

        stage("build image"){
            steps{
                script{
                    gv.buildImage(
                        "${imageName}",
                        "${IMAGE_TAG}",
                        "${docker_credentials}"
                    )     
                    }
            }
        }

        stage("push image"){
            steps{
                script{
                   gv.pushImage(
                    "${imageName}",
                    "${IMAGE_TAG}"
                   )
                }
            }
        }

        stage('Run Docker on EC2') {
            steps {
                script {
                    sh """
                        sed -i 's|bensassiahmed/node-app:.*|bensassiahmed/node-app:${IMAGE_TAG}|g' docker-compose.yaml
                    """
                    // Define the Docker command
                    def dockerCmd = 'docker-compose up -d'
                    // Use SSH Agent to execute the command on the EC2 instance
                    sshagent(credentials: ['ec2-server-key']) {
                        sh "scp docker-compose.yaml ec2-user@35.181.4.178:/home/ec2-user "
                        sh "ssh -o StrictHostKeyChecking=no ec2-user@35.181.4.178 ${dockerCmd}"
                    }
                }
            }
        }

/*
       stage("deploy to ec2") {
            steps {
                script {
                    def dockercmd= 'docker run -d --name app -p 8080:8080 bensassiahmed/project989:jma-${IMAGE_NAME} '
                        sshagent(['ec2-server-key']) {
                            sh "ssh -o StrictHostKeyChecking=no ubuntu@34.228.11.54 ${dockercmd}"
                        }
                    }
                }
            }
        }
*/ 
            
    } 
}
