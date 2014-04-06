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
public class MemberInfo extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"MemberInfo\",\"namespace\":\"avro\",\"fields\":[{\"name\":\"name\",\"type\":{\"type\":\"record\",\"name\":\"FullName\",\"fields\":[{\"name\":\"first\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"\"},{\"name\":\"last\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"\"}]},\"default\":{}},{\"name\":\"age\",\"type\":\"int\",\"default\":0},{\"name\":\"address\",\"type\":{\"type\":\"record\",\"name\":\"Address\",\"fields\":[{\"name\":\"street\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"\"},{\"name\":\"city\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"\"},{\"name\":\"state\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"\"},{\"name\":\"zip\",\"type\":\"int\",\"default\":0}]},\"default\":{}}]}");
  @Deprecated public avro.FullName name;
  @Deprecated public int age;
  @Deprecated public avro.Address address;
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return name;
    case 1: return age;
    case 2: return address;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: name = (avro.FullName)value$; break;
    case 1: age = (java.lang.Integer)value$; break;
    case 2: address = (avro.Address)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'name' field.
   */
  public avro.FullName getName() {
    return name;
  }

  /**
   * Sets the value of the 'name' field.
   * @param value the value to set.
   */
  public void setName(avro.FullName value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'age' field.
   */
  public java.lang.Integer getAge() {
    return age;
  }

  /**
   * Sets the value of the 'age' field.
   * @param value the value to set.
   */
  public void setAge(java.lang.Integer value) {
    this.age = value;
  }

  /**
   * Gets the value of the 'address' field.
   */
  public avro.Address getAddress() {
    return address;
  }

  /**
   * Sets the value of the 'address' field.
   * @param value the value to set.
   */
  public void setAddress(avro.Address value) {
    this.address = value;
  }

  /** Creates a new MemberInfo RecordBuilder */
  public static avro.MemberInfo.Builder newBuilder() {
    return new avro.MemberInfo.Builder();
  }
  
  /** Creates a new MemberInfo RecordBuilder by copying an existing Builder */
  public static avro.MemberInfo.Builder newBuilder(avro.MemberInfo.Builder other) {
    return new avro.MemberInfo.Builder(other);
  }
  
  /** Creates a new MemberInfo RecordBuilder by copying an existing MemberInfo instance */
  public static avro.MemberInfo.Builder newBuilder(avro.MemberInfo other) {
    return new avro.MemberInfo.Builder(other);
  }
  
  /**
   * RecordBuilder for MemberInfo instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<MemberInfo>
    implements org.apache.avro.data.RecordBuilder<MemberInfo> {

    private avro.FullName name;
    private int age;
    private avro.Address address;

    /** Creates a new Builder */
    private Builder() {
      super(avro.MemberInfo.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(avro.MemberInfo.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing MemberInfo instance */
    private Builder(avro.MemberInfo other) {
            super(avro.MemberInfo.SCHEMA$);
      if (isValidValue(fields()[0], other.name)) {
        this.name = (avro.FullName) data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.age)) {
        this.age = (java.lang.Integer) data().deepCopy(fields()[1].schema(), other.age);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.address)) {
        this.address = (avro.Address) data().deepCopy(fields()[2].schema(), other.address);
        fieldSetFlags()[2] = true;
      }
    }

    /** Gets the value of the 'name' field */
    public avro.FullName getName() {
      return name;
    }
    
    /** Sets the value of the 'name' field */
    public avro.MemberInfo.Builder setName(avro.FullName value) {
      validate(fields()[0], value);
      this.name = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'name' field has been set */
    public boolean hasName() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'name' field */
    public avro.MemberInfo.Builder clearName() {
      name = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'age' field */
    public java.lang.Integer getAge() {
      return age;
    }
    
    /** Sets the value of the 'age' field */
    public avro.MemberInfo.Builder setAge(int value) {
      validate(fields()[1], value);
      this.age = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'age' field has been set */
    public boolean hasAge() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'age' field */
    public avro.MemberInfo.Builder clearAge() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'address' field */
    public avro.Address getAddress() {
      return address;
    }
    
    /** Sets the value of the 'address' field */
    public avro.MemberInfo.Builder setAddress(avro.Address value) {
      validate(fields()[2], value);
      this.address = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'address' field has been set */
    public boolean hasAddress() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'address' field */
    public avro.MemberInfo.Builder clearAddress() {
      address = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    public MemberInfo build() {
      try {
        MemberInfo record = new MemberInfo();
        record.name = fieldSetFlags()[0] ? this.name : (avro.FullName) defaultValue(fields()[0]);
        record.age = fieldSetFlags()[1] ? this.age : (java.lang.Integer) defaultValue(fields()[1]);
        record.address = fieldSetFlags()[2] ? this.address : (avro.Address) defaultValue(fields()[2]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
