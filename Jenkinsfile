pipeline {

    agent any

    stages {

        stage('Checkout'){
            steps {
                checkout scm
            }
        }

        stage('Environment Check'){
            steps {
                bat 'whoami'
                bat 'java -version'
                bat 'mvn -version'
            }
        }

        stage('Run API Tests'){
            steps {
                bat 'mvn clean test'
            }
        }

    }

    post{
        always{
            junit allowEmptyResults: true,
            testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'target/allure-results/**',
            allowEmptyArchive: true

            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
            ])
        }

        success {
            echo 'REST Assured test execution passed.'
        }

        failure {
            echo 'REST Assured test execution failed.'
        }
    }
}