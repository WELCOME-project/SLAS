package edu.upf.taln.welcome.slas.client;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeClientException;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.factories.InputFactory;
import edu.upf.taln.welcome.slas.commons.factories.OutputFactory.OutputLevel;
import edu.upf.taln.welcome.slas.commons.input.AnalysisType;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.InputData;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;
import edu.upf.taln.welcome.slas.commons.output.AnalysisOutputImpl;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.output.XmiResult;


/**
 *
 * @author rcarlini
 */
class Processor {
	
	private static Logger logger = Logger.getLogger(Processor.class.getName());

    static Processor createProcessor(String configurationFilePath) throws WelcomeException {

        File configFile = new File(configurationFilePath);
        if (!configFile.exists()) {
            throw new WelcomeException("Configuration file not found!");
        }

        try {
            ObjectMapper om = new ObjectMapper();
            ClientConfiguration config = om.readValue(configFile, ClientConfiguration.class);
            
            return new Processor(config);
            
        } catch (IOException ex) {
            throw new WelcomeException("Invalid configuration file format!", ex);
        }
    }
    
    private IClientConfiguration config;
    private InputMetadata meta;

    public Processor(ClientConfiguration config) throws WelcomeException {
        this(config, config.getMetadata());
    }
    
    public Processor(IClientConfiguration config, InputMetadata metadata) throws WelcomeException {
        this.config = config;
        this.meta = metadata;
        
        if (meta == null) {
            AnalysisType analysisType = config.getAnalysisType();
            OutputLevel outputType = config.getOutputType();
            String language = config.getLanguage();
            if (analysisType == null || outputType == null || language == null) {
                throw new WelcomeException("No metadata or analysisType/outputType/language group found!");
                
            } else {
                meta = InputFactory.createMetadata(analysisType, outputType, language);
            }
        }
    }

	public void execute() throws WelcomeException, WelcomeClientException {
		FilenameFilter filter = (File dir, String name) -> name.endsWith(".txt");
        
        File inputDir = new File(config.getInputPath());
        if (!inputDir.exists()) {
            throw new WelcomeException("Input directory not found!");
		} else if (!inputDir.isDirectory()) {
	    	throw new WelcomeException("Input directory is not a directory!");
	    } else if (inputDir.listFiles(filter).length == 0) {
	    	Logger.getLogger(Processor.class.getName()).log(Level.WARNING, "Input folder does not contain any files.");
	    }
        
        File outputDir = new File(config.getOutputPath());
        if (!outputDir.exists()) {
            throw new WelcomeException("Output directory not found!");
        } else if (!outputDir.isDirectory()) {
        	throw new WelcomeException("Output directory is not a directory!");
        }
        
		execute(meta, inputDir, outputDir);
	}    

    public void execute(InputMetadata meta, File inputDir, File outputDir) throws WelcomeException, WelcomeClientException {
        
        String serviceUrl = config.getServiceURL();
        String language = meta.getLanguage();
        
        WelcomeBackendClient client; 
        switch (meta.getOutputLevel()) {
        case xmi:
            client = new WelcomeBackendClient<>(serviceUrl, language, XmiResult.class);
            break;
            
        default:
        case demo:
            client = new WelcomeBackendClient<>(serviceUrl, language, AnalysisOutputImpl.class);
            break;
        }
        
        FilenameFilter filter = (File dir, String name) -> name.endsWith(".txt");	
        File[] files = inputDir.listFiles(filter);
        
        System.out.println("STARTING ANALYSIS... Total file to process: " + files.length);
        
        int count = 1;
        for (File textFile : files) {
            
            try {
                String fileName = FilenameUtils.getBaseName(textFile.getName());
                
                System.out.println("\tAnalyzing file " + count + " of " + files.length + "... [" + fileName + "]");
                
                String text = FileUtils.readFileToString(textFile, StandardCharsets.UTF_8);
                
                InputData data = new InputData();
                data.setText(text);
                
                DeepAnalysisInput container = new DeepAnalysisInput();
                container.setMetadata(meta);
                container.setData(data);
                
                IAnalysisOutput result = client.analyze(container);
                
                writeOutput(result, outputDir, fileName, meta);
                
                count++;
                
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error found while processing document '" + textFile.getName() + "'!");
                WelcomeClientException wex;
                if (ex instanceof WelcomeClientException) {
                	wex = (WelcomeClientException) ex;
                } else {
                	wex = new WelcomeClientException("Unexpected error! (" + ex.getClass().getName() + ": " + ex.getMessage() + ")", ex);
                }

                if (config.isSkipErrors()) {
                    logger.log(Level.SEVERE, ex.getMessage());
                } else {
                    throw wex;
                }
            }
        }
        
        System.out.println("ANALYSIS COMPLETED");
    }
    
    public static void writeOutput(IAnalysisOutput analysisOutput, File outputDir, String baseName, InputMetadata meta) throws IOException {
    	
    	if (analysisOutput instanceof XmiResult) {
            XmiResult xmiResult = (XmiResult) analysisOutput;
            
            File xmiFile = new File(outputDir, baseName + ".xmi");
            File typeSystemFile = new File(outputDir, "TypeSystem.xml");
            
            FileUtils.writeStringToFile(xmiFile, xmiResult.getXmi(), StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(typeSystemFile, xmiResult.getTypesystem(), StandardCharsets.UTF_8);
            
		} else {
            File jsonFile = new File(outputDir, baseName + ".json");
            
			ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            om.writeValue(jsonFile, analysisOutput);
		}
	}
}
