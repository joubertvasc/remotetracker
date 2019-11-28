package jv.android.utils.apn;

public class APN {

	public static final int AUTH_TYPE_UNKNOW = -1;
	public static final int AUTH_TYPE_NONE = 0;
	public static final int AUTH_TYPE_PAP = 1;
	public static final int AUTH_TYPE_CHAP = 2;
	public static final int AUTH_TYPE_PAP_OR_CHAP = 3;
	
	private int id;
	private String name;
	private String user;
	private String password;
	private String apn;
	private String mcc;
	private String mnc;
	private String type;
	private String server;
	private String proxy;
	private String port;
	private String mmsProxy;
	private String mmsPort;
	private String mmsc;
	private String current;
	private String carrier_enabled;
	private int authType;
	private String bearer;
	private String mvno_match_data;
	private String mvno_type;
	private String roaming_protocol;
	private String sub_id;

	public APN() {
		id = -1;
		name = "";
		user = "";
		password = "";
		apn = "";
		mcc = "";
		mnc = "";
		type = "default,supl";
		server = "";
		proxy = "";
		port = "";
		mmsProxy = "";
		mmsPort = "";
		mmsc = "";
		current = "";
        carrier_enabled = "";
		authType = AUTH_TYPE_NONE;
		bearer = "";
		mvno_match_data = "";
		mvno_type = "";
		roaming_protocol = "";
		sub_id = "";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getApn() {
		return apn;
	}
	public void setApn(String apn) {
		this.apn = apn;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getMnc() {
		return mnc;
	}
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public String getMmsProxy() {
		return mmsProxy;
	}
	public void setMmsProxy(String mmsProxy) {
		this.mmsProxy = mmsProxy;
	}
	public String getMmsPort() {
		return mmsPort;
	}
	public void setMmsPort(String mmsPort) {
		this.mmsPort = mmsPort;
	}
	public String getMmsc() {
		return mmsc;
	}
	public void setMmsc(String mmsc) {
		this.mmsc = mmsc;
	}

    public String getCarrier_enabled() {
        return carrier_enabled;
    }

    public void setCarrier_enabled(String carrier_enabled) {
        this.carrier_enabled = carrier_enabled;
    }

    public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public int getAuthType() {
		return authType;
	}
	public void setAuthType(int authType) {
		this.authType = authType;
	}

	public String getBearer() {
		return bearer;
	}

	public void setBearer(String bearer) {
		this.bearer = bearer;
	}

	public String getMvno_match_data() {
		return mvno_match_data;
	}

	public void setMvno_match_data(String mvno_match_data) {
		this.mvno_match_data = mvno_match_data;
	}

	public String getMvno_type() {
		return mvno_type;
	}

	public void setMvno_type(String mvno_type) {
		this.mvno_type = mvno_type;
	}

	public String getRoaming_protocol() {
		return roaming_protocol;
	}

	public void setRoaming_protocol(String roaming_protocol) {
		this.roaming_protocol = roaming_protocol;
	}

	public String getSub_id() {
		return sub_id;
	}

	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}

}
