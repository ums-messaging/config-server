pipeline {
    agent any

    environment {
        AWS_REGION = 'ap-northeast-2'
        TARGET_INSTANCE_ID = 'i-01865e024d7b04019'
        REGISTRY = "registry.ums.local:5000"
        APP_NAME = "config-server"
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        GIT_BRANCH = "${env.BRANCH_NAME ?: 'main'}"
    }

    stages {
        stage('Checkout Gateway Server') {
            steps {
                 script {
                    def branch = env.BRANCH_NAME ?: 'main'
                    sshagent(['git']) {
                         sh """
                            if [ ! -d .git ]; then
                                git init
                                git remote add origin git@github.com:ums-messaging/config-server.git
                                git pull origin $GIT_BRANCH
                            else
                                git fetch origin $GIT_BRANCH
                                git checkout $GIT_BRANCH
                                git pull origin $GIT_BRANCH
                            fi
                         """
                    }
                 }
            }
        }
        stage('Gradle Build') {
            steps {
                script {
                    sh 'nohup aws ssm start-session --target i-00464ff35252824cb --document-name AWS-StartPortForwardingSession --parameters "portNumber=8081,localPortNumber=8081" > ~/ssm-session.log 2>&1 &'
                    sh 'chmod +x gradlew'
                    sh './gradlew build --refresh-dependencies'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build -t ${REGISTRY}/${APP_NAME}:${IMAGE_TAG} .
                    docker login ${REGISTRY} --username jang314 --password jang314
                    docker push ${REGISTRY}/${APP_NAME}:${IMAGE_TAG}
                '''
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker pull ${REGISTRY}/${APP_NAME}:${IMAGE_TAG}
                    docker stop ${APP_NAME}
                    docker rm ${APP_NAME}
                    docker run -d --name ${APP_NAME} -p 8888:8888 ${REGISTRY}/${APP_NAME}:${IMAGE_TAG} -v /data/config-server/aws-keys:/aws-keys:ro
                '''
            }
        }
    }

    post {
        failure {
            echo "❌ 빌드 또는 배포 실패!"
        }
        success {
            echo "✅ 전체 파이프라인 완료!"
        }
    }
}
