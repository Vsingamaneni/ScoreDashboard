package com.sports.cricket.dao;

import com.sports.cricket.model.Prediction;
import com.sports.cricket.model.Result;
import com.sports.cricket.model.Schedule;
import com.sports.cricket.model.Standings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {

    DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Schedule> findAll() {
        String sql = "SELECT * FROM schedule where isActive = TRUE";

        List<Schedule> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Schedule.class));

        return result;
    }

    @Override
    public List<Schedule> scheduleList() {
        String sql = "SELECT * FROM SCHEDULE";

        List<Schedule> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Schedule.class));

        return result;
    }

    @Override
    public Schedule findById(Integer matchNumber) {

        Map<String, Object> params = new HashMap<>();
        params.put("matchNumber", matchNumber);

        String sql = "SELECT * FROM SCHEDULE where matchNumber = ?";

        Schedule result = null;
        try {
            result = (Schedule) jdbcTemplate.queryForObject(sql, new Object[]{matchNumber}, new BeanPropertyRowMapper(Schedule.class));
        } catch (EmptyResultDataAccessException e) {
        }

        return result;
    }

    @Override
    public List<Prediction> findPredictions(Integer memberId) {
        String sql = "SELECT * FROM PREDICTIONS where memberId = " + memberId;

        List<Prediction> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Prediction.class));

        return result;
    }

    @Override
    public boolean savePrediction(Prediction prediction) {

        boolean isSuccess = false;

        String sql = "INSERT INTO PREDICTIONS(memberId, matchNumber, homeTeam, awayTeam, firstName, selected, predictedTime)" +
                    "VALUES (?,?,?,?,?,?,?)";


        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, prediction.getMemberId());
            ps.setInt(2, prediction.getMatchNumber());
            ps.setString(3, prediction.getHomeTeam());
            ps.setString(4, prediction.getAwayTeam());
            ps.setString(5, prediction.getFirstName());
            ps.setString(6, prediction.getSelected());
            ps.setString(7, getTime().toString());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    isSuccess = true;
                } catch (SQLException e) {}
            }
        }

        return isSuccess;
    }

    @Override
    public boolean updatePrediction(Prediction prediction) {
        boolean isSuccess = false;

        String sql = "UPDATE PREDICTIONS SET selected = ? , predictedTime = ? where predictionId = ?";


        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, prediction.getSelected());
            ps.setString(2, getTime().toString());
            ps.setInt(3, prediction.getPredictionId());


            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    isSuccess = true;
                } catch (SQLException e) {}
            }
        }

        return isSuccess;
    }

    @Override
    public Prediction getPrediction(Integer predictionId, Integer matchId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("predictionId", predictionId);
        params.put("matchId", matchId);

        String sql = "SELECT * FROM PREDICTIONS where predictionId = ? and matchNumber =?";

        Prediction prediction = null;
        try {
            prediction = (Prediction) jdbcTemplate.queryForObject(sql, new Object[]{predictionId, matchId}, new BeanPropertyRowMapper(Prediction.class));
        } catch (EmptyResultDataAccessException e) {
        }

        return prediction;
    }

    @Override
    public boolean deletePrediction(Integer predictionId) {
        boolean isSuccess = false;

        String sql = "DELETE FROM PREDICTIONS where predictionId = ?";


        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, predictionId);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    isSuccess = true;
                } catch (SQLException e) {}
            }
        }

        return isSuccess;
    }

    @Override
    public boolean authorizeMember(Integer memberID) {

        String sql = "UPDATE REGISTER SET isActive = 'Y' , isAdminActivated = 'Y' where memberId = ?";


        Connection conn = null;
        int rows = 0;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, memberID);

            rows = ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }

        return (rows == 1 ? true : false);
    }

    @Override
    public List<Prediction> getPredictionsByMatch(Integer matchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);

        String sql = "SELECT * FROM PREDICTIONS where matchNumber =?";

        List<Prediction> predictionList = jdbcTemplate.query(sql, new Object[]{matchId}, rs -> {

            List<Prediction> predictionList1 = new ArrayList<>();
            while(rs.next()){
                Prediction prediction = new Prediction();

                prediction.setPredictionId(rs.getInt("predictionId"));
                prediction.setMemberId(rs.getInt("memberId"));
                prediction.setMatchNumber(rs.getInt("matchNumber"));
                prediction.setHomeTeam(rs.getString("homeTeam"));
                prediction.setAwayTeam(rs.getString("awayTeam"));
                prediction.setFirstName(rs.getString("firstName"));
                prediction.setSelected(rs.getString("selected"));
                prediction.setPredictedTime(rs.getString("predictedTime"));

                predictionList1.add(prediction);
            }
            return predictionList1;
        });

        return predictionList;
    }

    @Override
    public boolean updateMatchResult(Schedule schedule) {
        String sql = "UPDATE SCHEDULE SET winner = ?, isActive = ? where matchNumber = ?";


        Connection conn = null;
        int rows;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, schedule.getWinner());
            ps.setBoolean(2, false);
            ps.setInt(3, schedule.getMatchNumber());

            rows = ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }

        return (rows == 1 ? true : false);
    }

    @Override
    public Integer totalMatches(Integer matchDay) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("matchDay", matchDay);

        String sql = "SELECT count(*) as matchTotal FROM SCHEDULE where matchDay = ?  and isActive = true";

        Integer matchDays = jdbcTemplate.query(sql, new Object[]{matchDay}, rs -> {

            Integer totalMatches = 0;
            while(rs.next()){
                totalMatches = rs.getInt("matchTotal");
            }
            return totalMatches;
        });

        return matchDays;
    }

    @Override
    public boolean updateMatchDay(Integer matchDay) {
        String sql = "UPDATE SCHEDULE SET  isActive = true where matchDay = " + matchDay;

        Connection conn = null;
        int rows;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            rows = ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        return (rows > 0 ? true : false);
    }

    @Override
    public boolean addResult(Result result) {

        boolean isSuccess = false;

        String sql = "INSERT INTO RESULTS(matchNumber, homeTeam, awayTeam, startDate, winner, winningAmount, homeTeamCount, awayTeamCount, notPredictedCount, matchDay, drawTeamCount)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?," +result.getDrawTeamCount()+")";


        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, result.getMatchNumber());
            ps.setString(2, result.getHomeTeam());
            ps.setString(3, result.getAwayTeam());
            ps.setString(4, result.getStartDate());
            ps.setString(5, result.getWinner());
            ps.setDouble(6, result.getWinningAmount());
            ps.setInt(7, result.getHomeTeamCount());
            ps.setInt(8, result.getAwayTeamCount());
            ps.setInt(9, result.getNotPredictedCount());
            ps.setInt(10, result.getMatchDay());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    isSuccess = true;
                } catch (SQLException e) {}
            }
        }

        return isSuccess;
    }

    @Override
    public boolean insertPredictions(List<Standings> standingsList) {

        String sql = "INSERT INTO STANDINGS (memberId, matchNumber, homeTeam, awayTeam, matchDate, predictedDate, selected, winner, wonAmount, lostAmount) " +
                "           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Standings standings = standingsList.get(i);
                ps.setInt(1, standings.getMemberId());
                ps.setInt(2, standings.getMatchNumber());
                ps.setString(3, standings.getHomeTeam());
                ps.setString(4, standings.getAwayTeam());
                ps.setString(5, standings.getMatchDate());
                ps.setString(6, standings.getPredictedDate());
                ps.setString(7, standings.getSelected());
                ps.setString(8, standings.getWinner());
                ps.setDouble(9, standings.getWonAmount());
                ps.setDouble(10, standings.getLostAmount());
            }

            @Override
            public int getBatchSize() {
                return standingsList.size();
            }
        });

        return true;
    }

    @Override
    public List<Standings> getLeaderBoard() {
        String sql = "SELECT * FROM STANDINGS";

        List<Standings> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Standings.class));

        return result;
    }

    @Override
    public List<Schedule> getScheduleByMatchDay(Integer matchDay) {
        Map<String, Object> params = new HashMap<>();
        params.put("matchNumber", matchDay);

        String sql = "SELECT * FROM SCHEDULE where matchDay = ?";

        List<Schedule> schedules = jdbcTemplate.query(sql, new Object[]{matchDay},  rs -> {

                List<Schedule> scheduleList = new ArrayList<>();
                while(rs.next()){
                    Schedule schedule = new Schedule();


                    schedule.setMatchNumber(rs.getInt("matchNumber"));
                    schedule.setHomeTeam(rs.getString("homeTeam"));
                    schedule.setAwayTeam(rs.getString("awayTeam"));
                    schedule.setIsactive(rs.getBoolean("isActive"));
                    schedule.setMatchDay(rs.getInt("matchDay"));
                    schedule.setMatchFee(rs.getInt("matchFee"));
                    schedule.setStartDate(rs.getString("startDate"));

                    scheduleList.add(schedule);
                }
                return scheduleList;
            });

        return schedules;
    }

    private SqlParameterSource getSqlParameterByModel(Prediction prediction) {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("memberId", prediction.getMemberId());
        paramSource.addValue("matchNumber", prediction.getMatchNumber());
        paramSource.addValue("homeTeam", prediction.getHomeTeam());
        paramSource.addValue("awayTeam", prediction.getAwayTeam());
        paramSource.addValue("selected", prediction.getSelected());
        paramSource.addValue("predictedTime", getTime().toString());

        return paramSource;
    }

    private LocalDateTime getTime(){
        return java.time.LocalDateTime.now();
    }
}
