

/* First created by JCasGen Fri Sep 20 11:08:54 CEST 2019 */
package edu.upf.taln.welcome.taxonomy_annotator;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Sep 20 11:08:54 CEST 2019
 * XML source: /home/ivan/git/TENSOR/tensor-types/src/main/resources/desc/types/TaxonomyAnnotationDescriptor.xml
 * @generated */
public class TaxonomyEntry extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TaxonomyEntry.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TaxonomyEntry() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public TaxonomyEntry(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public TaxonomyEntry(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public TaxonomyEntry(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: entryId

  /** getter for entryId - gets 
   * @generated
   * @return value of the feature 
   */
  public String getEntryId() {
    if (TaxonomyEntry_Type.featOkTst && ((TaxonomyEntry_Type)jcasType).casFeat_entryId == null)
      jcasType.jcas.throwFeatMissing("entryId", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TaxonomyEntry_Type)jcasType).casFeatCode_entryId);}
    
  /** setter for entryId - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEntryId(String v) {
    if (TaxonomyEntry_Type.featOkTst && ((TaxonomyEntry_Type)jcasType).casFeat_entryId == null)
      jcasType.jcas.throwFeatMissing("entryId", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    jcasType.ll_cas.ll_setStringValue(addr, ((TaxonomyEntry_Type)jcasType).casFeatCode_entryId, v);}    
   
    
  //*--------------*
  //* Feature: parentId

  /** getter for parentId - gets 
   * @generated
   * @return value of the feature 
   */
  public String getParentId() {
    if (TaxonomyEntry_Type.featOkTst && ((TaxonomyEntry_Type)jcasType).casFeat_parentId == null)
      jcasType.jcas.throwFeatMissing("parentId", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TaxonomyEntry_Type)jcasType).casFeatCode_parentId);}
    
  /** setter for parentId - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setParentId(String v) {
    if (TaxonomyEntry_Type.featOkTst && ((TaxonomyEntry_Type)jcasType).casFeat_parentId == null)
      jcasType.jcas.throwFeatMissing("parentId", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    jcasType.ll_cas.ll_setStringValue(addr, ((TaxonomyEntry_Type)jcasType).casFeatCode_parentId, v);}    
   
    
  //*--------------*
  //* Feature: label

  /** getter for label - gets 
   * @generated
   * @return value of the feature 
   */
  public String getLabel() {
    if (TaxonomyEntry_Type.featOkTst && ((TaxonomyEntry_Type)jcasType).casFeat_label == null)
      jcasType.jcas.throwFeatMissing("label", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TaxonomyEntry_Type)jcasType).casFeatCode_label);}
    
  /** setter for label - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLabel(String v) {
    if (TaxonomyEntry_Type.featOkTst && ((TaxonomyEntry_Type)jcasType).casFeat_label == null)
      jcasType.jcas.throwFeatMissing("label", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    jcasType.ll_cas.ll_setStringValue(addr, ((TaxonomyEntry_Type)jcasType).casFeatCode_label, v);}    
   
    
  //*--------------*
  //* Feature: parentLabel

  /** getter for parentLabel - gets 
   * @generated
   * @return value of the feature 
   */
  public String getParentLabel() {
    if (TaxonomyEntry_Type.featOkTst && ((TaxonomyEntry_Type)jcasType).casFeat_parentLabel == null)
      jcasType.jcas.throwFeatMissing("parentLabel", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TaxonomyEntry_Type)jcasType).casFeatCode_parentLabel);}
    
  /** setter for parentLabel - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setParentLabel(String v) {
    if (TaxonomyEntry_Type.featOkTst && ((TaxonomyEntry_Type)jcasType).casFeat_parentLabel == null)
      jcasType.jcas.throwFeatMissing("parentLabel", "edu.upf.taln.welcome.taxonomy_annotator.TaxonomyEntry");
    jcasType.ll_cas.ll_setStringValue(addr, ((TaxonomyEntry_Type)jcasType).casFeatCode_parentLabel, v);}    
  }

    