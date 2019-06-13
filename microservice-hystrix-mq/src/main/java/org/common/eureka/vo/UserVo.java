package org.common.eureka.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserVo implements Serializable {

	private String userId;

	private String address;

	private String age;

	private String birthday;

	private String createTime;

	private String createUserId;

	private String credentialsSalt;

	private String email;

	private String enabledTime;

	private String idCard;

	private String invalidTime;

	private String isSuperAdmin;

	private String lastUpdateTime;

	private String lastUpdateUserId;

	private String password;

	private String place;

	private String realName;

	private String remark;

	private String sex;

	private String status;

	private String telephone;

	private String userName;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getAge() {
		return age;
	}



	public void setAge(String age) {
		this.age = age;
	}



	public String getBirthday() {
		return birthday;
	}



	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}



	public String getCreateTime() {
		return createTime;
	}



	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}



	public String getCreateUserId() {
		return createUserId;
	}



	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}



	public String getCredentialsSalt() {
		return credentialsSalt;
	}



	public void setCredentialsSalt(String credentialsSalt) {
		this.credentialsSalt = credentialsSalt;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getEnabledTime() {
		return enabledTime;
	}



	public void setEnabledTime(String enabledTime) {
		this.enabledTime = enabledTime;
	}



	public String getIdCard() {
		return idCard;
	}



	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}



	public String getInvalidTime() {
		return invalidTime;
	}



	public void setInvalidTime(String invalidTime) {
		this.invalidTime = invalidTime;
	}



	public String getIsSuperAdmin() {
		return isSuperAdmin;
	}



	public void setIsSuperAdmin(String isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}



	public String getLastUpdateTime() {
		return lastUpdateTime;
	}



	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}



	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}



	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getPlace() {
		return place;
	}



	public void setPlace(String place) {
		this.place = place;
	}



	public String getRealName() {
		return realName;
	}



	public void setRealName(String realName) {
		this.realName = realName;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getSex() {
		return sex;
	}



	public void setSex(String sex) {
		this.sex = sex;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getTelephone() {
		return telephone;
	}



	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	@Override
	public String toString() {
		return "SecUser [userId=" + userId + ", address=" + address + ", age=" + age + ", birthday=" + birthday
				+ ", createTime=" + createTime + ", email=" + email + ", idCard=" + idCard + ", realName=" + realName
				+ ", remark=" + remark + ", sex=" + sex + ", status=" + status + ", telephone=" + telephone
				+ ", userName=" + userName + "]";
	}
}