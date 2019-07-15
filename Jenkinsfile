pipeline {
  agent {
      docker {
        image 'maven:3-alpine'
        args '-v /root/.m2:/root/.m2'
      }

  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn -B -DskipTests clean install'
      }
    }
    stage('Docker build') {
       agent any
       steps {
         sh 'docker build . -t task-manager'
       }
    }
    stage('Docker Run') {
       agent any
       steps {
          sh 'docker run -p 8081:8081 --name task-manager --link mysql-taskmanager-v1:mysql -d task-manager'
       }
    }
  }
}