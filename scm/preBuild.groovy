def call () {
    echo "call from preBuild.groovy"
    sh "pwd; ls -l"
}

return this