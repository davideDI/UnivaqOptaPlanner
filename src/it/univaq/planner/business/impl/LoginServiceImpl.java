package it.univaq.planner.business.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Service;

import it.univaq.planner.business.LoginService;
import it.univaq.planner.business.model.Role;
import it.univaq.planner.business.model.User;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private DataSource dataSource;
	
	@Override
	public User authenticate(String arg) throws Exception {
		
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl("ldap://localhost:389");
		contextSource.setBase("dc=test,dc=univaq,dc=it");
		contextSource.setUserDn("cn=admin,dc=test,dc=univaq,dc=it");
		contextSource.setPassword("secret");
		contextSource.afterPropertiesSet();

		LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
		ldapTemplate.afterPropertiesSet();

		OrFilter orFilter = new OrFilter();
		orFilter.or(new EqualsFilter("uid", arg))
				.or(new EqualsFilter("samaccountname", arg))
				.or(new EqualsFilter("cn", arg))
				.or(new EqualsFilter("name", arg))
				.or(new EqualsFilter("mail", arg));
		
//		boolean authed = ldapTemplate.authenticate("",
//										orFilter.encode(),
//									    "");
//		
//		if(!authed) 
//			throw new UsernameNotFoundException("");
		
		@SuppressWarnings("unchecked")
		List<User> templates = ldapTemplate.search("", orFilter.toString(), new AttributesMapper() {
            public User mapFromAttributes(Attributes attrs) throws NamingException {
            	
            	User user = new User();
            	
            	user.setCn((String) attrs.get("cn").get()); 
            	user.setUid((String) attrs.get("uid").get()); 
            	user.setCarLicense((String) attrs.get("carLicense").get()); 
            	user.setDescription((String) attrs.get("description").get()); 
            	user.setGivenName((String) attrs.get("givenName").get()); 
            	user.setSn((String) attrs.get("sn").get()); 
            	user.setTelephonenumber((String) attrs.get("telephonenumber").get()); 
            	user.setEmployeeNumber((String) attrs.get("employeeNumber").get()); 
            	user.setEduPersonScopedAffiliation((String) attrs.get("eduPersonScopedAffiliation").get()); 
            	user.setMail((String) attrs.get("mail").get()); 
            	
            	Object o = (Object) attrs.get("userPassword").get();
            	byte[] bytes = (byte[]) o;
            	String password = new String(bytes);
            	user.setPassword(password);
            	
            	return user;
                 
            }
        });
		
		User user = templates.get(0);
    	user.setRoles(getRoles(user.getCn()));

		return user;
		
	}
	
	private List<Role> getRoles(String cn) {
		
		List<Role> roles = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select tip_user.* from tip_user, users where tip_user.id = users.tip_user_id and users.cn = ?");
			st.setString(1, cn);
			rs = st.executeQuery();
			while (rs.next()) {
				Role roleTemp = new Role();
				roleTemp.setId(rs.getLong("id"));
				roleTemp.setName(rs.getNString("name"));
				roleTemp.setDescription(rs.getNString("description"));
				roles.add(roleTemp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}

		}
		
		return roles;
		
	}

}
