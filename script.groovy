def buildImage(String imageName, String IMAGE_TAG, String docker_credentials){
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

def pushImage (String imageName, String IMAGE_TAG){
     echo "pushing image to dockerhub ..."
                    sh "docker push ${imageName}:${IMAGE_TAG}"
}

return this 