package com.ap.config.security.userdetail;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ap.config.security.model.UserInformationDto;
import com.ap.domain.base.SessionDto;
import com.ap.domain.base.TenantId;
import com.ap.domain.base.UserId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "v_user")
public class UserDetail implements Serializable {

	@Id
	@Embedded
	private UserId userId;

	@Embedded
	private TenantId tenantId;

	@Column(name = "tenantName")
	private String tenantName;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;
	
	@Column(nullable = false)
    private boolean activated = false;
	
	public SessionDto toSessionDto() {
		return new SessionDto(this.username, this.userId, this.tenantName, this.tenantId, "0.0.0.0", "AP");
	}
	
	public UserInformationDto toUserInformationDto() {
		return new UserInformationDto(this.username, this.userId, this.tenantName, this.tenantId);
	}
}
