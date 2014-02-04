/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package influent.idl;  
@SuppressWarnings("all")
/** Description of entity match criteria within a pattern.

	 CHANGED IN 1.4 */
@org.apache.avro.specific.AvroGenerated
public class FL_EntityMatchDescriptor extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"FL_EntityMatchDescriptor\",\"namespace\":\"influent.idl\",\"doc\":\"Description of entity match criteria within a pattern.\\r\\n\\r\\n\\t CHANGED IN 1.4\",\"fields\":[{\"name\":\"uid\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"The UID of the PATTERN node (NOT the underlying matched entity ID).\\r\\n\\t\\t Will be referenced by FL_LinkMatchDescriptors as source or target, and in results.\\r\\n\\t\\t Cannot be null.\"},{\"name\":\"role\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"doc\":\"Optional role name, for labeling the pattern for human understanding\",\"default\":null},{\"name\":\"sameAs\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"doc\":\"- uid of another FL_EntityMatchDescriptor\\r\\n\\t\\t - if provided, this entity must match the same underlying entity that other node\\r\\n\\t\\t - used in the sequence diagram instead of allowing cycles\",\"default\":null},{\"name\":\"entities\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},\"null\"],\"doc\":\"entities should match AT LEAST ONE OF the given entity IDs, if provided\",\"default\":null},{\"name\":\"tags\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"enum\",\"name\":\"FL_EntityTag\",\"doc\":\"This is the current list of tags for Entities:\",\"symbols\":[\"ACCOUNT\",\"GROUP\",\"CLUSTER\",\"FILE\",\"ANONYMOUS\",\"OTHER\"]}},\"null\"],\"doc\":\"entities should match AT LEAST ONE OF the given tags (e.g ACCOUNT), if provided\",\"default\":null},{\"name\":\"properties\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"FL_PropertyMatchDescriptor\",\"doc\":\"A PropertyDescriptor is used to describe a possible property that can be present in an entity or link. It describes \\r\\n\\t a single property that can be used in a property search. It can optionally include example or suggested values \\r\\n\\t for searching on.\\r\\n\\r\\n\\t CHANGED IN 1.4\",\"fields\":[{\"name\":\"key\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"key or tag of the Properties that could be searched on\"},{\"name\":\"value\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"double\",\"long\",\"boolean\",{\"type\":\"record\",\"name\":\"FL_GeoData\",\"doc\":\"Structured representation of geo-spatial data.\",\"fields\":[{\"name\":\"text\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"doc\":\"an address or other place reference; unstructured text field\",\"default\":null},{\"name\":\"lat\",\"type\":[\"double\",\"null\"],\"doc\":\"latitude\",\"default\":null},{\"name\":\"lon\",\"type\":[\"double\",\"null\"],\"doc\":\"longitude\",\"default\":null},{\"name\":\"cc\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"doc\":\"ISO 3 digit country code\",\"default\":null}]},{\"type\":\"record\",\"name\":\"FL_Series\",\"doc\":\"This is a placeholder for timeseries and other series that are available as property values, which the UI will use to make\\r\\n\\t charts. This may come back from aggregating links.\",\"fields\":[]},\"null\"],\"doc\":\"value of the Property to search on\",\"default\":null},{\"name\":\"constraint\",\"type\":{\"type\":\"enum\",\"name\":\"FL_Constraint\",\"doc\":\"Property value matching constraints\\r\\n\\r\\n\\t CHANGED IN 1.4\",\"symbols\":[\"EQUALS\",\"NOT\",\"FUZZY\",\"LESS_THAN\",\"GREATER_THAN\",\"LESS_THAN_EQUALS\",\"GREATER_THAN_EQUALS\"]},\"doc\":\"EQUALS, NOT, FUZZY, LESS_THAN, GREATER_THAN, LESS_THAN_EQUALS, GREATER_THAN_EQUALS\"},{\"name\":\"relative\",\"type\":\"boolean\",\"doc\":\"If true, the values on this entity or link are relative to the values of this property\\r\\n\\t\\t    on other entities/links in this pattern or network descriptor, and are not to be interpreted\\r\\n\\t\\t    as absolute values.\\r\\n\\r\\n\\t\\t    ADDED IN 1.4\",\"default\":false},{\"name\":\"weight\",\"type\":\"double\",\"doc\":\"Indicates a relative weight of this to other match criteria\",\"default\":1}]}},\"null\"],\"doc\":\"entities should match ALL of the provided property descriptors (e.g. LABEL, GEO, etc)\\r\\n\\t\\t    e.g. logical \\\"AND\\\". Partial matches may be returned, if scoring is provided.\",\"default\":null},{\"name\":\"examplars\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},\"null\"],\"doc\":\"used for QBE -- not used to match like the entities list, this list of entities\\r\\n\\t\\t    should be used by the system to infer the above constraints when the user does not\\r\\n\\t\\t    provide them.\\r\\n\\r\\n \\t\\t\\tADDED IN 1.4\",\"default\":null},{\"name\":\"weight\",\"type\":\"double\",\"doc\":\"Indicates a relative weight of this to other match criteria\",\"default\":1}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** The UID of the PATTERN node (NOT the underlying matched entity ID).
		 Will be referenced by FL_LinkMatchDescriptors as source or target, and in results.
		 Cannot be null. */
   private java.lang.String uid;
  /** Optional role name, for labeling the pattern for human understanding */
   private java.lang.String role;
  /** - uid of another FL_EntityMatchDescriptor
		 - if provided, this entity must match the same underlying entity that other node
		 - used in the sequence diagram instead of allowing cycles */
   private java.lang.String sameAs;
  /** entities should match AT LEAST ONE OF the given entity IDs, if provided */
   private java.util.List<java.lang.String> entities;
  /** entities should match AT LEAST ONE OF the given tags (e.g ACCOUNT), if provided */
   private java.util.List<influent.idl.FL_EntityTag> tags;
  /** entities should match ALL of the provided property descriptors (e.g. LABEL, GEO, etc)
		    e.g. logical "AND". Partial matches may be returned, if scoring is provided. */
   private java.util.List<influent.idl.FL_PropertyMatchDescriptor> properties;
  /** used for QBE -- not used to match like the entities list, this list of entities
		    should be used by the system to infer the above constraints when the user does not
		    provide them.

 			ADDED IN 1.4 */
   private java.util.List<java.lang.String> examplars;
  /** Indicates a relative weight of this to other match criteria */
   private double weight;

  /**
   * Default constructor.
   */
  public FL_EntityMatchDescriptor() {}

  /**
   * All-args constructor.
   */
  public FL_EntityMatchDescriptor(java.lang.String uid, java.lang.String role, java.lang.String sameAs, java.util.List<java.lang.String> entities, java.util.List<influent.idl.FL_EntityTag> tags, java.util.List<influent.idl.FL_PropertyMatchDescriptor> properties, java.util.List<java.lang.String> examplars, java.lang.Double weight) {
    this.uid = uid;
    this.role = role;
    this.sameAs = sameAs;
    this.entities = entities;
    this.tags = tags;
    this.properties = properties;
    this.examplars = examplars;
    this.weight = weight;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return uid;
    case 1: return role;
    case 2: return sameAs;
    case 3: return entities;
    case 4: return tags;
    case 5: return properties;
    case 6: return examplars;
    case 7: return weight;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: uid = (java.lang.String)value$; break;
    case 1: role = (java.lang.String)value$; break;
    case 2: sameAs = (java.lang.String)value$; break;
    case 3: entities = (java.util.List<java.lang.String>)value$; break;
    case 4: tags = (java.util.List<influent.idl.FL_EntityTag>)value$; break;
    case 5: properties = (java.util.List<influent.idl.FL_PropertyMatchDescriptor>)value$; break;
    case 6: examplars = (java.util.List<java.lang.String>)value$; break;
    case 7: weight = (java.lang.Double)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'uid' field.
   * The UID of the PATTERN node (NOT the underlying matched entity ID).
		 Will be referenced by FL_LinkMatchDescriptors as source or target, and in results.
		 Cannot be null.   */
  public java.lang.String getUid() {
    return uid;
  }

  /**
   * Sets the value of the 'uid' field.
   * The UID of the PATTERN node (NOT the underlying matched entity ID).
		 Will be referenced by FL_LinkMatchDescriptors as source or target, and in results.
		 Cannot be null.   * @param value the value to set.
   */
  public void setUid(java.lang.String value) {
    this.uid = value;
  }

  /**
   * Gets the value of the 'role' field.
   * Optional role name, for labeling the pattern for human understanding   */
  public java.lang.String getRole() {
    return role;
  }

  /**
   * Sets the value of the 'role' field.
   * Optional role name, for labeling the pattern for human understanding   * @param value the value to set.
   */
  public void setRole(java.lang.String value) {
    this.role = value;
  }

  /**
   * Gets the value of the 'sameAs' field.
   * - uid of another FL_EntityMatchDescriptor
		 - if provided, this entity must match the same underlying entity that other node
		 - used in the sequence diagram instead of allowing cycles   */
  public java.lang.String getSameAs() {
    return sameAs;
  }

  /**
   * Sets the value of the 'sameAs' field.
   * - uid of another FL_EntityMatchDescriptor
		 - if provided, this entity must match the same underlying entity that other node
		 - used in the sequence diagram instead of allowing cycles   * @param value the value to set.
   */
  public void setSameAs(java.lang.String value) {
    this.sameAs = value;
  }

  /**
   * Gets the value of the 'entities' field.
   * entities should match AT LEAST ONE OF the given entity IDs, if provided   */
  public java.util.List<java.lang.String> getEntities() {
    return entities;
  }

  /**
   * Sets the value of the 'entities' field.
   * entities should match AT LEAST ONE OF the given entity IDs, if provided   * @param value the value to set.
   */
  public void setEntities(java.util.List<java.lang.String> value) {
    this.entities = value;
  }

  /**
   * Gets the value of the 'tags' field.
   * entities should match AT LEAST ONE OF the given tags (e.g ACCOUNT), if provided   */
  public java.util.List<influent.idl.FL_EntityTag> getTags() {
    return tags;
  }

  /**
   * Sets the value of the 'tags' field.
   * entities should match AT LEAST ONE OF the given tags (e.g ACCOUNT), if provided   * @param value the value to set.
   */
  public void setTags(java.util.List<influent.idl.FL_EntityTag> value) {
    this.tags = value;
  }

  /**
   * Gets the value of the 'properties' field.
   * entities should match ALL of the provided property descriptors (e.g. LABEL, GEO, etc)
		    e.g. logical "AND". Partial matches may be returned, if scoring is provided.   */
  public java.util.List<influent.idl.FL_PropertyMatchDescriptor> getProperties() {
    return properties;
  }

  /**
   * Sets the value of the 'properties' field.
   * entities should match ALL of the provided property descriptors (e.g. LABEL, GEO, etc)
		    e.g. logical "AND". Partial matches may be returned, if scoring is provided.   * @param value the value to set.
   */
  public void setProperties(java.util.List<influent.idl.FL_PropertyMatchDescriptor> value) {
    this.properties = value;
  }

  /**
   * Gets the value of the 'examplars' field.
   * used for QBE -- not used to match like the entities list, this list of entities
		    should be used by the system to infer the above constraints when the user does not
		    provide them.

 			ADDED IN 1.4   */
  public java.util.List<java.lang.String> getExamplars() {
    return examplars;
  }

  /**
   * Sets the value of the 'examplars' field.
   * used for QBE -- not used to match like the entities list, this list of entities
		    should be used by the system to infer the above constraints when the user does not
		    provide them.

 			ADDED IN 1.4   * @param value the value to set.
   */
  public void setExamplars(java.util.List<java.lang.String> value) {
    this.examplars = value;
  }

  /**
   * Gets the value of the 'weight' field.
   * Indicates a relative weight of this to other match criteria   */
  public java.lang.Double getWeight() {
    return weight;
  }

  /**
   * Sets the value of the 'weight' field.
   * Indicates a relative weight of this to other match criteria   * @param value the value to set.
   */
  public void setWeight(java.lang.Double value) {
    this.weight = value;
  }

  /** Creates a new FL_EntityMatchDescriptor RecordBuilder */
  public static influent.idl.FL_EntityMatchDescriptor.Builder newBuilder() {
    return new influent.idl.FL_EntityMatchDescriptor.Builder();
  }
  
  /** Creates a new FL_EntityMatchDescriptor RecordBuilder by copying an existing Builder */
  public static influent.idl.FL_EntityMatchDescriptor.Builder newBuilder(influent.idl.FL_EntityMatchDescriptor.Builder other) {
    return new influent.idl.FL_EntityMatchDescriptor.Builder(other);
  }
  
  /** Creates a new FL_EntityMatchDescriptor RecordBuilder by copying an existing FL_EntityMatchDescriptor instance */
  public static influent.idl.FL_EntityMatchDescriptor.Builder newBuilder(influent.idl.FL_EntityMatchDescriptor other) {
    return new influent.idl.FL_EntityMatchDescriptor.Builder(other);
  }
  
  /**
   * RecordBuilder for FL_EntityMatchDescriptor instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<FL_EntityMatchDescriptor>
    implements org.apache.avro.data.RecordBuilder<FL_EntityMatchDescriptor> {

    private java.lang.String uid;
    private java.lang.String role;
    private java.lang.String sameAs;
    private java.util.List<java.lang.String> entities;
    private java.util.List<influent.idl.FL_EntityTag> tags;
    private java.util.List<influent.idl.FL_PropertyMatchDescriptor> properties;
    private java.util.List<java.lang.String> examplars;
    private double weight;

    /** Creates a new Builder */
    private Builder() {
      super(influent.idl.FL_EntityMatchDescriptor.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(influent.idl.FL_EntityMatchDescriptor.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing FL_EntityMatchDescriptor instance */
    private Builder(influent.idl.FL_EntityMatchDescriptor other) {
            super(influent.idl.FL_EntityMatchDescriptor.SCHEMA$);
      if (isValidValue(fields()[0], other.uid)) {
        this.uid = data().deepCopy(fields()[0].schema(), other.uid);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.role)) {
        this.role = data().deepCopy(fields()[1].schema(), other.role);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.sameAs)) {
        this.sameAs = data().deepCopy(fields()[2].schema(), other.sameAs);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.entities)) {
        this.entities = data().deepCopy(fields()[3].schema(), other.entities);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.tags)) {
        this.tags = data().deepCopy(fields()[4].schema(), other.tags);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.properties)) {
        this.properties = data().deepCopy(fields()[5].schema(), other.properties);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.examplars)) {
        this.examplars = data().deepCopy(fields()[6].schema(), other.examplars);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.weight)) {
        this.weight = data().deepCopy(fields()[7].schema(), other.weight);
        fieldSetFlags()[7] = true;
      }
    }

    /** Gets the value of the 'uid' field */
    public java.lang.String getUid() {
      return uid;
    }
    
    /** Sets the value of the 'uid' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder setUid(java.lang.String value) {
      validate(fields()[0], value);
      this.uid = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'uid' field has been set */
    public boolean hasUid() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'uid' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder clearUid() {
      uid = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'role' field */
    public java.lang.String getRole() {
      return role;
    }
    
    /** Sets the value of the 'role' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder setRole(java.lang.String value) {
      validate(fields()[1], value);
      this.role = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'role' field has been set */
    public boolean hasRole() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'role' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder clearRole() {
      role = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'sameAs' field */
    public java.lang.String getSameAs() {
      return sameAs;
    }
    
    /** Sets the value of the 'sameAs' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder setSameAs(java.lang.String value) {
      validate(fields()[2], value);
      this.sameAs = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'sameAs' field has been set */
    public boolean hasSameAs() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'sameAs' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder clearSameAs() {
      sameAs = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'entities' field */
    public java.util.List<java.lang.String> getEntities() {
      return entities;
    }
    
    /** Sets the value of the 'entities' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder setEntities(java.util.List<java.lang.String> value) {
      validate(fields()[3], value);
      this.entities = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'entities' field has been set */
    public boolean hasEntities() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'entities' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder clearEntities() {
      entities = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'tags' field */
    public java.util.List<influent.idl.FL_EntityTag> getTags() {
      return tags;
    }
    
    /** Sets the value of the 'tags' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder setTags(java.util.List<influent.idl.FL_EntityTag> value) {
      validate(fields()[4], value);
      this.tags = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'tags' field has been set */
    public boolean hasTags() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'tags' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder clearTags() {
      tags = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /** Gets the value of the 'properties' field */
    public java.util.List<influent.idl.FL_PropertyMatchDescriptor> getProperties() {
      return properties;
    }
    
    /** Sets the value of the 'properties' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder setProperties(java.util.List<influent.idl.FL_PropertyMatchDescriptor> value) {
      validate(fields()[5], value);
      this.properties = value;
      fieldSetFlags()[5] = true;
      return this; 
    }
    
    /** Checks whether the 'properties' field has been set */
    public boolean hasProperties() {
      return fieldSetFlags()[5];
    }
    
    /** Clears the value of the 'properties' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder clearProperties() {
      properties = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /** Gets the value of the 'examplars' field */
    public java.util.List<java.lang.String> getExamplars() {
      return examplars;
    }
    
    /** Sets the value of the 'examplars' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder setExamplars(java.util.List<java.lang.String> value) {
      validate(fields()[6], value);
      this.examplars = value;
      fieldSetFlags()[6] = true;
      return this; 
    }
    
    /** Checks whether the 'examplars' field has been set */
    public boolean hasExamplars() {
      return fieldSetFlags()[6];
    }
    
    /** Clears the value of the 'examplars' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder clearExamplars() {
      examplars = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /** Gets the value of the 'weight' field */
    public java.lang.Double getWeight() {
      return weight;
    }
    
    /** Sets the value of the 'weight' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder setWeight(double value) {
      validate(fields()[7], value);
      this.weight = value;
      fieldSetFlags()[7] = true;
      return this; 
    }
    
    /** Checks whether the 'weight' field has been set */
    public boolean hasWeight() {
      return fieldSetFlags()[7];
    }
    
    /** Clears the value of the 'weight' field */
    public influent.idl.FL_EntityMatchDescriptor.Builder clearWeight() {
      fieldSetFlags()[7] = false;
      return this;
    }

    public FL_EntityMatchDescriptor build() {
      try {
        FL_EntityMatchDescriptor record = new FL_EntityMatchDescriptor();
        record.uid = fieldSetFlags()[0] ? this.uid : (java.lang.String) defaultValue(fields()[0]);
        record.role = fieldSetFlags()[1] ? this.role : (java.lang.String) defaultValue(fields()[1]);
        record.sameAs = fieldSetFlags()[2] ? this.sameAs : (java.lang.String) defaultValue(fields()[2]);
        record.entities = fieldSetFlags()[3] ? this.entities : (java.util.List<java.lang.String>) defaultValue(fields()[3]);
        record.tags = fieldSetFlags()[4] ? this.tags : (java.util.List<influent.idl.FL_EntityTag>) defaultValue(fields()[4]);
        record.properties = fieldSetFlags()[5] ? this.properties : (java.util.List<influent.idl.FL_PropertyMatchDescriptor>) defaultValue(fields()[5]);
        record.examplars = fieldSetFlags()[6] ? this.examplars : (java.util.List<java.lang.String>) defaultValue(fields()[6]);
        record.weight = fieldSetFlags()[7] ? this.weight : (java.lang.Double) defaultValue(fields()[7]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
