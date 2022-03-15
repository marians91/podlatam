#!/bin/bash
echo "Executing build_script.sh"
echo "What is inside the folder:"
ls -alp

# Producing the swagger for api read
echo "Create read only profile..."
mvn clean package -s src/maven-build-settings.xml -f src/docker_virtualentity -Denv.ENV_READ=true -Denv.ENV_WRITE=true
case $? in '0');; *) echo "Producing the swagger for api read Error. Exit"; exit 1;; esac

echo "Copy jar to container dir"
cp src/docker_virtualentity/target/virtualentity-1.0.jar containers/docker_virtualentity
case $? in '0');; *) echo "Error copying jar file. Exit"; exit 1;; esac

cp -R src/docker_virtualentity/target/* /data/repository_src/pipeline/extended_target_builded
