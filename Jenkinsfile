def gv

pipeline{
    agent any
    environment {
        IMAGE_TAG = "${BUILD_NUMBER}"
        docker_credentials = 'docker-hub-repo'
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
                    gv.buildImage(
                        'bensassiahmed/node-app',
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
                        'bensassiahmed/node-app',
                        "${IMAGE_TAG}"
                    )
                }
            }
        }

/*       stage("deploy to ec2") {
            steps {
                script {
                    def dockercmd= 'docker run -d --name app -p 8080:8080 bensassiahmed/project989:jma-${IMAGE_NAME} '
                        sshagent(['ec2-server-key']) {
                            sh "ssh -o StrictHostKeyChecking=no ubuntu@34.228.11.54 ${dockercmd}"
                        }
                    }
                }
            }
        }*/ 
            
    } 
}
