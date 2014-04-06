/*-
 *
 *  This file is part of Oracle NoSQL Database
 *  Copyright (C) 2011, 2014 Oracle and/or its affiliates.  All rights reserved.
 *
 * If you have received this file as part of Oracle NoSQL Database the
 * following applies to the work as a whole:
 *
 *   Oracle NoSQL Database server software is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Affero
 *   General Public License as published by the Free Software Foundation,
 *   version 3.
 *
 *   Oracle NoSQL Database is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   Affero General Public License for more details.
 *
 * If you have received this file as part of Oracle NoSQL Database Client or
 * distributed separately the following applies:
 *
 *   Oracle NoSQL Database client software is free software: you can
 *   redistribute it and/or modify it under the terms of the Apache License
 *   as published by the Apache Software Foundation, version 2.0.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and/or the Apache License in the LICENSE file along with Oracle NoSQL
 * Database client or server distribution.  If not, see
 * <http://www.gnu.org/licenses/>
 * or
 * <http://www.apache.org/licenses/LICENSE-2.0>.
 *
 * An active Oracle commercial licensing agreement for this product supersedes
 * these licenses and in such case the license notices, but not the copyright
 * notice, may be removed by you in connection with your distribution that is
 * in accordance with the commercial licensing terms.
 *
 * For more information please contact:
 *
 * berkeleydb-info_us@oracle.com
 *
 */
package avro;  
@SuppressWarnings("all")
public class Address extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Address\",\"namespace\":\"avro\",\"fields\":[{\"name\":\"street\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"\"},{\"name\":\"city\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"\"},{\"name\":\"state\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"\"},{\"name\":\"zip\",\"type\":\"int\",\"default\":0}]}");
  @Deprecated public java.lang.String street;
  @Deprecated public java.lang.String city;
  @Deprecated public java.lang.String state;
  @Deprecated public int zip;
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return street;
    case 1: return city;
    case 2: return state;
    case 3: return zip;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: street = (java.lang.String)value$; break;
    case 1: city = (java.lang.String)value$; break;
    case 2: state = (java.lang.String)value$; break;
    case 3: zip = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'street' field.
   */
  public java.lang.String getStreet() {
    return street;
  }

  /**
   * Sets the value of the 'street' field.
   * @param value the value to set.
   */
  public void setStreet(java.lang.String value) {
    this.street = value;
  }

  /**
   * Gets the value of the 'city' field.
   */
  public java.lang.String getCity() {
    return city;
  }

  /**
   * Sets the value of the 'city' field.
   * @param value the value to set.
   */
  public void setCity(java.lang.String value) {
    this.city = value;
  }

  /**
   * Gets the value of the 'state' field.
   */
  public java.lang.String getState() {
    return state;
  }

  /**
   * Sets the value of the 'state' field.
   * @param value the value to set.
   */
  public void setState(java.lang.String value) {
    this.state = value;
  }

  /**
   * Gets the value of the 'zip' field.
   */
  public java.lang.Integer getZip() {
    return zip;
  }

  /**
   * Sets the value of the 'zip' field.
   * @param value the value to set.
   */
  public void setZip(java.lang.Integer value) {
    this.zip = value;
  }

  /** Creates a new Address RecordBuilder */
  public static avro.Address.Builder newBuilder() {
    return new avro.Address.Builder();
  }
  
  /** Creates a new Address RecordBuilder by copying an existing Builder */
  public static avro.Address.Builder newBuilder(avro.Address.Builder other) {
    return new avro.Address.Builder(other);
  }
  
  /** Creates a new Address RecordBuilder by copying an existing Address instance */
  public static avro.Address.Builder newBuilder(avro.Address other) {
    return new avro.Address.Builder(other);
  }
  
  /**
   * RecordBuilder for Address instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Address>
    implements org.apache.avro.data.RecordBuilder<Address> {

    private java.lang.String street;
    private java.lang.String city;
    private java.lang.String state;
    private int zip;

    /** Creates a new Builder */
    private Builder() {
      super(avro.Address.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(avro.Address.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing Address instance */
    private Builder(avro.Address other) {
            super(avro.Address.SCHEMA$);
      if (isValidValue(fields()[0], other.street)) {
        this.street = (java.lang.String) data().deepCopy(fields()[0].schema(), other.street);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.city)) {
        this.city = (java.lang.String) data().deepCopy(fields()[1].schema(), other.city);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.state)) {
        this.state = (java.lang.String) data().deepCopy(fields()[2].schema(), other.state);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.zip)) {
        this.zip = (java.lang.Integer) data().deepCopy(fields()[3].schema(), other.zip);
        fieldSetFlags()[3] = true;
      }
    }

    /** Gets the value of the 'street' field */
    public java.lang.String getStreet() {
      return street;
    }
    
    /** Sets the value of the 'street' field */
    public avro.Address.Builder setStreet(java.lang.String value) {
      validate(fields()[0], value);
      this.street = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'street' field has been set */
    public boolean hasStreet() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'street' field */
    public avro.Address.Builder clearStreet() {
      street = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'city' field */
    public java.lang.String getCity() {
      return city;
    }
    
    /** Sets the value of the 'city' field */
    public avro.Address.Builder setCity(java.lang.String value) {
      validate(fields()[1], value);
      this.city = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'city' field has been set */
    public boolean hasCity() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'city' field */
    public avro.Address.Builder clearCity() {
      city = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'state' field */
    public java.lang.String getState() {
      return state;
    }
    
    /** Sets the value of the 'state' field */
    public avro.Address.Builder setState(java.lang.String value) {
      validate(fields()[2], value);
      this.state = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'state' field has been set */
    public boolean hasState() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'state' field */
    public avro.Address.Builder clearState() {
      state = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'zip' field */
    public java.lang.Integer getZip() {
      return zip;
    }
    
    /** Sets the value of the 'zip' field */
    public avro.Address.Builder setZip(int value) {
      validate(fields()[3], value);
      this.zip = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'zip' field has been set */
    public boolean hasZip() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'zip' field */
    public avro.Address.Builder clearZip() {
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    public Address build() {
      try {
        Address record = new Address();
        record.street = fieldSetFlags()[0] ? this.street : (java.lang.String) defaultValue(fields()[0]);
        record.city = fieldSetFlags()[1] ? this.city : (java.lang.String) defaultValue(fields()[1]);
        record.state = fieldSetFlags()[2] ? this.state : (java.lang.String) defaultValue(fields()[2]);
        record.zip = fieldSetFlags()[3] ? this.zip : (java.lang.Integer) defaultValue(fields()[3]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
