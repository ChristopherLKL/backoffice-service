package eu.christopherlee.admin.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import eu.christopherlee.admin.tplink.model.Account;

public class TpLinkAccountRowMapper implements RowMapper<Account> {

	public Account mapRow(ResultSet rs, int line) throws SQLException {
		Account account = new Account();
		account.setAccountId(rs.getInt("account_id"));
		account.setCountryCode(rs.getString("country_code"));
		account.setEmail(rs.getString("email"));
		account.setRegTime(rs.getString("reg_time"));
		account.setToken(rs.getString("token"));
		return account;
	}

}
