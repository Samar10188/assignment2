package com.adep.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity implements Serializable /* implements UserDetails */{

	private static final long serialVersionUID = 8557562192452648775L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userId;
	
	@Column(unique = true)
	private String username;
	
	private String fullName;

	private String password;

	public UserEntity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * @Override
	 * 
	 * @JsonIgnore public Collection<? extends GrantedAuthority> getAuthorities() {
	 * // TODO Auto-generated method stub return null; }
	 * 
	 * @Override
	 * 
	 * @JsonIgnore public boolean isAccountNonExpired() { // TODO Auto-generated
	 * method stub return true; }
	 * 
	 * @Override
	 * 
	 * @JsonIgnore public boolean isAccountNonLocked() { // TODO Auto-generated
	 * method stub return true; }
	 * 
	 * @Override
	 * 
	 * @JsonIgnore public boolean isCredentialsNonExpired() { // TODO Auto-generated
	 * method stub return true; }
	 * 
	 * @Override
	 * 
	 * @JsonIgnore public boolean isEnabled() { // TODO Auto-generated method stub
	 * return true; }
	 */

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
