pipeline {
	agent any
	
 	environment {
		BUILD_TOOLS_REPO_URL = 'git@github.com:dpriches/build_tools.git'
		BUILD_TOOLS_CRED_ID  = 'f9db046c-3781-4562-874d-913f43d03e24'
	}

	stages {	

// Do some prep work for the build
		stage('Prep') {
			
            steps {
				// Get the list of target environments to deploy to.
                script {
					String[] targetEnv = params.TARGET_ENV.tokenize (",")
					echo "# envs - ${targetEnv.size()}"
					for (String s: targetEnv) { 
						echo "Parameter $s" 
					}
				}

				// Get the tools directory for install scripts, etc
                checkout([  
					$class: 'GitSCM', 
					branches: [[name: 'refs/heads/master']], 
					doGenerateSubmoduleConfigurations: false, 
					extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: '_scm-tools']], 
					submoduleCfg: [], 
					userRemoteConfigs: [[ credentialsId: "${BUILD_TOOLS_CRED_ID}", url: "${BUILD_TOOLS_REPO_URL}" ]]
				])
			}
		}

// If this is the qa job that creates a release, run the commands to create a new branch
        stage('CreateBranch') {
			when {
				expression { 
					env.JOB_NAME.endsWith('qa')
				}
			}
			steps {
				script {
					sh 'chmod +x _scm-tools/tools/git-branch.sh;./_scm-tools/tools/git-branch.sh -d . -s master'
				}
				
			}
		}

// Build the app without running tests. Separates compilation errors from test errors		
		stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

// Do some unit testing
//        stage('Test') {
//            steps {
//                sh 'mvn test'
//            }
//            post {
//                always {
//                    junit '**/target/surefire-reports/*.xml'
//                }
//            }
//        }


// If pass, upload artifact to repo server
        stage('UploadArtifact') {
            steps {
                sh 'mvn -DskipTests deploy'
            }
        }

// Create rpms
        stage('GenerateRpms') {
            steps {
                sh 'mvn deploy -P create-rpms -f create-rpms/pom.xml'
            }
        }

// Do cleanup
 		stage('Cleanup') {
			steps {
				step ([$class: 'WsCleanup'])
			}
		}
	}
}
