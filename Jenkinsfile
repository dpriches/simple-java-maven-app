@Library('shared-library') _
 
pipeline {
    agent any
    stages {
        stage('Git Checkout') {
            steps {
                gitCheckout ( branch: "master", url: "https://github.com/dpriches/simple-java-maven-app")
            }
        }
    }
}