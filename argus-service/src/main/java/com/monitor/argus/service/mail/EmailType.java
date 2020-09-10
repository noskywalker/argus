package com.monitor.argus.service.mail;

public enum EmailType {
	REGISTER("0201002", "registEmail.vm", false);

	private String value;
	private String cnName;
	private boolean flag;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	EmailType(String value, String cnName, boolean flag) {
		this.value = value;
		this.cnName = cnName;
		this.flag = flag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public static boolean getSendFlag(String value) {
		boolean sendFlag = true;
		for (EmailType ss : EmailType.values()) {
			if (value.equals(ss.getValue())) {
				sendFlag = ss.isFlag();
			}
		}
		return sendFlag;
	}
}
