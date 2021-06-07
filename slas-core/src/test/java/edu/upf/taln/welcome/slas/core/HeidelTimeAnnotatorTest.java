package edu.upf.taln.welcome.slas.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;

import org.xml.sax.SAXException;
import org.dkpro.core.io.xmi.XmiReader;
import org.dkpro.core.io.xmi.XmiWriter;

import de.unihd.dbs.uima.annotator.heideltime.HeidelTime;

/**
 *
 * @author rcarlini
 */
public class HeidelTimeAnnotatorTest {
    
	public static CollectionReader createXMIReader(String xmiPath, String typesystemPath) throws UIMAException, IOException, SAXException {
	    
	    CollectionReader reader = CollectionReaderFactory.createReader(
                XmiReader.class,
                XmiReader.PARAM_SOURCE_LOCATION, xmiPath,
                XmiReader.PARAM_TYPE_SYSTEM_FILE, typesystemPath,
                XmiReader.PARAM_MERGE_TYPE_SYSTEM, true);
	    
        return reader;
	}

    /**
     * Test annotator given UDPipe-generated annotations.
     */
    @Test
    public void testUDPipeEn() throws Exception {
        
		String xmiPath = "src/test/resources/heideltime/udpipe/paleolithic_input.xmi";
		String typesystemPath = "src/test/resources/heideltime/udpipe/TypeSystem.xml";

        CollectionReader reader = createXMIReader(xmiPath, typesystemPath);
        
        AnalysisEngine heideltime = AnalysisEngineFactory.createEngine(
				HeidelTime.class,
				HeidelTime.PARAM_LANGUAGE, "english",
				HeidelTime.PARAM_TYPE_TO_PROCESS, "news",
				HeidelTime.PARAM_DATE, true,
				HeidelTime.PARAM_TIME, true,
				HeidelTime.PARAM_DURATION, true,
				HeidelTime.PARAM_SET, true,
				HeidelTime.PARAM_TEMPONYMS, false,
				HeidelTime.PARAM_GROUP, true,
				HeidelTime.PARAM_USE_COARSE_VALUE, false,
				HeidelTime.PARAM_DEBUG, false);
		
		/*AnalysisEngine intervalTagger = AnalysisEngineFactory.createEngine(
				IntervalTagger.class,
				"language", "english",
				"annotate_intervals", true,
				"annotate_interval_candidates", true);*/
		
		AnalysisEngine writer = AnalysisEngineFactory.createEngine(
				XmiWriter.class,
				XmiWriter.PARAM_TARGET_LOCATION, "src/test/resources/heideltime/udpipe/paleolithic_actual",
				XmiWriter.PARAM_OVERWRITE, true);

		SimplePipeline.runPipeline(reader, heideltime/*, intervalTagger*/, writer);
    }
    
}
