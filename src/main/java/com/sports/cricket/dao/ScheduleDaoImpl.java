package com.sports.cricket.dao;

import com.sports.cricket.model.*;
import com.sports.cricket.util.PredictionListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ScheduleDaoImpl implements ScheduleDao, Serializable {

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
        String sql = "SELECT * FROM SCHEDULE where isActive = TRUE";

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

        String sql = "INSERT INTO PREDICTIONS(memberId, matchNumber, homeTeam, awayTeam, firstName, selected, predictedTime, matchday)" +
                    "VALUES (?,?,?,?,?,?,?,?)";


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
            ps.setInt(8, prediction.getMatchDay());

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
    public Prediction getPrediction(Integer predictionId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("predictionId", predictionId);

        String sql = "SELECT * FROM PREDICTIONS where predictionId = ?";

        Prediction prediction = null;
        try {
            prediction = (Prediction) jdbcTemplate.queryForObject(sql, new Object[]{predictionId}, new BeanPropertyRowMapper(Prediction.class));
        } catch (EmptyResultDataAccessException e) {
        }

        return prediction;
    }


    @Override
    public List<Prediction> getPredictionByMatchDay(Integer matchDay) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("matchDay", matchDay);

        String sql = "SELECT * FROM PREDICTIONS where matchDay = ?";

        List<Prediction> predictionList = PredictionListMapper.predictionList(jdbcTemplate, sql, matchDay);
        return predictionList;
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
        return executeQuery(sql, memberID);
    }

    @Override
    public boolean deactivateMember(Integer id) {

        String sql = "UPDATE REGISTER SET isActive = 'N' where memberId = ?";
        return executeQuery(sql, id);
    }

    @Override
    public List<Prediction> getPredictionsByMatch(Integer matchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);

        String sql = "SELECT * FROM PREDICTIONS where matchNumber =?";

        List<Prediction> predictionList = PredictionListMapper.predictionList(jdbcTemplate, sql, matchId);
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

        String sql = "INSERT INTO RESULTS(matchNumber, homeTeam, awayTeam, startDate, winner, winningAmount, adminAmount, homeTeamCount, awayTeamCount, notPredictedCount, matchDay, drawTeamCount)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?," +result.getDrawTeamCount()+")";


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
            ps.setDouble(7, result.getAdminQuota());
            ps.setInt(8, (int)result.getHomeTeamCount());
            ps.setInt(9, (int)result.getAwayTeamCount());
            ps.setInt(10, (int)result.getNotPredictedCount());
            ps.setInt(11, result.getMatchDay());

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
    public List<Result> getResults() {
        String sql = "SELECT * FROM RESULTS";

        List<Result> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Result.class));

        return result;
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
    public List<Settlement> getSettlement() {
        String sql = "SELECT * FROM SETTLEMENT";

        List<Settlement> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Settlement.class));

        return result;
    }

    @Override
    public List<TrackSettlement> getSettlementsTrack() {
        String sql = "SELECT * FROM TRACK_SETTLEMENT";

        List<TrackSettlement> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TrackSettlement.class));

        return result;
    }

    @Override
    public Settlement getSettlement(Integer memberId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", memberId);

        String sql = "SELECT * FROM SETTLEMENT where memberId = ?";

        Settlement settlement = null;
        try {
            settlement = (Settlement) jdbcTemplate.queryForObject(sql, new Object[]{memberId}, new BeanPropertyRowMapper(Settlement.class));
        } catch (EmptyResultDataAccessException e) {
        }

        return settlement;
    }

    @Override
    public boolean updateSettlement(List<Settlement> settlementList) {
        if (!CollectionUtils.isEmpty(settlementList)) {
            for (Settlement settlement : settlementList) {
                String sql = "UPDATE SETTLEMENT SET net = ? where memberId = ?";

                Connection conn = null;

                try {
                    conn = dataSource.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setDouble(1, settlement.getNet());
                    ps.setInt(2, settlement.getMemberId());

                    ps.executeUpdate();
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
            }
        }

        return true;
    }

    @Override
    public boolean addSettlement(TrackSettlement trackSettlement) {
        boolean isSuccess = false;

        String sql = "INSERT INTO TRACK_SETTLEMENT(memberId, name, settledMemberId, settledName, settledAmount, settledTime)" +
                "VALUES (?,?,?,?,?,?)";


        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, trackSettlement.getMemberId());
            ps.setString(2, trackSettlement.getName());
            ps.setInt(3, trackSettlement.getSettledMemberId());
            ps.setString(4, trackSettlement.getSettledName());
            ps.setDouble(5, trackSettlement.getSettledAmount());
            ps.setString(6, trackSettlement.getSettledTime());

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

    @Override
    public List<Prediction> getAdminPrediction(Integer memberId, Integer matchDay) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("matchDay", matchDay);

        String sql = "SELECT * FROM PREDICTIONS where memberId =" + memberId + " and matchDay = " +matchDay;

        List<Prediction> predictionList = PredictionListMapper.adminPredictionList(jdbcTemplate, sql);
        return predictionList;
    }

    private LocalDateTime getTime(){
        return java.time.LocalDateTime.now();
    }

    public boolean executeQuery(String sql, int id){
        Connection connection = null;
        int totalRows;

        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            totalRows = statement.executeUpdate();

            statement.close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {}
            }
        }

        return (totalRows == 1 ? true : false);
    }
}
