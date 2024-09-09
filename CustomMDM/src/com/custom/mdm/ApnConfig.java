package com.custom.mdm;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Telephony;
import android.text.TextUtils;

public class ApnConfig implements Parcelable {
    private static final String TAG = "ApnConfig";

    private Integer id = -1;
    private String name;
    private String apn;
    private String proxy = "";
    private String port = "";
    private String user = "";
    private String server = "";
    private String password = "";
    private String mmsc = "";
    private String mcc;
    private String mnc;
    private String numeric;
    private String mmsproxy = "";
    private String mmsport = "";
    private Integer authType;
    private String type;
    private String protocol;
    private Integer carrierEnabled;
    private Integer bearer;
    private Integer bearerBitmask;
    private String roamingProtocol;
    private String mvnoType = "";
    private String mvnoMatchData = "";
    private Integer edited;
    private Integer userEditable;
    private Integer sourceType;

    public static final String APN_TYPE = "default,mms,supl,hipri,fota,cbs,xcap";
    public static final String APN_PROTOCOL_IPV4 = "IP";
    public static final String APN_PROTOCOL_IPV6 = "IPV6";
    public static final String APN_PROTOCOL_IPV4V6 = "IPV4V6";

    static final String[] sProjection = new String[] {
            Telephony.Carriers._ID,     // 0
            Telephony.Carriers.NAME,    // 1
            Telephony.Carriers.APN,     // 2
            Telephony.Carriers.PROXY,   // 3
            Telephony.Carriers.PORT,    // 4
            Telephony.Carriers.USER,    // 5
            Telephony.Carriers.SERVER,  // 6
            Telephony.Carriers.PASSWORD, // 7
            Telephony.Carriers.MMSC,    // 8
            Telephony.Carriers.MCC,     // 9
            Telephony.Carriers.MNC,     // 10
            Telephony.Carriers.NUMERIC, // 11
            Telephony.Carriers.MMSPROXY,// 12
            Telephony.Carriers.MMSPORT, // 13
            Telephony.Carriers.AUTH_TYPE, // 14
            Telephony.Carriers.TYPE,    // 15
            Telephony.Carriers.PROTOCOL, // 16
            Telephony.Carriers.CARRIER_ENABLED, // 17
            Telephony.Carriers.BEARER,  // 18
            "bearer_bitmask", //Telephony.Carriers.BEARER_BITMASK, // 19
            Telephony.Carriers.ROAMING_PROTOCOL, // 20
            Telephony.Carriers.MVNO_TYPE, // 21
            Telephony.Carriers.MVNO_MATCH_DATA, // 22
            "edited", //Telephony.Carriers.EDITED_STATUS, // 23
            "user_editable", //Telephony.Carriers.USER_EDITABLE, //24
            "sourcetype" //MtkTelephony.Carriers.SOURCE_TYPE //25
    };

    static final int ID_INDEX = 0;
    static final int NAME_INDEX = 1;
    static final int APN_INDEX = 2;
    static final int PROXY_INDEX = 3;
    static final int PORT_INDEX = 4;
    static final int USER_INDEX = 5;
    static final int SERVER_INDEX = 6;
    static final int PASSWORD_INDEX = 7;
    static final int MMSC_INDEX = 8;
    static final int MCC_INDEX = 9;
    static final int MNC_INDEX = 10;
    static final int NUMERIC_INDEX = 11;
    static final int MMSPROXY_INDEX = 12;
    static final int MMSPORT_INDEX = 13;
    static final int AUTH_TYPE_INDEX = 14;
    static final int TYPE_INDEX = 15;
    static final int PROTOCOL_INDEX = 16;
    static final int CARRIER_ENABLED_INDEX = 17;
    static final int BEARER_INDEX = 18;
    static final int BEARER_BITMASK_INDEX = 19;
    static final int ROAMING_PROTOCOL_INDEX = 20;
    static final int MVNO_TYPE_INDEX = 21;
    static final int MVNO_MATCH_DATA_INDEX = 22;
    static final int EDITED_INDEX = 23;
    static final int USER_EDITABLE_INDEX = 24;
    static final int SOURCE_TYPE_INDEX = 25;

