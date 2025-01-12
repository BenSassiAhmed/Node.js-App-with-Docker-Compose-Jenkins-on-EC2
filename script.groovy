def buildimage(String imageName, String tag, String crendId){
    echo "building the docker image..."
    withCredentials([
        usernamePassword(
            credentialsId: "${crendId}" ,
            passwordVariable: 'PASS', 
            usernameVariable: 'USER')
    ]){
        sh "docker build -t ${imageName}:${tag} ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
    }
}

def pushimage (String imageName, String tag){
    echo "pushing image to dockerhub ..."
    sh "docker push ${imageName}:${tag}"
}