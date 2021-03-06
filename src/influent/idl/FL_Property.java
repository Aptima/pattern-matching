/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package influent.idl;  
@SuppressWarnings("all")
/** Each property on an Entity or Link is a name-value pair, with data type information, as well as optional
	 provenance. Tags provide a way for the data provider to associate semantic annotations to each property
	 in terms of the semantics of the application. */
@org.apache.avro.specific.AvroGenerated
public class FL_Property extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"FL_Property\",\"namespace\":\"influent.idl\",\"doc\":\"Each property on an Entity or Link is a name-value pair, with data type information, as well as optional\\r\\n\\t provenance. Tags provide a way for the data provider to associate semantic annotations to each property\\r\\n\\t in terms of the semantics of the application.\",\"fields\":[{\"name\":\"key\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"the field name in the underlying data source\"},{\"name\":\"friendlyText\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"doc\":\"user-friendly short-text for key (displayable)\",\"default\":null},{\"name\":\"value\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"int\",\"float\",\"double\",\"long\",\"boolean\",{\"type\":\"record\",\"name\":\"FL_GeoData\",\"doc\":\"Structured representation of geo-spatial data.\",\"fields\":[{\"name\":\"text\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"doc\":\"an address or other place reference; unstructured text field\",\"default\":null},{\"name\":\"lat\",\"type\":[\"double\",\"null\"],\"doc\":\"latitude\",\"default\":null},{\"name\":\"lon\",\"type\":[\"double\",\"null\"],\"doc\":\"longitude\",\"default\":null},{\"name\":\"cc\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"doc\":\"ISO 3 digit country code\",\"default\":null}]},{\"type\":\"record\",\"name\":\"FL_Series\",\"doc\":\"This is a placeholder for timeseries and other series that are available as property values, which the UI will use to make\\r\\n\\t charts. This may come back from aggregating links.\",\"fields\":[]},\"null\"],\"default\":null},{\"name\":\"type\",\"type\":{\"type\":\"enum\",\"name\":\"FL_PropertyType\",\"doc\":\"Allowed types for Property values.\",\"symbols\":[\"DOUBLE\",\"LONG\",\"BOOLEAN\",\"STRING\",\"DATE\",\"GEO\",\"SERIES\",\"OTHER\"]},\"doc\":\"One of DOUBLE, LONG, BOOLEAN, STRING, DATE, GEO, SERIES, OTHER\"},{\"name\":\"provenance\",\"type\":[{\"type\":\"record\",\"name\":\"FL_Provenance\",\"doc\":\"This is a placeholder for future modeling of provenance. It is not a required field in any service calls.\",\"fields\":[{\"name\":\"uri\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Placeholder for now. Express provenance as a single URI.\"}]},\"null\"],\"default\":null},{\"name\":\"uncertainty\",\"type\":[{\"type\":\"record\",\"name\":\"FL_Uncertainty\",\"doc\":\"This is a placeholder for future modeling of uncertainty. It is not a required field in any service calls.\",\"fields\":[{\"name\":\"confidence\",\"type\":\"double\",\"doc\":\"Placeholder for now. Express uncertainty as a single number from 0 to 1.\",\"default\":1}]},\"null\"],\"default\":null},{\"name\":\"tags\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"enum\",\"name\":\"FL_PropertyTag\",\"doc\":\"Tags are defined by the application layer as a taxonomy of user and application concepts,\\r\\n\\t independent of the data sources. This allows application semantics to be re-used with new\\r\\n\\t data, with a minimum of new software design and development. Data layer entity types, link\\r\\n\\t types and properties should be mapped into the list of tags. The application layer must be\\r\\n\\t able to search by native field name or by tag interchangeably, and properties returned must\\r\\n\\t contain both native field names as well as tags.\\r\\n\\t \\r\\n\\t The list of tags may change as application features evolve, though that will require\\r\\n\\t collaboration with the data layer providers. Evolving the tag list should not change the\\r\\n\\t Data Access or Search APIs.\\r\\n\\r\\n\\t This is the current list of tags for Properties:\",\"symbols\":[\"ID\",\"TYPE\",\"LABEL\",\"STAT\",\"TEXT\",\"LINKED_DATA\",\"IMAGE\",\"GEO\",\"DATE\",\"AMOUNT\",\"CREDIT\",\"DEBIT\",\"COUNT\",\"SERIES\",\"CONSTRUCTED\",\"RAW\"]}},\"doc\":\"one or more tags from the Tag list, used to map this source-specific field into the semantics of applications\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** the field name in the underlying data source */
   private java.lang.String key;
  /** user-friendly short-text for key (displayable) */
   private java.lang.String friendlyText;
   private java.lang.Object value;
  /** One of DOUBLE, LONG, BOOLEAN, STRING, DATE, GEO, SERIES, OTHER */
   private influent.idl.FL_PropertyType type;
   private influent.idl.FL_Provenance provenance;
   private influent.idl.FL_Uncertainty uncertainty;
  /** one or more tags from the Tag list, used to map this source-specific field into the semantics of applications */
   private java.util.List<influent.idl.FL_PropertyTag> tags;

  /**
   * Default constructor.
   */
  public FL_Property() {}

  /**
   * All-args constructor.
   */
  public FL_Property(java.lang.String key, java.lang.String friendlyText, java.lang.Object value, influent.idl.FL_PropertyType type, influent.idl.FL_Provenance provenance, influent.idl.FL_Uncertainty uncertainty, java.util.List<influent.idl.FL_PropertyTag> tags) {
    this.key = key;
    this.friendlyText = friendlyText;
    this.value = value;
    this.type = type;
    this.provenance = provenance;
    this.uncertainty = uncertainty;
    this.tags = tags;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return key;
    case 1: return friendlyText;
    case 2: return value;
    case 3: return type;
    case 4: return provenance;
    case 5: return uncertainty;
    case 6: return tags;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: key = (java.lang.String)value$; break;
    case 1: friendlyText = (java.lang.String)value$; break;
    case 2: value = (java.lang.Object)value$; break;
    case 3: type = (influent.idl.FL_PropertyType)value$; break;
    case 4: provenance = (influent.idl.FL_Provenance)value$; break;
    case 5: uncertainty = (influent.idl.FL_Uncertainty)value$; break;
    case 6: tags = (java.util.List<influent.idl.FL_PropertyTag>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'key' field.
   * the field name in the underlying data source   */
  public java.lang.String getKey() {
    return key;
  }

  /**
   * Sets the value of the 'key' field.
   * the field name in the underlying data source   * @param value the value to set.
   */
  public void setKey(java.lang.String value) {
    this.key = value;
  }

  /**
   * Gets the value of the 'friendlyText' field.
   * user-friendly short-text for key (displayable)   */
  public java.lang.String getFriendlyText() {
    return friendlyText;
  }

  /**
   * Sets the value of the 'friendlyText' field.
   * user-friendly short-text for key (displayable)   * @param value the value to set.
   */
  public void setFriendlyText(java.lang.String value) {
    this.friendlyText = value;
  }

  /**
   * Gets the value of the 'value' field.
   */
  public java.lang.Object getValue() {
    return value;
  }

  /**
   * Sets the value of the 'value' field.
   * @param value the value to set.
   */
  public void setValue(java.lang.Object value) {
    this.value = value;
  }

  /**
   * Gets the value of the 'type' field.
   * One of DOUBLE, LONG, BOOLEAN, STRING, DATE, GEO, SERIES, OTHER   */
  public influent.idl.FL_PropertyType getType() {
    return type;
  }

  /**
   * Sets the value of the 'type' field.
   * One of DOUBLE, LONG, BOOLEAN, STRING, DATE, GEO, SERIES, OTHER   * @param value the value to set.
   */
  public void setType(influent.idl.FL_PropertyType value) {
    this.type = value;
  }

  /**
   * Gets the value of the 'provenance' field.
   */
  public influent.idl.FL_Provenance getProvenance() {
    return provenance;
  }

  /**
   * Sets the value of the 'provenance' field.
   * @param value the value to set.
   */
  public void setProvenance(influent.idl.FL_Provenance value) {
    this.provenance = value;
  }

  /**
   * Gets the value of the 'uncertainty' field.
   */
  public influent.idl.FL_Uncertainty getUncertainty() {
    return uncertainty;
  }

  /**
   * Sets the value of the 'uncertainty' field.
   * @param value the value to set.
   */
  public void setUncertainty(influent.idl.FL_Uncertainty value) {
    this.uncertainty = value;
  }

  /**
   * Gets the value of the 'tags' field.
   * one or more tags from the Tag list, used to map this source-specific field into the semantics of applications   */
  public java.util.List<influent.idl.FL_PropertyTag> getTags() {
    return tags;
  }

  /**
   * Sets the value of the 'tags' field.
   * one or more tags from the Tag list, used to map this source-specific field into the semantics of applications   * @param value the value to set.
   */
  public void setTags(java.util.List<influent.idl.FL_PropertyTag> value) {
    this.tags = value;
  }

  /** Creates a new FL_Property RecordBuilder */
  public static influent.idl.FL_Property.Builder newBuilder() {
    return new influent.idl.FL_Property.Builder();
  }
  
  /** Creates a new FL_Property RecordBuilder by copying an existing Builder */
  public static influent.idl.FL_Property.Builder newBuilder(influent.idl.FL_Property.Builder other) {
    return new influent.idl.FL_Property.Builder(other);
  }
  
  /** Creates a new FL_Property RecordBuilder by copying an existing FL_Property instance */
  public static influent.idl.FL_Property.Builder newBuilder(influent.idl.FL_Property other) {
    return new influent.idl.FL_Property.Builder(other);
  }
  
  /**
   * RecordBuilder for FL_Property instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<FL_Property>
    implements org.apache.avro.data.RecordBuilder<FL_Property> {

    private java.lang.String key;
    private java.lang.String friendlyText;
    private java.lang.Object value;
    private influent.idl.FL_PropertyType type;
    private influent.idl.FL_Provenance provenance;
    private influent.idl.FL_Uncertainty uncertainty;
    private java.util.List<influent.idl.FL_PropertyTag> tags;

    /** Creates a new Builder */
    private Builder() {
      super(influent.idl.FL_Property.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(influent.idl.FL_Property.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing FL_Property instance */
    private Builder(influent.idl.FL_Property other) {
            super(influent.idl.FL_Property.SCHEMA$);
      if (isValidValue(fields()[0], other.key)) {
        this.key = data().deepCopy(fields()[0].schema(), other.key);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.friendlyText)) {
        this.friendlyText = data().deepCopy(fields()[1].schema(), other.friendlyText);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.value)) {
        this.value = data().deepCopy(fields()[2].schema(), other.value);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.type)) {
        this.type = data().deepCopy(fields()[3].schema(), other.type);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.provenance)) {
        this.provenance = data().deepCopy(fields()[4].schema(), other.provenance);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.uncertainty)) {
        this.uncertainty = data().deepCopy(fields()[5].schema(), other.uncertainty);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.tags)) {
        this.tags = data().deepCopy(fields()[6].schema(), other.tags);
        fieldSetFlags()[6] = true;
      }
    }

    /** Gets the value of the 'key' field */
    public java.lang.String getKey() {
      return key;
    }
    
    /** Sets the value of the 'key' field */
    public influent.idl.FL_Property.Builder setKey(java.lang.String value) {
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
    public influent.idl.FL_Property.Builder clearKey() {
      key = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'friendlyText' field */
    public java.lang.String getFriendlyText() {
      return friendlyText;
    }
    
    /** Sets the value of the 'friendlyText' field */
    public influent.idl.FL_Property.Builder setFriendlyText(java.lang.String value) {
      validate(fields()[1], value);
      this.friendlyText = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'friendlyText' field has been set */
    public boolean hasFriendlyText() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'friendlyText' field */
    public influent.idl.FL_Property.Builder clearFriendlyText() {
      friendlyText = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'value' field */
    public java.lang.Object getValue() {
      return value;
    }
    
    /** Sets the value of the 'value' field */
    public influent.idl.FL_Property.Builder setValue(java.lang.Object value) {
      validate(fields()[2], value);
      this.value = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'value' field has been set */
    public boolean hasValue() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'value' field */
    public influent.idl.FL_Property.Builder clearValue() {
      value = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'type' field */
    public influent.idl.FL_PropertyType getType() {
      return type;
    }
    
    /** Sets the value of the 'type' field */
    public influent.idl.FL_Property.Builder setType(influent.idl.FL_PropertyType value) {
      validate(fields()[3], value);
      this.type = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'type' field has been set */
    public boolean hasType() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'type' field */
    public influent.idl.FL_Property.Builder clearType() {
      type = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'provenance' field */
    public influent.idl.FL_Provenance getProvenance() {
      return provenance;
    }
    
    /** Sets the value of the 'provenance' field */
    public influent.idl.FL_Property.Builder setProvenance(influent.idl.FL_Provenance value) {
      validate(fields()[4], value);
      this.provenance = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'provenance' field has been set */
    public boolean hasProvenance() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'provenance' field */
    public influent.idl.FL_Property.Builder clearProvenance() {
      provenance = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /** Gets the value of the 'uncertainty' field */
    public influent.idl.FL_Uncertainty getUncertainty() {
      return uncertainty;
    }
    
    /** Sets the value of the 'uncertainty' field */
    public influent.idl.FL_Property.Builder setUncertainty(influent.idl.FL_Uncertainty value) {
      validate(fields()[5], value);
      this.uncertainty = value;
      fieldSetFlags()[5] = true;
      return this; 
    }
    
    /** Checks whether the 'uncertainty' field has been set */
    public boolean hasUncertainty() {
      return fieldSetFlags()[5];
    }
    
    /** Clears the value of the 'uncertainty' field */
    public influent.idl.FL_Property.Builder clearUncertainty() {
      uncertainty = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /** Gets the value of the 'tags' field */
    public java.util.List<influent.idl.FL_PropertyTag> getTags() {
      return tags;
    }
    
    /** Sets the value of the 'tags' field */
    public influent.idl.FL_Property.Builder setTags(java.util.List<influent.idl.FL_PropertyTag> value) {
      validate(fields()[6], value);
      this.tags = value;
      fieldSetFlags()[6] = true;
      return this; 
    }
    
    /** Checks whether the 'tags' field has been set */
    public boolean hasTags() {
      return fieldSetFlags()[6];
    }
    
    /** Clears the value of the 'tags' field */
    public influent.idl.FL_Property.Builder clearTags() {
      tags = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    public FL_Property build() {
      try {
        FL_Property record = new FL_Property();
        record.key = fieldSetFlags()[0] ? this.key : (java.lang.String) defaultValue(fields()[0]);
        record.friendlyText = fieldSetFlags()[1] ? this.friendlyText : (java.lang.String) defaultValue(fields()[1]);
        record.value = fieldSetFlags()[2] ? this.value : (java.lang.Object) defaultValue(fields()[2]);
        record.type = fieldSetFlags()[3] ? this.type : (influent.idl.FL_PropertyType) defaultValue(fields()[3]);
        record.provenance = fieldSetFlags()[4] ? this.provenance : (influent.idl.FL_Provenance) defaultValue(fields()[4]);
        record.uncertainty = fieldSetFlags()[5] ? this.uncertainty : (influent.idl.FL_Uncertainty) defaultValue(fields()[5]);
        record.tags = fieldSetFlags()[6] ? this.tags : (java.util.List<influent.idl.FL_PropertyTag>) defaultValue(fields()[6]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
