# Welcome client

This repository contains the code to create a command line client for the UPF TALN Welcome analysis pipeline.

In order to create the executable jar, you'll have to run:  
```bash
mvn compile assembly:single -DskipTests
```

Once you've executed that command, you'll find under `target` directory a jar named `welcome-client.jar`.

This client allow to analyze different text files contained into a specific directory and store the output in different formats (xmi, conll) into an output directory.

There are two options to specify the parameters:
- Using the 'config' command, which requires having a json configuration file specifying the options you want for the analysis.  
  `java -jar welcome-client.jar config JSON_CONFIG_FILE`


- Using the 'params' commmand, which requires to specify the configuration as command line options, i.e.:  
  `java -jar welcome-client.jar params -l LANGUAGE -i INPUT_DIR -o OUTPUT_DIR -u SERVICE_URL -a ANALYSIS_TYPE -f OUTPUT_FORMAT`
