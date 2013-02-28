/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.continuuity.app.services;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Information returned for each Flow run.
 */
public class FlowRunRecord implements org.apache.thrift.TBase<FlowRunRecord, FlowRunRecord._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("FlowRunRecord");

  private static final org.apache.thrift.protocol.TField RUN_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("runId", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField START_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("startTime", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField END_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("endTime", org.apache.thrift.protocol.TType.I64, (short)3);
  private static final org.apache.thrift.protocol.TField END_STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("endStatus", org.apache.thrift.protocol.TType.STRING, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new FlowRunRecordStandardSchemeFactory());
    schemes.put(TupleScheme.class, new FlowRunRecordTupleSchemeFactory());
  }

  private String runId; // required
  private long startTime; // required
  private long endTime; // required
  private String endStatus; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    RUN_ID((short)1, "runId"),
    START_TIME((short)2, "startTime"),
    END_TIME((short)3, "endTime"),
    END_STATUS((short)4, "endStatus");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // RUN_ID
          return RUN_ID;
        case 2: // START_TIME
          return START_TIME;
        case 3: // END_TIME
          return END_TIME;
        case 4: // END_STATUS
          return END_STATUS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __STARTTIME_ISSET_ID = 0;
  private static final int __ENDTIME_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.RUN_ID, new org.apache.thrift.meta_data.FieldMetaData("runId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.START_TIME, new org.apache.thrift.meta_data.FieldMetaData("startTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.END_TIME, new org.apache.thrift.meta_data.FieldMetaData("endTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.END_STATUS, new org.apache.thrift.meta_data.FieldMetaData("endStatus", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(FlowRunRecord.class, metaDataMap);
  }

  public FlowRunRecord() {
  }

  public FlowRunRecord(
    String runId,
    long startTime,
    long endTime,
    String endStatus)
  {
    this();
    this.runId = runId;
    this.startTime = startTime;
    setStartTimeIsSet(true);
    this.endTime = endTime;
    setEndTimeIsSet(true);
    this.endStatus = endStatus;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public FlowRunRecord(FlowRunRecord other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetRunId()) {
      this.runId = other.runId;
    }
    this.startTime = other.startTime;
    this.endTime = other.endTime;
    if (other.isSetEndStatus()) {
      this.endStatus = other.endStatus;
    }
  }

  public FlowRunRecord deepCopy() {
    return new FlowRunRecord(this);
  }

  @Override
  public void clear() {
    this.runId = null;
    setStartTimeIsSet(false);
    this.startTime = 0;
    setEndTimeIsSet(false);
    this.endTime = 0;
    this.endStatus = null;
  }

  public String getRunId() {
    return this.runId;
  }

  public void setRunId(String runId) {
    this.runId = runId;
  }

  public void unsetRunId() {
    this.runId = null;
  }

  /** Returns true if field runId is set (has been assigned a value) and false otherwise */
  public boolean isSetRunId() {
    return this.runId != null;
  }

  public void setRunIdIsSet(boolean value) {
    if (!value) {
      this.runId = null;
    }
  }

  public long getStartTime() {
    return this.startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
    setStartTimeIsSet(true);
  }

  public void unsetStartTime() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __STARTTIME_ISSET_ID);
  }

  /** Returns true if field startTime is set (has been assigned a value) and false otherwise */
  public boolean isSetStartTime() {
    return EncodingUtils.testBit(__isset_bitfield, __STARTTIME_ISSET_ID);
  }

  public void setStartTimeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __STARTTIME_ISSET_ID, value);
  }

  public long getEndTime() {
    return this.endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
    setEndTimeIsSet(true);
  }

  public void unsetEndTime() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ENDTIME_ISSET_ID);
  }

  /** Returns true if field endTime is set (has been assigned a value) and false otherwise */
  public boolean isSetEndTime() {
    return EncodingUtils.testBit(__isset_bitfield, __ENDTIME_ISSET_ID);
  }

  public void setEndTimeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ENDTIME_ISSET_ID, value);
  }

  public String getEndStatus() {
    return this.endStatus;
  }

  public void setEndStatus(String endStatus) {
    this.endStatus = endStatus;
  }

  public void unsetEndStatus() {
    this.endStatus = null;
  }

  /** Returns true if field endStatus is set (has been assigned a value) and false otherwise */
  public boolean isSetEndStatus() {
    return this.endStatus != null;
  }

  public void setEndStatusIsSet(boolean value) {
    if (!value) {
      this.endStatus = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case RUN_ID:
      if (value == null) {
        unsetRunId();
      } else {
        setRunId((String)value);
      }
      break;

    case START_TIME:
      if (value == null) {
        unsetStartTime();
      } else {
        setStartTime((Long)value);
      }
      break;

    case END_TIME:
      if (value == null) {
        unsetEndTime();
      } else {
        setEndTime((Long)value);
      }
      break;

    case END_STATUS:
      if (value == null) {
        unsetEndStatus();
      } else {
        setEndStatus((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case RUN_ID:
      return getRunId();

    case START_TIME:
      return Long.valueOf(getStartTime());

    case END_TIME:
      return Long.valueOf(getEndTime());

    case END_STATUS:
      return getEndStatus();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case RUN_ID:
      return isSetRunId();
    case START_TIME:
      return isSetStartTime();
    case END_TIME:
      return isSetEndTime();
    case END_STATUS:
      return isSetEndStatus();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof FlowRunRecord)
      return this.equals((FlowRunRecord)that);
    return false;
  }

  public boolean equals(FlowRunRecord that) {
    if (that == null)
      return false;

    boolean this_present_runId = true && this.isSetRunId();
    boolean that_present_runId = true && that.isSetRunId();
    if (this_present_runId || that_present_runId) {
      if (!(this_present_runId && that_present_runId))
        return false;
      if (!this.runId.equals(that.runId))
        return false;
    }

    boolean this_present_startTime = true;
    boolean that_present_startTime = true;
    if (this_present_startTime || that_present_startTime) {
      if (!(this_present_startTime && that_present_startTime))
        return false;
      if (this.startTime != that.startTime)
        return false;
    }

    boolean this_present_endTime = true;
    boolean that_present_endTime = true;
    if (this_present_endTime || that_present_endTime) {
      if (!(this_present_endTime && that_present_endTime))
        return false;
      if (this.endTime != that.endTime)
        return false;
    }

    boolean this_present_endStatus = true && this.isSetEndStatus();
    boolean that_present_endStatus = true && that.isSetEndStatus();
    if (this_present_endStatus || that_present_endStatus) {
      if (!(this_present_endStatus && that_present_endStatus))
        return false;
      if (!this.endStatus.equals(that.endStatus))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_runId = true && (isSetRunId());
    builder.append(present_runId);
    if (present_runId)
      builder.append(runId);

    boolean present_startTime = true;
    builder.append(present_startTime);
    if (present_startTime)
      builder.append(startTime);

    boolean present_endTime = true;
    builder.append(present_endTime);
    if (present_endTime)
      builder.append(endTime);

    boolean present_endStatus = true && (isSetEndStatus());
    builder.append(present_endStatus);
    if (present_endStatus)
      builder.append(endStatus);

    return builder.toHashCode();
  }

  public int compareTo(FlowRunRecord other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    FlowRunRecord typedOther = (FlowRunRecord)other;

    lastComparison = Boolean.valueOf(isSetRunId()).compareTo(typedOther.isSetRunId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRunId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.runId, typedOther.runId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetStartTime()).compareTo(typedOther.isSetStartTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStartTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.startTime, typedOther.startTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetEndTime()).compareTo(typedOther.isSetEndTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEndTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.endTime, typedOther.endTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetEndStatus()).compareTo(typedOther.isSetEndStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEndStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.endStatus, typedOther.endStatus);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("FlowRunRecord(");
    boolean first = true;

    sb.append("runId:");
    if (this.runId == null) {
      sb.append("null");
    } else {
      sb.append(this.runId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("startTime:");
    sb.append(this.startTime);
    first = false;
    if (!first) sb.append(", ");
    sb.append("endTime:");
    sb.append(this.endTime);
    first = false;
    if (!first) sb.append(", ");
    sb.append("endStatus:");
    if (this.endStatus == null) {
      sb.append("null");
    } else {
      sb.append(this.endStatus);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class FlowRunRecordStandardSchemeFactory implements SchemeFactory {
    public FlowRunRecordStandardScheme getScheme() {
      return new FlowRunRecordStandardScheme();
    }
  }

  private static class FlowRunRecordStandardScheme extends StandardScheme<FlowRunRecord> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, FlowRunRecord struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // RUN_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.runId = iprot.readString();
              struct.setRunIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // START_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.startTime = iprot.readI64();
              struct.setStartTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // END_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.endTime = iprot.readI64();
              struct.setEndTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // END_STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.endStatus = iprot.readString();
              struct.setEndStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, FlowRunRecord struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.runId != null) {
        oprot.writeFieldBegin(RUN_ID_FIELD_DESC);
        oprot.writeString(struct.runId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(START_TIME_FIELD_DESC);
      oprot.writeI64(struct.startTime);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(END_TIME_FIELD_DESC);
      oprot.writeI64(struct.endTime);
      oprot.writeFieldEnd();
      if (struct.endStatus != null) {
        oprot.writeFieldBegin(END_STATUS_FIELD_DESC);
        oprot.writeString(struct.endStatus);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class FlowRunRecordTupleSchemeFactory implements SchemeFactory {
    public FlowRunRecordTupleScheme getScheme() {
      return new FlowRunRecordTupleScheme();
    }
  }

  private static class FlowRunRecordTupleScheme extends TupleScheme<FlowRunRecord> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, FlowRunRecord struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetRunId()) {
        optionals.set(0);
      }
      if (struct.isSetStartTime()) {
        optionals.set(1);
      }
      if (struct.isSetEndTime()) {
        optionals.set(2);
      }
      if (struct.isSetEndStatus()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetRunId()) {
        oprot.writeString(struct.runId);
      }
      if (struct.isSetStartTime()) {
        oprot.writeI64(struct.startTime);
      }
      if (struct.isSetEndTime()) {
        oprot.writeI64(struct.endTime);
      }
      if (struct.isSetEndStatus()) {
        oprot.writeString(struct.endStatus);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, FlowRunRecord struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.runId = iprot.readString();
        struct.setRunIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.startTime = iprot.readI64();
        struct.setStartTimeIsSet(true);
      }
      if (incoming.get(2)) {
        struct.endTime = iprot.readI64();
        struct.setEndTimeIsSet(true);
      }
      if (incoming.get(3)) {
        struct.endStatus = iprot.readString();
        struct.setEndStatusIsSet(true);
      }
    }
  }

}

