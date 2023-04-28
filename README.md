# SLAS

## Introduction

SLAS is an analysis module implemented as a MAVEN project in JAVA (JDK). The SLAS software consists of 8 submodules, namely SLAS Commons, SLAS Client, SLAS API Service and SLAS CA, EN, ES, DE and EL. These submodules are publicly available on GitHub at https://github.com/WELCOME-project/SLAS.

## Installation, Deployment, and Execution

### Installation and Deployment

#### Software Requirements

The WELCOME SLAS installation requires the use of Docker and Docker Compose. Docker is used to package SLAS as images that can be run in a container. The `.yml` configuration file and Docker Compose are used to instantiate Docker images as containers. The `.yml` file specifies the virtual network details and other necessary configurations.

#### Hardware Requirements

The images for SLAS API Service and Demo require a maximum of 1 GB of memory. And 1 CPU.
The images for the SLAS languages requires a maximum of 4 GB of memory to run. 
All language images can utilize multiple threads, so having several CPU cores available is preferred. In the example configuration provided below, up to 4 CPU cores are used.

#### Deployment

To run SLAS, several images will have to be created, one for SLAS API Service and one for each SLAS language you want to deploy.

The configuration of the SLAS deployment is specified in the `.yml` file. One can use an existing image by specifying its name and tag in the `.yml` file to run the corresponding SLAS container.

To create an image from scratch, access the project directory and run the command:
```
docker build -f Dockerfile -t name_of_the_image:tag .
```
Then upload it to a Maven repository with the command:
```
docker push name_of_the_image:tag
```
Use this image inside the `.yml` file.

#### Example `compose.yml` for Deployment

Here is an example `compose.yml` file that can be used for deployment. Adjust the image tags and port mapping as required:

```
version: '3.2'
services:
  slas-api:
    image: registry.gitlab.com/talnupf/welcome/slas-api:2023-02-01
    deploy:
      replicas: 1
      resources:
        limits:
          cpus: "1"
          memory: 1GB
      ports:
        - "8080:8080"
    environment:
      - LANGUAGE_SERVICES_CONFIGURATION_JSON={"language_services_map":{"en":"http://slas-en:8080/slas-services-en/api/dla","ca":"http://slas-ca:8080/slas-services-ca/api/ca","de":"http://slas-de:8080/slas-services-de/api/de","el":"http://slas-el:8080/slas-services-el/api/el","es":"http://slas-es:8080/slas-services-es/api/es"}}
  slas-(any_of_the_languages):
    image: registry.gitlab.com/talnupf/welcome/slas-(any_of_the_languages):2023-02-01
    deploy:
      replicas: 1
      resources:
        limits:
          cpus: "4"
          memory: 4GB
      ports:
        - "8080:8080"
    environment:
      - DBPEDIA_ENDPOINT=<url_to_dbpedia_service>
      - CONCEPT_URL=<url_to_concept_extraction_service>
      - NER_URL=<url_to_ner_service>
      - BABELNET_URL=<url_to_babelnet_service>
      - SPEECHACT_URL=<url_to_speechact_service>
      - GEOLOCATION_URL=<url_to_geolocation_service>
      - TAXONOMY_PATH=/resources/taxonomy.ttl
      - TAXONOMY_SERVICE_URL=<url_to_taxonomy_service>
```

### Execution

The deployed SLAS components can run in the cloud infrastructure of the installed WELCOME platform or be deployed externally. Make the corresponding adjustments in the dispatcher configuration to point to the appropriate service URL.

The service is accessible through a REST-like API at `http://<base_url>/slas-api/` where `<base_url>` corresponds to the location of the deployment. For example, `http://slas:8080/slas-api/` or `https://welcome-project.upf.edu/slas-api/`. Swagger documentation is available at that endpoint.