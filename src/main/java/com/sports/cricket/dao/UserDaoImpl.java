package com.sports.cricket.dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sports.cricket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Repository
public class UserDaoImpl implements UserDao, Serializable {

	//NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	DataSource dataSource;
	JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DataAccessException {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}*/

	@Override
	public User findById(Integer id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		String sql = "SELECT id, name, email FROM users WHERE id = ?";

		User result = null;
		try {

			result = (User)jdbcTemplate.queryForObject(sql,new Object[] { id }, new BeanPropertyRowMapper(User.class));

		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}

		return result;

	}

	@Override
	public List<User> findAll() {

		String sql = "SELECT * FROM users";

		List<User> result = jdbcTemplate.query(sql,  new BeanPropertyRowMapper(User.class));

				//namedParameterJdbcTemplate.query(sql, new UserMapper());

		return result;

	}

	@Override
	public void save(User user) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		String sql = "INSERT INTO USERS(NAME, EMAIL, ADDRESS, PASSWORD, NEWSLETTER, FRAMEWORK, SEX, NUMBER, COUNTRY, SKILL) "
				+ "VALUES ( :name, :email, :address, :password, :newsletter, :framework, :sex, :number, :country, :skill)";

		jdbcTemplate.update(sql);
		//namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(user), keyHolder);
		user.setId(keyHolder.getKey().intValue());
		
	}

	@Override
	public void update(User user) {

		String sql = "UPDATE USERS SET NAME=:name, EMAIL=:email, ADDRESS=:address, " + "PASSWORD=:password, NEWSLETTER=:newsletter, FRAMEWORK=:framework, "
				+ "SEX=:sex, NUMBER=:number, COUNTRY=:country, SKILL=:skill WHERE id=:id";

		jdbcTemplate.update(sql);
		//namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(user));

	}

	@Override
	public void delete(Integer id) {

		String sql = "DELETE FROM USERS WHERE id= :id";
		jdbcTemplate.update(sql, id);
		//namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource("id", id));

	}

	private SqlParameterSource getSqlParameterByModel(User user) {

		// Unable to handle List<String> or Array
		// BeanPropertySqlParameterSource

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("id", user.getId());
		paramSource.addValue("name", user.getName());
		paramSource.addValue("email", user.getEmail());
		paramSource.addValue("address", user.getAddress());
		paramSource.addValue("password", user.getPassword());
		paramSource.addValue("newsletter", user.isNewsletter());

		// join String
		paramSource.addValue("framework", convertListToDelimitedString(user.getFramework()));
		paramSource.addValue("sex", user.getSex());
		paramSource.addValue("number", user.getNumber());
		paramSource.addValue("country", user.getCountry());

		return paramSource;
	}

	private static final class UserMapper implements RowMapper<User> {

		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			user.setFramework(convertDelimitedStringToList(rs.getString("framework")));
			user.setAddress(rs.getString("address"));
			user.setCountry(rs.getString("country"));
			user.setNewsletter(rs.getBoolean("newsletter"));
			user.setNumber(rs.getInt("number"));
			user.setPassword(rs.getString("password"));
			user.setSex(rs.getString("sex"));
			return user;
		}
	}

	private static List<String> convertDelimitedStringToList(String delimitedString) {

		List<String> result = new ArrayList<String>();

		if (!StringUtils.isEmpty(delimitedString)) {
			result = Arrays.asList(StringUtils.delimitedListToStringArray(delimitedString, ","));
		}
		return result;

	}

	private String convertListToDelimitedString(List<String> list) {

		String result = "";
		if (list != null) {
			result = StringUtils.arrayToCommaDelimitedString(list.toArray());
		}
		return result;

	}

}