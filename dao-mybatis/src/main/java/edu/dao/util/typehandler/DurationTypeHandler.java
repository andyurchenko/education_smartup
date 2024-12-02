package edu.dao.util.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import java.sql.*;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationTypeHandler implements TypeHandler<Duration> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Duration parameter, JdbcType jdbcType) throws SQLException {
        ps.setTime(i, new Time(parameter.toMillis()));
    }

    @Override
    public Duration getResult(ResultSet rs, String columnName) throws SQLException {
        return toDuration(rs.getString(columnName));
    }

    @Override
    public Duration getResult(ResultSet rs, int columnIndex) throws SQLException {
        return toDuration(rs.getString(columnIndex));
    }

    @Override
    public Duration getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toDuration(cs.getString(columnIndex));
    }

    private Duration toDuration(String str) {
        Duration duration = null;
        Pattern pattern = Pattern.compile("(?<hours>\\d{2}):(?<minutes>\\d{2}):(?<seconds>\\d{2})");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            int hours = Integer.parseInt(matcher.group("hours"));
            int minutes = Integer.parseInt(matcher.group("minutes"));
            duration = Duration.ofHours(hours);
            duration = duration.plusMinutes(minutes);
        }

        return duration;
    }
}
