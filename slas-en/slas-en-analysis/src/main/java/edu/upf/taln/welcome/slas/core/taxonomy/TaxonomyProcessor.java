package edu.upf.taln.welcome.slas.core.taxonomy;

import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;

/**
 *
 * @author rcarlini
 */
public class TaxonomyProcessor {
    
    private static String getValue(RDFNode node) {
        return node.asLiteral().getValue().toString();
    }
    
    public static Model readModel(InputStream inStream, String rdfLang) {
        Model model = ModelFactory.createDefaultModel();
        model.read(inStream, "", rdfLang);
        
        return model;
    }
    public static Concepts processStream(InputStream inStream) throws IOException {
        return processStream(inStream, null);
    }    
    
    public static Concepts processStream(InputStream inStream, String rdfLang) throws IOException {

        Concepts concepts = new Concepts();
        Model model = readModel(inStream, rdfLang);
        
        ResIterator conceptIterator = model.listSubjectsWithProperty(RDF.type, SKOS.Concept);
        while (conceptIterator.hasNext()) {
            Resource concept = conceptIterator.next();

            Concepts.Token token = new Concepts.Token();
            token.setEntryId(concept.toString());

            Statement stmt;
            stmt = model.getProperty(concept, RDFS.comment);
            if (stmt != null) {
                String comment = getValue(stmt.getObject());
                token.setComment(comment);
            }

            stmt = model.getProperty(concept, SKOS.prefLabel); // rdflib.term.Literal('Soft Target', lang='eng')
            if (stmt != null) {
                String prefLabel = getValue(stmt.getObject());
                token.setLabel(prefLabel);
 
                Concepts.Variant defaultVariant = new Concepts.Variant();
                defaultVariant.setBase(prefLabel);
                token.addVariant(defaultVariant);
           }

            stmt = model.getProperty(concept, SKOS.broader);    // rdflib.term.URIRef('http://www.tensor-project.eu/taxomony##7b9cf007-806e-3854-8d12-ab800c8a982b')
            if (stmt != null) {
                Resource parent = stmt.getResource();
                token.setParentId(parent.toString());
                
                stmt = model.getProperty(parent, SKOS.prefLabel);
                if (stmt != null) {
                    String parentPrefLabel = getValue(stmt.getObject());
                    token.setParentLabel(parentPrefLabel);
                }
            }

            
            StmtIterator labelIterator = model.listStatements(concept, SKOS.altLabel, (String) null);
            while (labelIterator.hasNext()) {
                Statement label = labelIterator.next();
                String base = getValue(label.getObject());

                Concepts.Variant variant = new Concepts.Variant();
                variant.setBase(base);
                token.addVariant(variant);
            }
            concepts.addToken(token);
        }
        return concepts;
    }
}
