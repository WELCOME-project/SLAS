package edu.upf.taln.welcome.slas.client;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.factories.InputFactory;
import edu.upf.taln.welcome.slas.commons.factories.OutputFactory.OutputLevel;
import edu.upf.taln.welcome.slas.commons.input.AnalysisType;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInputPlain;
import edu.upf.taln.welcome.slas.commons.input.InputDataPlain;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;
import edu.upf.taln.welcome.slas.commons.output.AnalysisOutputImpl;
import edu.upf.taln.welcome.slas.commons.output.AnalysisOutputMetadata;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.output.XmiResult;


/**
 *
 * @author rcarlini
 */
class Processor {

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

	public void execute() throws WelcomeException {
        
        File inputDir = new File(config.getInputPath());
        if (!inputDir.exists()) {
            throw new WelcomeException("Input directory not found!");
        }
        
        File outputDir = new File(config.getOutputPath());
        if (!outputDir.exists()) {
            throw new WelcomeException("Output directory not found!");
        }
        
		execute(meta, inputDir, outputDir);
	}    

    public void execute(InputMetadata meta, File inputDir, File outputDir) throws WelcomeException {
        
        String serviceUrl = config.getServiceURL();
        String language = meta.getLanguage();
        
        WelcomeBackendClient client = new WelcomeBackendClient<>(serviceUrl, language, AnalysisOutputImpl.class);
        
        FilenameFilter filter = (File dir, String name) -> name.endsWith(".txt");	
        File[] files = inputDir.listFiles(filter);
        
        System.out.println("STARTING ANALYSIS... Total file to process: " + files.length);
        
        int count = 1;
        for (File textFile : files) {
            
            try {
                String fileName = FilenameUtils.getBaseName(textFile.getName());
                
                System.out.println("\tAnalyzing file " + count + " of " + files.length + "... [" + fileName + "]");
                
                String text = FileUtils.readFileToString(textFile, StandardCharsets.UTF_8);
                
                InputDataPlain data = new InputDataPlain();
                data.setText(text);
                
                DeepAnalysisInputPlain container = new DeepAnalysisInputPlain();
                container.setMetadata(meta);
                container.setData(data);
                
                IAnalysisOutput result = client.analyze(container);
                
                writeOutput(result, outputDir, fileName, meta);
                
                count++;
                
            } catch (IOException ex) {
                if (config.isSkipErrors()) {
                    Logger.getLogger(Processor.class.getName()).log(Level.WARNING, null, ex);
                    
                } else {
                    throw new WelcomeException("Something happened while processing document '" + textFile.getName() + "'!", ex);
                }
            }
        }
        
        System.out.println("ANALYSIS COMPLETED");
    }
    
    public static void writeOutput(IAnalysisOutput analysisOutput, File outputDir, String baseName, InputMetadata meta) throws IOException {
    	
    	AnalysisOutputImpl out = (AnalysisOutputImpl) analysisOutput;
		LinkedHashMap<String, String> xmiResult = (LinkedHashMap<String, String>) out.getResult();

		if (xmiResult.containsKey("xmi") && xmiResult.containsKey("typesystem")) {
            File xmiFile = new File(outputDir, baseName + ".xmi");
            File typeSystemFile = new File(outputDir, "TypeSystem.xml");
            
            FileUtils.writeStringToFile(xmiFile, (String) xmiResult.get("xmi"), StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(typeSystemFile, (String) xmiResult.get("typesystem"), StandardCharsets.UTF_8);
            
		} else {
            File jsonFile = new File(outputDir, baseName + ".json");
            
			ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            om.writeValue(jsonFile, analysisOutput);
		}
	}
}
