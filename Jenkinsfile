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

        stage('Check Committer') {
            steps {
                script {
                    // Check if the build was triggered manually
                    def isManualBuild = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')

                    // Skip the committer check for manual builds
                    if (!isManualBuild) {
                        // Get the committer's email or username
                        def committerEmail = sh(script: 'git log -1 --pretty=format:"%ae"', returnStdout: true).trim()
                        def committerName = sh(script: 'git log -1 --pretty=format:"%an"', returnStdout: true).trim()

                        // Stop the build if the commit was made by Jenkins
                        if (committerEmail == "jenkins@example.com" || committerName == "jenkins") {
                            error "Stopping build because the commit was made by Jenkins."
                        } else {
                            echo "Proceeding with the build because the commit was not made by Jenkins."
                        }
                    } else {
                        echo "Build was triggered manually. Skipping committer check."
                    }
                }
            }
        }

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
                        sh "scp docker-compose.yaml ec2-user@35.180.196.12:/home/ec2-user "
                        sh "ssh -o StrictHostKeyChecking=no ec2-user@35.180.196.12 ${dockerCmd}"
                    }
                }
            }
        }

        stage('commit version update') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        // git config here for the first time run
                        sh 'git config --global user.email "jenkins@example.com"'
                        sh 'git config --global user.name "jenkins"'

                        sh "git remote set-url origin https://${USER}:${PASS}@github.com/BenSassiAhmed/Node.js-App-with-Docker-Compose-Jenkins-on-EC2.git"
                        sh 'git add .'
                        sh 'git commit -m "ci: version bump"'
                        sh 'git push origin HEAD:main'
                    }
                }
            }
        }
    } 
}
