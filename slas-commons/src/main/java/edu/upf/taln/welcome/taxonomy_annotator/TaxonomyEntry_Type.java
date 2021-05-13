
/* First created by JCasGen Fri Sep 20 11:08:54 CEST 2019 */
package edu.upf.taln.welcome.taxonomy_annotator;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Fri Sep 20 11:08:54 CEST 2019
 * @generated */
public class TaxonomyEntry_Type extends Annotation_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = TaxonomyEntry.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
 
  /** @generated */
  final Feature casFeat_entryId;
  /** @generated */
  final int     casFeatCode_entryId;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getEntryId(int addr) {
        if (featOkTst && casFeat_entryId == null)
      jcas.throwFeatMissing("entryId", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    return ll_cas.ll_getStringValue(addr, casFeatCode_entryId);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEntryId(int addr, String v) {
        if (featOkTst && casFeat_entryId == null)
      jcas.throwFeatMissing("entryId", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    ll_cas.ll_setStringValue(addr, casFeatCode_entryId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_parentId;
  /** @generated */
  final int     casFeatCode_parentId;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getParentId(int addr) {
        if (featOkTst && casFeat_parentId == null)
      jcas.throwFeatMissing("parentId", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    return ll_cas.ll_getStringValue(addr, casFeatCode_parentId);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setParentId(int addr, String v) {
        if (featOkTst && casFeat_parentId == null)
      jcas.throwFeatMissing("parentId", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    ll_cas.ll_setStringValue(addr, casFeatCode_parentId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_label;
  /** @generated */
  final int     casFeatCode_label;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getLabel(int addr) {
        if (featOkTst && casFeat_label == null)
      jcas.throwFeatMissing("label", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    return ll_cas.ll_getStringValue(addr, casFeatCode_label);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLabel(int addr, String v) {
        if (featOkTst && casFeat_label == null)
      jcas.throwFeatMissing("label", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    ll_cas.ll_setStringValue(addr, casFeatCode_label, v);}
    
  
 
  /** @generated */
  final Feature casFeat_parentLabel;
  /** @generated */
  final int     casFeatCode_parentLabel;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getParentLabel(int addr) {
        if (featOkTst && casFeat_parentLabel == null)
      jcas.throwFeatMissing("parentLabel", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    return ll_cas.ll_getStringValue(addr, casFeatCode_parentLabel);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setParentLabel(int addr, String v) {
        if (featOkTst && casFeat_parentLabel == null)
      jcas.throwFeatMissing("parentLabel", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    ll_cas.ll_setStringValue(addr, casFeatCode_parentLabel, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public TaxonomyEntry_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_entryId = jcas.getRequiredFeatureDE(casType, "entryId", "uima.cas.String", featOkTst);
    casFeatCode_entryId  = (null == casFeat_entryId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_entryId).getCode();

 
    casFeat_parentId = jcas.getRequiredFeatureDE(casType, "parentId", "uima.cas.String", featOkTst);
    casFeatCode_parentId  = (null == casFeat_parentId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_parentId).getCode();

 
    casFeat_label = jcas.getRequiredFeatureDE(casType, "label", "uima.cas.String", featOkTst);
    casFeatCode_label  = (null == casFeat_label) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_label).getCode();

 
    casFeat_parentLabel = jcas.getRequiredFeatureDE(casType, "parentLabel", "uima.cas.String", featOkTst);
    casFeatCode_parentLabel  = (null == casFeat_parentLabel) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_parentLabel).getCode();

  }
}



    