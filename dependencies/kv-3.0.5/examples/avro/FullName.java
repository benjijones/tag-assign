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
public class FullName extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"FullName\",\"namespace\":\"avro\",\"fields\":[{\"name\":\"first\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"\"},{\"name\":\"last\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"\"}]}");
  @Deprecated public java.lang.String first;
  @Deprecated public java.lang.String last;
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return first;
    case 1: return last;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: first = (java.lang.String)value$; break;
    case 1: last = (java.lang.String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'first' field.
   */
  public java.lang.String getFirst() {
    return first;
  }

  /**
   * Sets the value of the 'first' field.
   * @param value the value to set.
   */
  public void setFirst(java.lang.String value) {
    this.first = value;
  }

  /**
   * Gets the value of the 'last' field.
   */
  public java.lang.String getLast() {
    return last;
  }

  /**
   * Sets the value of the 'last' field.
   * @param value the value to set.
   */
  public void setLast(java.lang.String value) {
    this.last = value;
  }

  /** Creates a new FullName RecordBuilder */
  public static avro.FullName.Builder newBuilder() {
    return new avro.FullName.Builder();
  }
  
  /** Creates a new FullName RecordBuilder by copying an existing Builder */
  public static avro.FullName.Builder newBuilder(avro.FullName.Builder other) {
    return new avro.FullName.Builder(other);
  }
  
  /** Creates a new FullName RecordBuilder by copying an existing FullName instance */
  public static avro.FullName.Builder newBuilder(avro.FullName other) {
    return new avro.FullName.Builder(other);
  }
  
  /**
   * RecordBuilder for FullName instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<FullName>
    implements org.apache.avro.data.RecordBuilder<FullName> {

    private java.lang.String first;
    private java.lang.String last;

    /** Creates a new Builder */
    private Builder() {
      super(avro.FullName.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(avro.FullName.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing FullName instance */
    private Builder(avro.FullName other) {
            super(avro.FullName.SCHEMA$);
      if (isValidValue(fields()[0], other.first)) {
        this.first = (java.lang.String) data().deepCopy(fields()[0].schema(), other.first);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.last)) {
        this.last = (java.lang.String) data().deepCopy(fields()[1].schema(), other.last);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'first' field */
    public java.lang.String getFirst() {
      return first;
    }
    
    /** Sets the value of the 'first' field */
    public avro.FullName.Builder setFirst(java.lang.String value) {
      validate(fields()[0], value);
      this.first = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'first' field has been set */
    public boolean hasFirst() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'first' field */
    public avro.FullName.Builder clearFirst() {
      first = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'last' field */
    public java.lang.String getLast() {
      return last;
    }
    
    /** Sets the value of the 'last' field */
    public avro.FullName.Builder setLast(java.lang.String value) {
      validate(fields()[1], value);
      this.last = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'last' field has been set */
    public boolean hasLast() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'last' field */
    public avro.FullName.Builder clearLast() {
      last = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public FullName build() {
      try {
        FullName record = new FullName();
        record.first = fieldSetFlags()[0] ? this.first : (java.lang.String) defaultValue(fields()[0]);
        record.last = fieldSetFlags()[1] ? this.last : (java.lang.String) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
