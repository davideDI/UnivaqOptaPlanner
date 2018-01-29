package it.univaq.planner.presentation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.univaq.planner.business.model.User;
import it.univaq.planner.common.spring.Utility;

@RequestMapping(value="/admin")
@Controller
public class TestController extends ABaseController {

	@RequestMapping(value="/test", method=RequestMethod.GET)
	public String test() {
		
		User user = Utility.getUtente();
		System.out.println(user);
		
		List<String> exampleDbConnection = testConnection();
		System.out.println(exampleDbConnection);
		
		return "admin.index";
		
	}
	
	@RequestMapping(value="/sicurezza", method=RequestMethod.GET)
	public String securityCheck() {
	
		//TODO autorizzazione
		return "admin.index";
		
	}
	
	@Autowired
	private DataSource dataSource;
	
	private List<String> testConnection() {
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<String> listGroups = new ArrayList<>();
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select * from groups");
			rs = st.executeQuery();
			while (rs.next()) {
				listGroups.add(rs.getString("name"));
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
		return listGroups;
		
	}
	
}
