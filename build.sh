#!/bin/bash

OPTIONS=t:l:
LONGOPTS=tag:,langs:

# -regarding ! and PIPESTATUS see above
# -temporarily store output to be able to check for errors
# -activate quoting/enhanced mode (e.g. by writing out “--options”)
# -pass arguments only via   -- "$@"   to separate them correctly
! PARSED=$(getopt --options=$OPTIONS --longoptions=$LONGOPTS --name "$0" -- "$@")
if [[ ${PIPESTATUS[0]} -ne 0 ]]; then
    # e.g. return value is 1
    #  then getopt has complained about wrong arguments to stdout
    exit 2
fi
# read getopt’s output this way to handle the quoting right:
eval set -- "$PARSED"

tag=$(date -I) langs=en,de,ca,el
# now enjoy the options in order and nicely split until we see --
while true; do
    case "$1" in
        -t|--tag)
            tag="$2"
            shift 2
            ;;
        -l|--langs)
            langs="$2"
            shift 2
            ;;
        --)
            shift
            break
            ;;
        *)
            echo "Programming error"
            exit 3
            ;;
    esac
done

git submodule update
mvn -U clean package -DskipTests
export TAG=$tag
IFS=',' read -ra XR_LANGS <<< "$langs"    #Convert string to array

for XR_LANG in "${XR_LANGS[@]}"; do
    docker build --build-arg lang=${XR_LANG} -t maven-taln.upf.edu/welcome/slas_${XR_LANG}:${TAG} .
    docker push maven-taln.upf.edu/welcome/slas_${XR_LANG}:${TAG}
    #docker tag maven-taln.upf.edu/welcome/slas_${XR_LANG}:${TAG} nexus-dockers.everis.com:10110/upf/slas_${XR_LANG}:${TAG} && docker push nexus-dockers.everis.com:10110/upf/slas_${XR_LANG}:${TAG}
    #docker tag maven-taln.upf.edu/welcome/slas_${XR_LANG}:${TAG} registry.gitlab.com/talnupf/welcome/slas/slas_${XR_LANG}:${TAG} && docker push registry.gitlab.com/talnupf/welcome/slas/slas_${XR_LANG}:${TAG}
done

docker build -f Dockerfile.demo -t maven-taln.upf.edu/welcome/slas_demo:${TAG} . && docker push maven-taln.upf.edu/welcome/slas_demo:${TAG}
#docker tag maven-taln.upf.edu/welcome/slas_demo:${TAG} registry.gitlab.com/talnupf/welcome/slas/slas_demo:${TAG} && docker push registry.gitlab.com/talnupf/welcome/slas/slas_demo:${TAG}
docker build -f Dockerfile.api -t maven-taln.upf.edu/welcome/slas_api:${TAG} . && docker push maven-taln.upf.edu/welcome/slas_api:${TAG}
#docker tag maven-taln.upf.edu/welcome/slas_api:${TAG} registry.gitlab.com/talnupf/welcome/slas/slas_api:${TAG} && docker push registry.gitlab.com/talnupf/welcome/slas/slas_api:${TAG}
