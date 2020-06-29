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

tag=$(date -I)
# now enjoy the options in order and nicely split until we see --
while true; do
    case "$1" in
        -t|--tag)
            tag="$2"
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

mvn -U clean package -DskipTests
export TAG=$tag

docker build -t registry.gitlab.com/talnupf/welcome/slas/dla:${TAG} . && docker push registry.gitlab.com/talnupf/welcome/slas/dla:${TAG}
