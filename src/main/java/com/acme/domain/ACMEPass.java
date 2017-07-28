package com.acme.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.xml.bind.DatatypeConverter;

/**
 * A ACMEPass.
 */
@Entity
@Table(name = "acmepass")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ACMEPass extends AbstractDatedEntity implements Serializable {

	private static final long serialVersionUID = 1L;
    private static final String envKey = System.getenv("ACMEPASS_KEY");
    private static final byte[] c = new byte[]{
            (byte) Integer.parseInt(envKey.substring(0,2),16),
            (byte) Integer.parseInt(envKey.substring(2,4),16),
            (byte) Integer.parseInt(envKey.substring(4,6),16),
            (byte) Integer.parseInt(envKey.substring(6,8),16),
            (byte) Integer.parseInt(envKey.substring(8,10),16),
            (byte) Integer.parseInt(envKey.substring(10,12),16),
            (byte) Integer.parseInt(envKey.substring(12,14),16),
            (byte) Integer.parseInt(envKey.substring(14,16),16),
            (byte) Integer.parseInt(envKey.substring(16,18),16),
            (byte) Integer.parseInt(envKey.substring(18,20),16),
            (byte) Integer.parseInt(envKey.substring(20,22),16),
            (byte) Integer.parseInt(envKey.substring(22,24),16),
            (byte) Integer.parseInt(envKey.substring(24,26),16),
            (byte) Integer.parseInt(envKey.substring(26,28),16),
            (byte) Integer.parseInt(envKey.substring(28,30),16),
            (byte) Integer.parseInt(envKey.substring(30,32),16)
    };


    private static final Key k = new SecretKeySpec(c, "AES");

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 3)
	@Column(name = "site", nullable = false)
	private String site;

	@NotNull
	@Column(name = "login", nullable = false)
	private String login;

	@NotNull
	@Column(name = "password", nullable = false)
	private String password;

	@ManyToOne
	@NotNull
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSite() {
		return site;
	}

	public ACMEPass site(String site) {
		this.site = site;
		return this;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getLogin() {
		return login;
	}

	public ACMEPass login(String login) {
		this.login = login;
		return this;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		if (password != null) {
			try {
				Cipher c = Cipher.getInstance("AES");
				c.init(Cipher.DECRYPT_MODE, k);
				return new String(c.doFinal(DatatypeConverter.parseBase64Binary(password)));
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
				Logger.getLogger(ACMEPass.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return null;
	}

	public ACMEPass password(String password) {
		setPassword(password);
		return this;
	}

	public void setPassword(String password) {
		try {
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.ENCRYPT_MODE, k);
			this.password = DatatypeConverter.printBase64Binary(c.doFinal(password.getBytes()));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
			Logger.getLogger(ACMEPass.class.getName()).log(Level.SEVERE, null, ex);
			this.password = null;
		}
	}

	public User getUser() {
		return user;
	}

	public ACMEPass user(User user) {
		this.user = user;
		return this;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ACMEPass acmePass = (ACMEPass) o;
		if (acmePass.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, acmePass.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "ACMEPass{"
			+ "id=" + id
			+ ", site='" + site + "'"
			+ ", login='" + login + "'"
			+ ", password='" + password + "'"
			+ ", createdDate='" + createdDate + "'"
			+ ", lastModifiedDate='" + lastModifiedDate + "'"
			+ '}';
	}
}
