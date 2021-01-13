package edu.upf.taln.welcome.slas.client;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import edu.upf.taln.utils.pojos.uima.OutputFactory.OutputType;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.factories.OutputFactory.OutputLevel;
import edu.upf.taln.welcome.slas.commons.input.AnalysisType;


public class Main {

    private static final String PARAMS_COMMAND = "params";
    
    @Parameters(commandDescription = "Calls the Mindspaces analysis service with the given parameters.")
    private static class ParametrizedCommand implements IClientConfiguration {

        @Parameter(names = { "-l", "--language" }, description = "Input language.", required = true, order = 0)
        private String language = null;

        @Parameter(names = { "-u", "--url" }, description = "Service URL.", required = true, order = 1)
        private String serviceURL = null;

        @Parameter(names = { "-i", "--input-directory" }, description = "Input directory (.txt files).", required = true, order = 2)
        private String inputPath = null;

        @Parameter(names = { "-o", "--output-directory" }, description = "Output directory.", required = true, order = 3)
        private String outputPath = null;

        @Parameter(names = { "-a", "--analysis-type" }, description = "Analysis type, which corresponds to a certain configuration of the pipeline.", order = 4)
        private AnalysisType analysisType = AnalysisType.FULL;

        @Parameter(names = { "-f", "--output-format" }, description = "Output file format.", order = 5)
        private OutputLevel outputType = OutputLevel.xmi;

        @Parameter(names = { "-e", "--skip-errors" }, description = "Skip errors while processing documents.", order = 6)
        private boolean skipErrors = true;

        @Override
        public String getLanguage() {
            return language;
        }

        @Override
        public String getServiceURL() {
            return serviceURL;
        }

        @Override
        public String getInputPath() {
            return inputPath;
        }

        @Override
        public String getOutputPath() {
            return outputPath;
        }

        @Override
        public AnalysisType getAnalysisType() {
            return analysisType;
        }

        @Override
        public OutputLevel getOutputType() {
            return outputType;
        }

        @Override
        public boolean isSkipErrors() {
            return skipErrors;
        }
    }
    
    private static final String CONFIG_COMMAND = "config";
    
    @Parameters(commandDescription = "Calls the Mindspaces analysis service using the given configuration file.")
    private static class ConfigCommand {
     	
        @Parameter(description = "JSON_CONFIG_FILE", required = true)
        private String configurationFile = null;
    }
    
    private static class BaseCmd {
        @Parameter(names = { "-h", "--help" }, description = "Shows usage.", help = true)
        private boolean help;   
        
        @Parameter(names = { "-v", "--version" }, description = "Shows client version.")
        private boolean version;        
    }
    
	public static void main(String[] args) throws Exception {
    	
        BaseCmd baseCmd = new BaseCmd();
        ParametrizedCommand paramsCmd = new ParametrizedCommand();
        ConfigCommand configCmd = new ConfigCommand();
        
		JCommander jCommander = JCommander.newBuilder()
          .addObject(baseCmd)
		  .addCommand(CONFIG_COMMAND, configCmd)
          .addCommand(PARAMS_COMMAND, paramsCmd)
		  .build();
		
		try{
			jCommander.parse(args);
            
            String command = jCommander.getParsedCommand();
			if (baseCmd.version) {
                Package pack = Main.class.getPackage();
                String version = pack.getImplementationVersion();
                
                System.out.println(version);
                
			} else if(baseCmd.help || command == null){
				jCommander.usage();
                
            } else {
                
                Processor processor = null;
                if (command.equals(PARAMS_COMMAND)) {
                    processor = new Processor(paramsCmd, null);
                    
                } else if (command.equals(CONFIG_COMMAND)) {
                    
                    File configFile = new File(configCmd.configurationFile);
                    if (!configFile.exists()) {
                        throw new WelcomeException("Configuration file not found!");
                    }

                    ObjectMapper om = new ObjectMapper();
                    ClientConfiguration config = om.readValue(configFile, ClientConfiguration.class);
                    processor = new Processor(config);
                }
                processor.execute();
            }
			
		} catch(ParameterException ex){
			System.out.println(ex.getMessage());
			jCommander.usage();
		}
    }
}
