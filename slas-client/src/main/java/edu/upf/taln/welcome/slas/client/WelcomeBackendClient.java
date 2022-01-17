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

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeClientException;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.factories.InputFactory;
import edu.upf.taln.welcome.slas.commons.input.OutputType;
import edu.upf.taln.welcome.slas.commons.input.AnalysisType;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;
import edu.upf.taln.welcome.slas.commons.output.AnalysisOutputImpl;
import edu.upf.taln.welcome.slas.commons.output.DeepAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.output.XmiResult;


public class WelcomeBackendClient<T extends IAnalysisOutput> {
	
    private static final Logger logger = Logger.getLogger(WelcomeBackendClient.class.getSimpleName());

    private final WebTarget target;

    public WelcomeBackendClient(String url) {

        Client client = ClientBuilder.newClient();
        target = client.target(url);
    }

    public T analyze(AnalysisType analysisType, String text, String language, String useCase) throws WelcomeClientException {
    	
        return analyze(analysisType, text, language, useCase, OutputType.demo);
    }
    
    public T analyze(AnalysisType analysisType, String text, String language, String useCase, OutputType outputLevel) throws WelcomeClientException {

    	DeepAnalysisInput request = InputFactory.create(analysisType, outputLevel, text, language, useCase);
        return analyze(request);
    }

    public T analyze(DeepAnalysisInput request) throws WelcomeClientException {

        InputMetadata meta = request.getMetadata();

        Class<? extends IAnalysisOutput> outputClass;
        switch (meta.getOutputType()) {
            case xmi:
                outputClass = XmiResult.class;
                break;
                
            case welcome:
                outputClass = DeepAnalysisOutput.class;
                break;

            case demo:
            default:
                outputClass = AnalysisOutputImpl.class;
                break;
        }

        Response response = sendRequest(request);
        T result = response.readEntity(new GenericType<T>(outputClass) {});

        return result;
    }

    protected Response sendRequest(DeepAnalysisInput request) throws WelcomeClientException {

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
    
    private void error_management(Response response) throws WelcomeClientException {
        String responseStr = response.readEntity(String.class);
        ObjectMapper om = new ObjectMapper()
        		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        if (response.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {

            WelcomeClientException wce;
            try {
            	WelcomeException we = om.readValue(responseStr, WelcomeException.class);
                wce = new WelcomeClientException(we.getMessage(), we, response.getStatus());
            } catch (Exception e) {
                try {
                    Exception newEx = om.readValue(responseStr, Exception.class);
                    wce = new WelcomeClientException(newEx.getMessage(), newEx, response.getStatus());

                } catch (IOException e1) {
                    throw new WelcomeClientException(response.getStatus());
                }
            }
            throw wce;
        } else {
            throw new WelcomeClientException(response.getStatus());
        }
    }

}
