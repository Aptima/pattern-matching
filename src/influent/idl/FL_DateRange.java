/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package influent.idl;  
@SuppressWarnings("all")
/** Describes a date range at a specific resolution.

	 CHANGED IN 1.4 */
@org.apache.avro.specific.AvroGenerated
public class FL_DateRange extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"FL_DateRange\",\"namespace\":\"influent.idl\",\"doc\":\"Describes a date range at a specific resolution.\\r\\n\\r\\n\\t CHANGED IN 1.4\",\"fields\":[{\"name\":\"startDate\",\"type\":\"long\"},{\"name\":\"interval\",\"type\":{\"type\":\"enum\",\"name\":\"FL_DateInterval\",\"doc\":\"Temporal resolution for aggregation\",\"symbols\":[\"DAILY\",\"WEEKLY\",\"MONTHLY\",\"QUARTERLY\",\"YEARLY\"]},\"doc\":\"time aggregation level to use, e.g. use monthly data\"},{\"name\":\"numBins\",\"type\":\"long\",\"doc\":\"number of bins to return, e.g. 12 monthly bins for 1 year of data\"},{\"name\":\"numIntervalsPerBin\",\"type\":\"long\",\"doc\":\"number of intervals in a bin, e.g. 2 months/bin in 12 bins for 2 years of data\",\"default\":1}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
   private long startDate;
  /** time aggregation level to use, e.g. use monthly data */
   private influent.idl.FL_DateInterval interval;
  /** number of bins to return, e.g. 12 monthly bins for 1 year of data */
   private long numBins;
  /** number of intervals in a bin, e.g. 2 months/bin in 12 bins for 2 years of data */
   private long numIntervalsPerBin;

  /**
   * Default constructor.
   */
  public FL_DateRange() {}

  /**
   * All-args constructor.
   */
  public FL_DateRange(java.lang.Long startDate, influent.idl.FL_DateInterval interval, java.lang.Long numBins, java.lang.Long numIntervalsPerBin) {
    this.startDate = startDate;
    this.interval = interval;
    this.numBins = numBins;
    this.numIntervalsPerBin = numIntervalsPerBin;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return startDate;
    case 1: return interval;
    case 2: return numBins;
    case 3: return numIntervalsPerBin;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: startDate = (java.lang.Long)value$; break;
    case 1: interval = (influent.idl.FL_DateInterval)value$; break;
    case 2: numBins = (java.lang.Long)value$; break;
    case 3: numIntervalsPerBin = (java.lang.Long)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'startDate' field.
   */
  public java.lang.Long getStartDate() {
    return startDate;
  }

  /**
   * Sets the value of the 'startDate' field.
   * @param value the value to set.
   */
  public void setStartDate(java.lang.Long value) {
    this.startDate = value;
  }

  /**
   * Gets the value of the 'interval' field.
   * time aggregation level to use, e.g. use monthly data   */
  public influent.idl.FL_DateInterval getInterval() {
    return interval;
  }

  /**
   * Sets the value of the 'interval' field.
   * time aggregation level to use, e.g. use monthly data   * @param value the value to set.
   */
  public void setInterval(influent.idl.FL_DateInterval value) {
    this.interval = value;
  }

  /**
   * Gets the value of the 'numBins' field.
   * number of bins to return, e.g. 12 monthly bins for 1 year of data   */
  public java.lang.Long getNumBins() {
    return numBins;
  }

  /**
   * Sets the value of the 'numBins' field.
   * number of bins to return, e.g. 12 monthly bins for 1 year of data   * @param value the value to set.
   */
  public void setNumBins(java.lang.Long value) {
    this.numBins = value;
  }

  /**
   * Gets the value of the 'numIntervalsPerBin' field.
   * number of intervals in a bin, e.g. 2 months/bin in 12 bins for 2 years of data   */
  public java.lang.Long getNumIntervalsPerBin() {
    return numIntervalsPerBin;
  }

  /**
   * Sets the value of the 'numIntervalsPerBin' field.
   * number of intervals in a bin, e.g. 2 months/bin in 12 bins for 2 years of data   * @param value the value to set.
   */
  public void setNumIntervalsPerBin(java.lang.Long value) {
    this.numIntervalsPerBin = value;
  }

  /** Creates a new FL_DateRange RecordBuilder */
  public static influent.idl.FL_DateRange.Builder newBuilder() {
    return new influent.idl.FL_DateRange.Builder();
  }
  
  /** Creates a new FL_DateRange RecordBuilder by copying an existing Builder */
  public static influent.idl.FL_DateRange.Builder newBuilder(influent.idl.FL_DateRange.Builder other) {
    return new influent.idl.FL_DateRange.Builder(other);
  }
  
  /** Creates a new FL_DateRange RecordBuilder by copying an existing FL_DateRange instance */
  public static influent.idl.FL_DateRange.Builder newBuilder(influent.idl.FL_DateRange other) {
    return new influent.idl.FL_DateRange.Builder(other);
  }
  
  /**
   * RecordBuilder for FL_DateRange instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<FL_DateRange>
    implements org.apache.avro.data.RecordBuilder<FL_DateRange> {

    private long startDate;
    private influent.idl.FL_DateInterval interval;
    private long numBins;
    private long numIntervalsPerBin;

    /** Creates a new Builder */
    private Builder() {
      super(influent.idl.FL_DateRange.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(influent.idl.FL_DateRange.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing FL_DateRange instance */
    private Builder(influent.idl.FL_DateRange other) {
            super(influent.idl.FL_DateRange.SCHEMA$);
      if (isValidValue(fields()[0], other.startDate)) {
        this.startDate = data().deepCopy(fields()[0].schema(), other.startDate);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.interval)) {
        this.interval = data().deepCopy(fields()[1].schema(), other.interval);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.numBins)) {
        this.numBins = data().deepCopy(fields()[2].schema(), other.numBins);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.numIntervalsPerBin)) {
        this.numIntervalsPerBin = data().deepCopy(fields()[3].schema(), other.numIntervalsPerBin);
        fieldSetFlags()[3] = true;
      }
    }

    /** Gets the value of the 'startDate' field */
    public java.lang.Long getStartDate() {
      return startDate;
    }
    
    /** Sets the value of the 'startDate' field */
    public influent.idl.FL_DateRange.Builder setStartDate(long value) {
      validate(fields()[0], value);
      this.startDate = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'startDate' field has been set */
    public boolean hasStartDate() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'startDate' field */
    public influent.idl.FL_DateRange.Builder clearStartDate() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'interval' field */
    public influent.idl.FL_DateInterval getInterval() {
      return interval;
    }
    
    /** Sets the value of the 'interval' field */
    public influent.idl.FL_DateRange.Builder setInterval(influent.idl.FL_DateInterval value) {
      validate(fields()[1], value);
      this.interval = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'interval' field has been set */
    public boolean hasInterval() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'interval' field */
    public influent.idl.FL_DateRange.Builder clearInterval() {
      interval = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'numBins' field */
    public java.lang.Long getNumBins() {
      return numBins;
    }
    
    /** Sets the value of the 'numBins' field */
    public influent.idl.FL_DateRange.Builder setNumBins(long value) {
      validate(fields()[2], value);
      this.numBins = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'numBins' field has been set */
    public boolean hasNumBins() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'numBins' field */
    public influent.idl.FL_DateRange.Builder clearNumBins() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'numIntervalsPerBin' field */
    public java.lang.Long getNumIntervalsPerBin() {
      return numIntervalsPerBin;
    }
    
    /** Sets the value of the 'numIntervalsPerBin' field */
    public influent.idl.FL_DateRange.Builder setNumIntervalsPerBin(long value) {
      validate(fields()[3], value);
      this.numIntervalsPerBin = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'numIntervalsPerBin' field has been set */
    public boolean hasNumIntervalsPerBin() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'numIntervalsPerBin' field */
    public influent.idl.FL_DateRange.Builder clearNumIntervalsPerBin() {
      fieldSetFlags()[3] = false;
      return this;
    }

    public FL_DateRange build() {
      try {
        FL_DateRange record = new FL_DateRange();
        record.startDate = fieldSetFlags()[0] ? this.startDate : (java.lang.Long) defaultValue(fields()[0]);
        record.interval = fieldSetFlags()[1] ? this.interval : (influent.idl.FL_DateInterval) defaultValue(fields()[1]);
        record.numBins = fieldSetFlags()[2] ? this.numBins : (java.lang.Long) defaultValue(fields()[2]);
        record.numIntervalsPerBin = fieldSetFlags()[3] ? this.numIntervalsPerBin : (java.lang.Long) defaultValue(fields()[3]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
