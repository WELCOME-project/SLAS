package edu.upf.taln.welcome.slas.commons.output;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.wsd.type.WSDResult;
import de.unihd.dbs.uima.types.heideltime.Timex3;
import edu.upf.taln.flask_wrapper.type.GeolocationCandidate;
import edu.upf.taln.flask_wrapper.type.WSDSpan;
import edu.upf.taln.parser.deep_parser.types.PredArgsToken;
import java.util.List;
import java.util.Set;
import org.apache.uima.fit.util.JCasUtil;

/**
 *
 * @author rcarlini
 */
public class EntityTypeInfo {

    public static enum EntityType {Speaker, Addressee, Temporal, Location, NE, Predicate, Concept, Unknown}    
    
    private static String DEFAULT_TYPE = EntityType.Unknown.name();
    String type = DEFAULT_TYPE;
    String morph;
    String pos;
    boolean isNamedEntity;
    String namedEntity;
    boolean isHeidelTime;
    boolean isGeolocation;
    boolean isWSDNamedEntity;
    boolean isPredicate;
    boolean isConcept;

    public EntityTypeInfo() {
    }

    public EntityTypeInfo(PredArgsToken token, Set<PredArgsToken> predicateSet) {
        extractInfo(token, predicateSet);
        assignType();
    }

    protected final void extractInfo(PredArgsToken token, Set<PredArgsToken> predicateSet) {
        this.pos = token.getPos().getPosValue().toUpperCase();
        List<Token> tokensList = JCasUtil.selectCovering(Token.class, token);
        Token surfaceToken = null;
        if (tokensList.size() > 0) {
            surfaceToken = tokensList.get(0);
        }
        if (surfaceToken != null) {
            this.morph = surfaceToken.getMorph().getValue();
        }
        List<NamedEntity> neList = JCasUtil.selectCovered(NamedEntity.class, token);
        this.isNamedEntity = !neList.isEmpty();
        if (this.isNamedEntity) {
            this.namedEntity = neList.get(0).getValue();
        }
        List<Timex3> heideltimeList = JCasUtil.selectCovered(Timex3.class, token);
        this.isHeidelTime = !heideltimeList.isEmpty();
        List<GeolocationCandidate> geolocationsList = JCasUtil.selectCovered(GeolocationCandidate.class, token);
        this.isGeolocation = !geolocationsList.isEmpty();
        List<WSDResult> wsdList = JCasUtil.selectCovered(WSDResult.class, token);
        this.isWSDNamedEntity = !wsdList.isEmpty();
        this.isPredicate = predicateSet.contains(token);
        List<WSDSpan> conceptsList = JCasUtil.selectCovered(WSDSpan.class, token);
        this.isConcept = !conceptsList.isEmpty();
    }

    protected final void assignType() {
        this.type = DEFAULT_TYPE;
        boolean isPronoun = this.pos.equals("PRP") || this.pos.equals("PRP$");
        if (this.morph != null && this.morph.contains("Person=1") && isPronoun) {
            this.type = EntityType.Speaker.name();
        } else if (this.morph != null && this.morph.contains("Person=2") && isPronoun) {
            this.type = EntityType.Addressee.name();
        } else if (this.isNamedEntity) {
            this.type = this.namedEntity;
        } else if (this.isHeidelTime) {
            this.type = EntityType.Temporal.name();
        } else if (this.isGeolocation) {
            this.type = EntityType.Location.name();
        } else if (this.isWSDNamedEntity) {
            this.type = EntityType.NE.name();
        } else if (this.isPredicate) {
            this.type = EntityType.Predicate.name();
        } else if (this.isConcept) {
            this.type = EntityType.Concept.name();
        }
    }

    public String getType() {
        return type;
    }
    
}