    public ApnConfig(String name, String apn, String mcc, String mnc, String type) {
        this.name = name;
        this.apn = apn;
        this.mcc = mcc;
        this.mnc = mnc;
        this.numeric = mcc + mnc;
        this.type = TextUtils.isEmpty(type) ? APN_TYPE:type;
        this.edited = 1; //Telephony.Carriers.USER_EDITED
        this.userEditable = 1;
        this.sourceType = 1;
        this.authType = -1;
        this.carrierEnabled = 1;
        this.bearer = 0;
        this.bearerBitmask = 0;
        this.protocol = APN_PROTOCOL_IPV4;
        this.roamingProtocol = APN_PROTOCOL_IPV4;
    }

    public ApnConfig(ApnConfig apnConfig) {
        this.id = apnConfig.getId();
        this.name = apnConfig.getName();
        this.apn = apnConfig.getApn();
        this.mcc = apnConfig.getMcc();
        this.mnc = apnConfig.getMnc();
        this.numeric = apnConfig.getNumeric();
        this.type = apnConfig.getType();
        this.edited = apnConfig.getEdited();
        this.userEditable = apnConfig.getUserEditable();
        this.sourceType = apnConfig.getSourceType();
        this.authType = apnConfig.getAuthType();
        this.carrierEnabled = apnConfig.getCarrierEnabled();
        this.bearer = apnConfig.getBearer();
        this.bearerBitmask = apnConfig.getBearerBitmask();
        this.protocol = apnConfig.getProtocol();
        this.roamingProtocol = apnConfig.getRoamingProtocol();
        this.proxy = apnConfig.getProxy();
        this.port = apnConfig.getPort();
        this.user = apnConfig.getUser();
        this.server = apnConfig.getServer();
        this.password = apnConfig.getPassword();
        this.mmsc = apnConfig.getMmsc();
        this.mmsproxy = apnConfig.getMmsproxy();
        this.mmsport = apnConfig.getMmsport();
        this.mvnoType = apnConfig.getMvnoType();
        this.mvnoMatchData = apnConfig.getMvnoMatchData();
    }

    ApnConfig(ApnData apnData) {
        this.id = Integer.parseInt(apnData.getString(ID_INDEX));
        this.edited = Integer.parseInt(apnData.getString(EDITED_INDEX));
        this.userEditable = Integer.parseInt(apnData.getString(USER_EDITABLE_INDEX));
        this.sourceType = Integer.parseInt(apnData.getString(SOURCE_TYPE_INDEX));
        this.authType = Integer.parseInt(apnData.getString(AUTH_TYPE_INDEX));
        this.carrierEnabled = Integer.parseInt(apnData.getString(CARRIER_ENABLED_INDEX));
        this.bearer = Integer.parseInt(apnData.getString(BEARER_INDEX));
        this.bearerBitmask = Integer.parseInt(apnData.getString(BEARER_BITMASK_INDEX));
        this.name = apnData.getString(NAME_INDEX);
        this.apn = apnData.getString(APN_INDEX);
        this.mcc = apnData.getString(MCC_INDEX);
        this.mnc = apnData.getString(MNC_INDEX);
        this.numeric = apnData.getString(NUMERIC_INDEX);
        this.type = apnData.getString(TYPE_INDEX);
        this.protocol = apnData.getString(PROTOCOL_INDEX);
        this.roamingProtocol = apnData.getString(ROAMING_PROTOCOL_INDEX);
        this.proxy = apnData.getString(PROXY_INDEX);
        this.port = apnData.getString(PORT_INDEX);
        this.user = apnData.getString(USER_INDEX);
        this.server = apnData.getString(SERVER_INDEX);
        this.password = apnData.getString(PASSWORD_INDEX);
        this.mmsc = apnData.getString(MMSC_INDEX);
        this.mmsproxy = apnData.getString(MMSPROXY_INDEX);
        this.mmsport = apnData.getString(MMSPORT_INDEX);
        this.mvnoType = apnData.getString(MVNO_TYPE_INDEX);
        this.mvnoMatchData = apnData.getString(MVNO_MATCH_DATA_INDEX);
    }

