pipeline {
  agent any
  stages {
    stage('Build') {
      agent {
            docker {
              image 'maven:3-alpine'
              args '-v /root/.m2:/root/.m2'
            }

      }
      steps {
        sh 'mvn -B -DskipTests clean install'
      }
    }
    stage('Docker build') {
       steps {
         sh 'docker build . -t task-manager'
       }
    }
    stage('Docker Run') {
       steps {
          sh 'docker run -p 8081:8081 --name task-manager --link mysql-taskmanager-v1:mysql -d task-manager'
       }
    }
  }
}