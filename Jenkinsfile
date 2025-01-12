#!/usr/bin/env groovy
def gv

pipeline{
    agent any
    environment {
        IMAGE_TAG = $BUILD_NUMBER

    }
    stages{

        stage("init"){
            steps{
                script{
                    gv = load "script.groovy"
                }
            }
        }

        stage("build image"){
            steps{
                script{
                    gv = buildImage()
                }
            }
        }

        stage("push image"){
            steps{
                script{
                    gv.pushImage()
                }
            }
        }

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
    
}