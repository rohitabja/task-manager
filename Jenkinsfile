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
    stage('Test') {
      post {
        always {
          junit 'target/surefire-reports/*.xml'

        }

      }
      steps {
        sh 'mvn test'
      }
    }
    stage('Docker build') {
       steps {
         echo "current build number: ${currentBuild.number}"
         sh 'docker build . -t task-manager:${currentBuild.number}'
       }
    }
    stage('Docker Run') {
       steps {
          sh 'docker run -p 8081:8081 --name task-manager:${currentBuild.number} --link mysql-taskmanager-v1:mysql -d task-manager:${currentBuild.number}'
       }
    }
  }
}