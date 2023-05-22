pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh './gradlew build'
      }
    }

    stage('sonarqube') {
      steps {
        withSonarQubeEnv(installationName: 'sonarqube-server', credentialsId: '371bf57e-6472-4aa8-b788-d4636a1b6e85') {
          sh './gradlew sonar'
        }

      }
    }

    stage('deploy') {
      steps {
        build 'stage4-task1-deploy'
      }
    }

  }
}