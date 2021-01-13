package edu.upf.taln.welcome.slas.client;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.factories.InputFactory;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.AnalysisType;
import edu.upf.taln.welcome.slas.commons.factories.OutputFactory.OutputLevel;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;


public class WelcomeBackendClient<T extends IAnalysisOutput> {

	private final Class<T> outputType;
	
    private static final Logger logger = Logger.getLogger(WelcomeBackendClient.class.getSimpleName());

    private final WebTarget target;

    private final String language;

    public WelcomeBackendClient(String url, String language, Class<T> outputType) {

        this.language = language;
        Client client = ClientBuilder.newClient();
        target = client.target(url);
        this.outputType = outputType;
    }

    public T analyze(AnalysisType analysisType, String text) throws WelcomeException {
    	
        return analyze(analysisType, OutputLevel.demo, text);
    }
    
    public T analyze(AnalysisType analysisType, OutputLevel outputLevel, String text) throws WelcomeException {

    	DeepAnalysisInput request = InputFactory.create(analysisType, outputLevel, text, language);
        return analyze(request);
    }

    public T analyze(DeepAnalysisInput request) throws WelcomeException {

        Response response = sendRequest(request);
        T result = response.readEntity(new GenericType<T>(outputType) {});

        return result;
    }

    protected Response sendRequest(DeepAnalysisInput request) throws WelcomeException {

        Response response = target.path("analyze")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response;
        } else {
            this.error_management(response);
            return null;
        }
    }
    
    private void error_management(Response response) throws WelcomeException {
        String responseStr = response.readEntity(String.class);
        ObjectMapper om = new ObjectMapper()
        		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        if (response.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            logger.severe("Status: " + response.getStatus());

            WelcomeException we;
            try {
                we = om.readValue(responseStr, WelcomeException.class);
                logger.severe("Server error! Message: " + we.getMessage());
                
            } catch (Exception e) {
                logger.severe("Unable to load MindspacesException. Trying Exception...");
                try {
                    Exception newEx = om.readValue(responseStr, Exception.class);
                    we = new WelcomeException(newEx);

                } catch (IOException e1) {
                    logger.severe("Unable to load Exception.");
                    logger.severe("Response content:\n" + responseStr);
                    throw new WelcomeException("Server error! Status: " + response.getStatus() + " Unable to load Exception.", e1);
                }
            }
            throw we;
        } else {
            logger.severe("Status: " + response.getStatus());
            logger.severe("Response content:\n" + responseStr);
            throw new WelcomeException("Server error! Status: " + response.getStatus());
        }
    }

}
