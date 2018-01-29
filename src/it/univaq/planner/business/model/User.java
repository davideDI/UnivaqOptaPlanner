package it.univaq.planner.business.model;

import java.io.Serializable;
import java.util.List;

//LDIF EXAMPLE
/*
	cn: davdei
	cn: 228893
	uid: davdei1990
	carLicense: DNNDVD90R11E372F
	description: univaq test student
	objectclass: top
	objectclass: person
	objectclass: organizationalperson
	objectclass: inetorgperson
	objectclass: eduPerson
	givenName: davide
	sn: de innocentis
	telephonenumber: 0866564603
	userpassword: {MD5}T8Y7PxDGrYUxpc51hcmUzQ==
	employeeNumber: 228893
	eduPersonScopedAffiliation: member@univaq;student@univaq
	#whenCreated: 20171018220500
	#whenChanged: 20171018220500
	mail: davide.de.innocentis
*/
public class User implements Serializable {

	private static final long serialVersionUID = -758181024979837797L;

	private String uid;
	private String cn;
	private String carLicense;
	private String description;
	private String givenName;
	private String sn;
	private String telephonenumber;
	private String password;
	private String employeeNumber;
	private String eduPersonScopedAffiliation;
	private String mail;
	
	private List<Role> roles;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getCarLicense() {
		return carLicense;
	}
	public void setCarLicense(String carLicense) {
		this.carLicense = carLicense;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getTelephonenumber() {
		return telephonenumber;
	}
	public void setTelephonenumber(String telephonenumber) {
		this.telephonenumber = telephonenumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getEduPersonScopedAffiliation() {
		return eduPersonScopedAffiliation;
	}
	public void setEduPersonScopedAffiliation(String eduPersonScopedAffiliation) {
		this.eduPersonScopedAffiliation = eduPersonScopedAffiliation;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	@Override
	public String toString() {
		return "User [uid=" + uid + ", cn=" + cn + ", carLicense=" + carLicense + ", description=" + description
				+ ", givenName=" + givenName + ", sn=" + sn + ", telephonenumber=" + telephonenumber + ", password="
				+ password + ", employeeNumber=" + employeeNumber + ", eduPersonScopedAffiliation="
				+ eduPersonScopedAffiliation + ", mail=" + mail + ", roles=" + roles + "]";
	}
	
}
