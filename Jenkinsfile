pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'development', url: 'https://github.com/kallam540/master.git'
            }
        }
        stage('Build') {
            steps {
                sh "${tool name: 'clean package', type: 'maven'}/bin/mvn package"
            }
        }
        stage('Test') {
            steps {
                echo 'Hello World'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Hello World'
            }
        }
    }
   
}
