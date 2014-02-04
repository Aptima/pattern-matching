/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package influent.idl;  
@SuppressWarnings("all")
/** A PropertyDescriptor is used to describe a possible property that can be present in an entity or link. It describes 
	 a single property that can be used in a property search. It can optionally include example or suggested values 
	 for searching on.

	 CHANGED IN 1.4 */
@org.apache.avro.specific.AvroGenerated
public class FL_PropertyMatchDescriptor extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"FL_PropertyMatchDescriptor\",\"namespace\":\"influent.idl\",\"doc\":\"A PropertyDescriptor is used to describe a possible property that can be present in an entity or link. It describes \\r\\n\\t a single property that can be used in a property search. It can optionally include example or suggested values \\r\\n\\t for searching on.\\r\\n\\r\\n\\t CHANGED IN 1.4\",\"fields\":[{\"name\":\"key\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"key or tag of the Properties that could be searched on\"},{\"name\":\"value\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"double\",\"long\",\"boolean\",{\"type\":\"record\",\"name\":\"FL_GeoData\",\"doc\":\"Structured representation of geo-spatial data.\",\"fields\":[{\"name\":\"text\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"doc\":\"an address or other place reference; unstructured text field\",\"default\":null},{\"name\":\"lat\",\"type\":[\"double\",\"null\"],\"doc\":\"latitude\",\"default\":null},{\"name\":\"lon\",\"type\":[\"double\",\"null\"],\"doc\":\"longitude\",\"default\":null},{\"name\":\"cc\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"doc\":\"ISO 3 digit country code\",\"default\":null}]},{\"type\":\"record\",\"name\":\"FL_Series\",\"doc\":\"This is a placeholder for timeseries and other series that are available as property values, which the UI will use to make\\r\\n\\t charts. This may come back from aggregating links.\",\"fields\":[]},\"null\"],\"doc\":\"value of the Property to search on\",\"default\":null},{\"name\":\"constraint\",\"type\":{\"type\":\"enum\",\"name\":\"FL_Constraint\",\"doc\":\"Property value matching constraints\\r\\n\\r\\n\\t CHANGED IN 1.4\",\"symbols\":[\"EQUALS\",\"NOT\",\"FUZZY\",\"LESS_THAN\",\"GREATER_THAN\",\"LESS_THAN_EQUALS\",\"GREATER_THAN_EQUALS\"]},\"doc\":\"EQUALS, NOT, FUZZY, LESS_THAN, GREATER_THAN, LESS_THAN_EQUALS, GREATER_THAN_EQUALS\"},{\"name\":\"relative\",\"type\":\"boolean\",\"doc\":\"If true, the values on this entity or link are relative to the values of this property\\r\\n\\t\\t    on other entities/links in this pattern or network descriptor, and are not to be interpreted\\r\\n\\t\\t    as absolute values.\\r\\n\\r\\n\\t\\t    ADDED IN 1.4\",\"default\":false},{\"name\":\"weight\",\"type\":\"double\",\"doc\":\"Indicates a relative weight of this to other match criteria\",\"default\":1}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** key or tag of the Properties that could be searched on */
   private java.lang.String key;
  /** value of the Property to search on */
   private java.lang.Object value;
  /** EQUALS, NOT, FUZZY, LESS_THAN, GREATER_THAN, LESS_THAN_EQUALS, GREATER_THAN_EQUALS */
   private influent.idl.FL_Constraint constraint;
  /** If true, the values on this entity or link are relative to the values of this property
		    on other entities/links in this pattern or network descriptor, and are not to be interpreted
		    as absolute values.

		    ADDED IN 1.4 */
   private boolean relative;
  /** Indicates a relative weight of this to other match criteria */
   private double weight;

  /**
   * Default constructor.
   */
  public FL_PropertyMatchDescriptor() {}

  /**
   * All-args constructor.
   */
  public FL_PropertyMatchDescriptor(java.lang.String key, java.lang.Object value, influent.idl.FL_Constraint constraint, java.lang.Boolean relative, java.lang.Double weight) {
    this.key = key;
    this.value = value;
    this.constraint = constraint;
    this.relative = relative;
    this.weight = weight;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return key;
    case 1: return value;
    case 2: return constraint;
    case 3: return relative;
    case 4: return weight;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: key = (java.lang.String)value$; break;
    case 1: value = (java.lang.Object)value$; break;
    case 2: constraint = (influent.idl.FL_Constraint)value$; break;
    case 3: relative = (java.lang.Boolean)value$; break;
    case 4: weight = (java.lang.Double)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'key' field.
   * key or tag of the Properties that could be searched on   */
  public java.lang.String getKey() {
    return key;
  }

  /**
   * Sets the value of the 'key' field.
   * key or tag of the Properties that could be searched on   * @param value the value to set.
   */
  public void setKey(java.lang.String value) {
    this.key = value;
  }

  /**
   * Gets the value of the 'value' field.
   * value of the Property to search on   */
  public java.lang.Object getValue() {
    return value;
  }

  /**
   * Sets the value of the 'value' field.
   * value of the Property to search on   * @param value the value to set.
   */
  public void setValue(java.lang.Object value) {
    this.value = value;
  }

  /**
   * Gets the value of the 'constraint' field.
   * EQUALS, NOT, FUZZY, LESS_THAN, GREATER_THAN, LESS_THAN_EQUALS, GREATER_THAN_EQUALS   */
  public influent.idl.FL_Constraint getConstraint() {
    return constraint;
  }

  /**
   * Sets the value of the 'constraint' field.
   * EQUALS, NOT, FUZZY, LESS_THAN, GREATER_THAN, LESS_THAN_EQUALS, GREATER_THAN_EQUALS   * @param value the value to set.
   */
  public void setConstraint(influent.idl.FL_Constraint value) {
    this.constraint = value;
  }

  /**
   * Gets the value of the 'relative' field.
   * If true, the values on this entity or link are relative to the values of this property
		    on other entities/links in this pattern or network descriptor, and are not to be interpreted
		    as absolute values.

		    ADDED IN 1.4   */
  public java.lang.Boolean getRelative() {
    return relative;
  }

  /**
   * Sets the value of the 'relative' field.
   * If true, the values on this entity or link are relative to the values of this property
		    on other entities/links in this pattern or network descriptor, and are not to be interpreted
		    as absolute values.

		    ADDED IN 1.4   * @param value the value to set.
   */
  public void setRelative(java.lang.Boolean value) {
    this.relative = value;
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

  /** Creates a new FL_PropertyMatchDescriptor RecordBuilder */
  public static influent.idl.FL_PropertyMatchDescriptor.Builder newBuilder() {
    return new influent.idl.FL_PropertyMatchDescriptor.Builder();
  }
  
  /** Creates a new FL_PropertyMatchDescriptor RecordBuilder by copying an existing Builder */
  public static influent.idl.FL_PropertyMatchDescriptor.Builder newBuilder(influent.idl.FL_PropertyMatchDescriptor.Builder other) {
    return new influent.idl.FL_PropertyMatchDescriptor.Builder(other);
  }
  
  /** Creates a new FL_PropertyMatchDescriptor RecordBuilder by copying an existing FL_PropertyMatchDescriptor instance */
  public static influent.idl.FL_PropertyMatchDescriptor.Builder newBuilder(influent.idl.FL_PropertyMatchDescriptor other) {
    return new influent.idl.FL_PropertyMatchDescriptor.Builder(other);
  }
  
  /**
   * RecordBuilder for FL_PropertyMatchDescriptor instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<FL_PropertyMatchDescriptor>
    implements org.apache.avro.data.RecordBuilder<FL_PropertyMatchDescriptor> {

    private java.lang.String key;
    private java.lang.Object value;
    private influent.idl.FL_Constraint constraint;
    private boolean relative;
    private double weight;

    /** Creates a new Builder */
    private Builder() {
      super(influent.idl.FL_PropertyMatchDescriptor.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(influent.idl.FL_PropertyMatchDescriptor.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing FL_PropertyMatchDescriptor instance */
    private Builder(influent.idl.FL_PropertyMatchDescriptor other) {
            super(influent.idl.FL_PropertyMatchDescriptor.SCHEMA$);
      if (isValidValue(fields()[0], other.key)) {
        this.key = data().deepCopy(fields()[0].schema(), other.key);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.value)) {
        this.value = data().deepCopy(fields()[1].schema(), other.value);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.constraint)) {
        this.constraint = data().deepCopy(fields()[2].schema(), other.constraint);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.relative)) {
        this.relative = data().deepCopy(fields()[3].schema(), other.relative);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.weight)) {
        this.weight = data().deepCopy(fields()[4].schema(), other.weight);
        fieldSetFlags()[4] = true;
      }
    }

    /** Gets the value of the 'key' field */
    public java.lang.String getKey() {
      return key;
    }
    
    /** Sets the value of the 'key' field */
    public influent.idl.FL_PropertyMatchDescriptor.Builder setKey(java.lang.String value) {
      validate(fields()[0], value);
      this.key = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'key' field has been set */
    public boolean hasKey() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'key' field */
    public influent.idl.FL_PropertyMatchDescriptor.Builder clearKey() {
      key = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'value' field */
    public java.lang.Object getValue() {
      return value;
    }
    
    /** Sets the value of the 'value' field */
    public influent.idl.FL_PropertyMatchDescriptor.Builder setValue(java.lang.Object value) {
      validate(fields()[1], value);
      this.value = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'value' field has been set */
    public boolean hasValue() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'value' field */
    public influent.idl.FL_PropertyMatchDescriptor.Builder clearValue() {
      value = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'constraint' field */
    public influent.idl.FL_Constraint getConstraint() {
      return constraint;
    }
    
    /** Sets the value of the 'constraint' field */
    public influent.idl.FL_PropertyMatchDescriptor.Builder setConstraint(influent.idl.FL_Constraint value) {
      validate(fields()[2], value);
      this.constraint = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'constraint' field has been set */
    public boolean hasConstraint() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'constraint' field */
    public influent.idl.FL_PropertyMatchDescriptor.Builder clearConstraint() {
      constraint = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'relative' field */
    public java.lang.Boolean getRelative() {
      return relative;
    }
    
    /** Sets the value of the 'relative' field */
    public influent.idl.FL_PropertyMatchDescriptor.Builder setRelative(boolean value) {
      validate(fields()[3], value);
      this.relative = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'relative' field has been set */
    public boolean hasRelative() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'relative' field */
    public influent.idl.FL_PropertyMatchDescriptor.Builder clearRelative() {
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'weight' field */
    public java.lang.Double getWeight() {
      return weight;
    }
    
    /** Sets the value of the 'weight' field */
    public influent.idl.FL_PropertyMatchDescriptor.Builder setWeight(double value) {
      validate(fields()[4], value);
      this.weight = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'weight' field has been set */
    public boolean hasWeight() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'weight' field */
    public influent.idl.FL_PropertyMatchDescriptor.Builder clearWeight() {
      fieldSetFlags()[4] = false;
      return this;
    }

    public FL_PropertyMatchDescriptor build() {
      try {
        FL_PropertyMatchDescriptor record = new FL_PropertyMatchDescriptor();
        record.key = fieldSetFlags()[0] ? this.key : (java.lang.String) defaultValue(fields()[0]);
        record.value = fieldSetFlags()[1] ? this.value : (java.lang.Object) defaultValue(fields()[1]);
        record.constraint = fieldSetFlags()[2] ? this.constraint : (influent.idl.FL_Constraint) defaultValue(fields()[2]);
        record.relative = fieldSetFlags()[3] ? this.relative : (java.lang.Boolean) defaultValue(fields()[3]);
        record.weight = fieldSetFlags()[4] ? this.weight : (java.lang.Double) defaultValue(fields()[4]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