    ApnConfig(ApnData apnData, String data) {
        this.id = apnData.getInteger(ID_INDEX);
        this.edited = apnData.getInteger(EDITED_INDEX, 1);
        this.userEditable = apnData.getInteger(USER_EDITABLE_INDEX, 1);
        this.sourceType = apnData.getInteger(SOURCE_TYPE_INDEX, 1);
        this.authType = apnData.getInteger(AUTH_TYPE_INDEX, -1);
        this.carrierEnabled = apnData.getInteger(CARRIER_ENABLED_INDEX, 1);
        this.bearer = apnData.getInteger(BEARER_INDEX, 0);
        this.bearerBitmask = apnData.getInteger(BEARER_BITMASK_INDEX, 0);
        this.name = apnData.getString(NAME_INDEX);
        this.apn = apnData.getString(APN_INDEX);
        this.mcc = apnData.getString(MCC_INDEX);
        this.mnc = apnData.getString(MNC_INDEX);
        this.numeric = apnData.getString(NUMERIC_INDEX);
        this.type = apnData.getString(TYPE_INDEX);
        this.protocol = apnData.getString(PROTOCOL_INDEX);
        this.roamingProtocol = apnData.getString(ROAMING_PROTOCOL_INDEX);
        this.proxy = apnData.getString(PROXY_INDEX);
        this.port = apnData.getString(PORT_INDEX);
        this.user = apnData.getString(USER_INDEX);
        this.server = apnData.getString(SERVER_INDEX);
        this.password = apnData.getString(PASSWORD_INDEX);
        this.mmsc = apnData.getString(MMSC_INDEX);
        this.mmsproxy = apnData.getString(MMSPROXY_INDEX);
        this.mmsport = apnData.getString(MMSPORT_INDEX);
        this.mvnoType = apnData.getString(MVNO_TYPE_INDEX);
        this.mvnoMatchData = apnData.getString(MVNO_MATCH_DATA_INDEX);
    }


