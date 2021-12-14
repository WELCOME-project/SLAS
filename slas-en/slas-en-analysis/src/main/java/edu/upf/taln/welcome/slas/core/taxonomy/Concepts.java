package edu.upf.taln.welcome.slas.core.taxonomy;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.ArrayList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 *
 * @author rcarlini
 */
@JacksonXmlRootElement(localName = "concepts")
public class Concepts {
    
    public static class Variant {
        
        @JacksonXmlProperty(isAttribute = true)
        private String base;

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }
    }
    
    public static class Token {
        
        @JacksonXmlProperty(isAttribute = true)
        private String label;
        
        @JacksonXmlProperty(isAttribute = true)
        private String entryId;

        @JacksonXmlProperty(isAttribute = true)
        private String parentId;

        @JacksonXmlProperty(isAttribute = true)
        private String parentLabel;

        @JacksonXmlProperty(isAttribute = true)
        private String comment;

        @JacksonXmlProperty(localName = "variant")
        @JacksonXmlElementWrapper(useWrapping=false)
        private ArrayList<Variant> variants = new ArrayList<>();

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getEntryId() {
            return entryId;
        }

        public void setEntryId(String entryId) {
            this.entryId = entryId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getParentLabel() {
            return parentLabel;
        }

        public void setParentLabel(String parentLabel) {
            this.parentLabel = parentLabel;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public ArrayList<Variant> getVariants() {
            return variants;
        }

        public void setVariants(ArrayList<Variant> variants) {
            this.variants = variants;
        }
        
        public void addVariant(Variant variant) {
            this.variants.add(variant);
        }
    }
    
    @JacksonXmlProperty(localName = "token")
    @JacksonXmlElementWrapper(useWrapping=false)
    private ArrayList<Token> tokens = new ArrayList<>();

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }
    
    public void addToken(Token token) {
        this.tokens.add(token);
    }
}
