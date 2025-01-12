
pipeline{
    agent any
    environment {
        IMAGE_TAG = "${BUILD_NUMBER}"
        docker_credentials = 'docker-hub-repo'
        imageName = 'bensassiahmed/node-app'
    }
    stages{
/* 
        stage("init"){
            steps{
                script{
                    gv = load "script.groovy"
                    echo "Groovy script loaded: ${gv}"
                    
                }
            }
        } */

        stage("build image"){
            steps{
                script{
                    echo "building the docker image..."
                    withCredentials([
                        usernamePassword(
                            credentialsId: "${docker_credentials}" ,
                            passwordVariable: 'PASS', 
                            usernameVariable: 'USER')
                    ]){
                        sh "docker build -t ${imageName}:${IMAGE_TAG} ."
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                }
                        
                    }
            }
        }

        stage("push image"){
            steps{
                script{
                    echo "pushing image to dockerhub ..."
                    sh "docker push ${imageName}:${IMAGE_TAG}"
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