    protected ApnConfig(Parcel in) {
        if (in.readByte() == 0) {
            id = -1;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        apn = in.readString();
        proxy = in.readString();
        port = in.readString();
        user = in.readString();
        server = in.readString();
        password = in.readString();
        mmsc = in.readString();
        mcc = in.readString();
        mnc = in.readString();
        numeric = in.readString();
        mmsproxy = in.readString();
        mmsport = in.readString();
        if (in.readByte() == 0) {
            authType = -1;
        } else {
            authType = in.readInt();
        }
        type = in.readString();
        protocol = in.readString();
        if (in.readByte() == 0) {
            carrierEnabled = 1;
        } else {
            carrierEnabled = in.readInt();
        }
        if (in.readByte() == 0) {
            bearer = 0;
        } else {
            bearer = in.readInt();
        }
        if (in.readByte() == 0) {
            bearerBitmask = 0;
        } else {
            bearerBitmask = in.readInt();
        }
        roamingProtocol = in.readString();
        mvnoType = in.readString();
        mvnoMatchData = in.readString();
        if (in.readByte() == 0) {
            edited = 1;
        } else {
            edited = in.readInt();
        }
        if (in.readByte() == 0) {
            userEditable = 1;
        } else {
            userEditable = in.readInt();
        }
        if (in.readByte() == 0) {
            sourceType = 1;
        } else {
            sourceType = in.readInt();
        }
    }

    public static final Creator<ApnConfig> CREATOR = new Creator<ApnConfig>() {
        @Override
        public ApnConfig createFromParcel(Parcel in) {
            return new ApnConfig(in);
        }

        @Override
        public ApnConfig[] newArray(int size) {
            return new ApnConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(apn);
        dest.writeString(proxy);
        dest.writeString(port);
        dest.writeString(user);
        dest.writeString(server);
        dest.writeString(password);
        dest.writeString(mmsc);
        dest.writeString(mcc);
        dest.writeString(mnc);
        dest.writeString(numeric);
        dest.writeString(mmsproxy);
        dest.writeString(mmsport);
        if (authType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(authType);
        }
        dest.writeString(type);
        dest.writeString(protocol);
        if (carrierEnabled == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(carrierEnabled);
        }
        if (bearer == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bearer);
        }
        if (bearerBitmask == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bearerBitmask);
        }
        dest.writeString(roamingProtocol);
        dest.writeString(mvnoType);
        dest.writeString(mvnoMatchData);
        if (edited == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(edited);
        }
        if (userEditable == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userEditable);
        }
        if (sourceType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sourceType);
        }
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(sProjection[NAME_INDEX], name);
        values.put(sProjection[APN_INDEX], apn);
        values.put(sProjection[NUMERIC_INDEX], mcc + mnc);
        values.put(sProjection[MCC_INDEX], mcc);
        values.put(sProjection[MNC_INDEX], mnc);
        values.put(sProjection[TYPE_INDEX], type);
        values.put(sProjection[EDITED_INDEX], edited);
        values.put(sProjection[USER_EDITABLE_INDEX], userEditable);
        values.put(sProjection[SOURCE_TYPE_INDEX], sourceType);
        values.put(sProjection[AUTH_TYPE_INDEX], authType);
        values.put(sProjection[CARRIER_ENABLED_INDEX], carrierEnabled);
        values.put(sProjection[BEARER_INDEX], bearer);
        values.put(sProjection[BEARER_BITMASK_INDEX], bearerBitmask);
        values.put(sProjection[PROTOCOL_INDEX], protocol);
        values.put(sProjection[ROAMING_PROTOCOL_INDEX], roamingProtocol);
        values.put(sProjection[PROXY_INDEX], proxy);
        values.put(sProjection[PORT_INDEX], port);
        values.put(sProjection[USER_INDEX], user);
        values.put(sProjection[SERVER_INDEX], server);
        values.put(sProjection[PASSWORD_INDEX], password);
        values.put(sProjection[MMSC_INDEX], mmsc);
        values.put(sProjection[MMSPORT_INDEX], mmsport);
        values.put(sProjection[MVNO_TYPE_INDEX], mvnoType);
        values.put(sProjection[MVNO_MATCH_DATA_INDEX], mvnoMatchData);
        return values;
    }

    public Integer getId() {
        return id;
    }

    public ApnConfig setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ApnConfig setName(String name) {
        this.name = name;
        return this;
    }

    public String getApn() {
        return apn;
    }

    public ApnConfig setApn(String apn) {
        this.apn = apn;
        return this;
    }

    public String getProxy() {
        return proxy;
    }

    public ApnConfig setProxy(String proxy) {
        this.proxy = proxy;
        return this;
    }

    public String getPort() {
        return port;
    }

    public ApnConfig setPort(String port) {
        this.port = port;
        return this;
    }

    public String getUser() {
        return user;
    }

    public ApnConfig setUser(String user) {
        this.user = user;
        return this;
    }

    public String getServer() {
        return server;
    }

    public ApnConfig setServer(String server) {
        this.server = server;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ApnConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getMmsc() {
        return mmsc;
    }

    public ApnConfig setMmsc(String mmsc) {
        this.mmsc = mmsc;
        return this;
    }

    public String getMcc() {
        return mcc;
    }

    public ApnConfig setMcc(String mcc) {
        this.mcc = mcc;
        return this;
    }

    public String getMnc() {
        return mnc;
    }

    public ApnConfig setMnc(String mnc) {
        this.mnc = mnc;
        return this;
    }

    public String getNumeric() {
        return numeric;
    }

    public ApnConfig setNumeric(String numeric) {
        this.numeric = numeric;
        return this;
    }

    public String getMmsproxy() {
        return mmsproxy;
    }

    public ApnConfig setMmsproxy(String mmsproxy) {
        this.mmsproxy = mmsproxy;
        return this;
    }

    public String getMmsport() {
        return mmsport;
    }

    public ApnConfig setMmsport(String mmsport) {
        this.mmsport = mmsport;
        return this;
    }

    public Integer getAuthType() {
        return authType;
    }

    public ApnConfig setAuthType(Integer authType) {
        this.authType = authType;
        return this;
    }

    public String getType() {
        return type;
    }

    public ApnConfig setType(String type) {
        this.type = type;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public ApnConfig setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public Integer getCarrierEnabled() {
        return carrierEnabled;
    }

    public ApnConfig setCarrierEnabled(Integer carrierEnabled) {
        this.carrierEnabled = carrierEnabled;
        return this;
    }

    public Integer getBearer() {
        return bearer;
    }

    public ApnConfig setBearer(Integer bearer) {
        this.bearer = bearer;
        return this;
    }

    public Integer getBearerBitmask() {
        return bearerBitmask;
    }

    public ApnConfig setBearerBitmask(Integer bearerBitmask) {
        this.bearerBitmask = bearerBitmask;
        return this;
    }

    public String getRoamingProtocol() {
        return roamingProtocol;
    }

    public ApnConfig setRoamingProtocol(String roamingProtocol) {
        this.roamingProtocol = roamingProtocol;
        return this;
    }

    public String getMvnoType() {
        return mvnoType;
    }

    public ApnConfig setMvnoType(String mvnoType) {
        this.mvnoType = mvnoType;
        return this;
    }

    public String getMvnoMatchData() {
        return mvnoMatchData;
    }

    public ApnConfig setMvnoMatchData(String mvnoMatchData) {
        this.mvnoMatchData = mvnoMatchData;
        return this;
    }

    public Integer getEdited() {
        return edited;
    }

    public ApnConfig setEdited(Integer edited) {
        this.edited = edited;
        return this;
    }

    public Integer getUserEditable() {
        return userEditable;
    }

    public ApnConfig setUserEditable(Integer userEditable) {
        this.userEditable = userEditable;
        return this;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public ApnConfig setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    @Override
    public String toString() {
        return "ApnConfig{" +
                "id=" + id + ", name='" + name + '\'' + ", apn='" + apn + '\'' +
                ", proxy='" + proxy + '\'' + ", port='" + port + '\'' +
                ", user='" + user + '\'' + ", server='" + server + '\'' +
                ", password='" + password + '\'' + ", mmsc='" + mmsc + '\'' +
                ", mcc='" + mcc + '\'' + ", mnc='" + mnc + '\'' + ", numeric='" + numeric + '\'' +
                ", mmsproxy='" + mmsproxy + '\'' + ", mmsport='" + mmsport + '\'' +
                ", authType=" + authType + ", type='" + type + '\'' + ", protocol='" + protocol + '\'' +
                ", carrierEnabled=" + carrierEnabled + ", bearer=" + bearer +
                ", bearerBitmask=" + bearerBitmask + ", roamingProtocol='" + roamingProtocol + '\'' +
                ", mvnoType='" + mvnoType + '\'' + ", mvnoMatchData='" + mvnoMatchData + '\'' +
                ", edited=" + edited + ", userEditable=" + userEditable + ", sourceType=" + sourceType +
                '}';
    }

    static class ApnData {
        /**
         * The uri correspond to a database row of the apn data. This should be null if the apn is not in the database.
         */
        Uri mUri;

        /** Each element correspond to a column of the database row. */
        Object[] mData;

        ApnData(int numberOfField) {
            mData = new Object[numberOfField];
        }

        ApnData(Uri uri, Cursor cursor) {
            mUri = uri;
            mData = new Object[cursor.getColumnCount()];
            for (int i = 0; i < mData.length; i++) {
                switch (cursor.getType(i)) {
                    case Cursor.FIELD_TYPE_FLOAT:
                        mData[i] = cursor.getFloat(i);
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        mData[i] = cursor.getInt(i);
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        mData[i] = cursor.getString(i);
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        mData[i] = cursor.getBlob(i);
                        break;
                    default:
                        mData[i] = null;
                }
            }
        }

        Uri getUri() {
            return mUri;
        }

        void setUri(Uri uri) {
            mUri = uri;
        }

        Integer getInteger(int index) {
            return (Integer) mData[index];
        }

        Integer getInteger(int index, Integer defaultValue) {
            Integer val = getInteger(index);
            return val == null ? defaultValue : val;
        }

        String getString(int index) {
            return (String) mData[index];
        }
    }
}
