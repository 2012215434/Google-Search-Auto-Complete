package webDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhangsheng on 8/9/17.
 */
@Service
public class FollowingWordRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<FollowingWord> searchCompletion(String startPhrase) {

        List<FollowingWord> result = jdbcTemplate.query(
                "select  startingPhrase, followingWord, count from search where startingPhrase like ? order by count desc limit 5",
                new Object[]{startPhrase+"%"},
                new RowMapper<FollowingWord>() {
                    public  FollowingWord mapRow(ResultSet rs, int rowNum) throws SQLException{
                        return new FollowingWord(rs.getString(1), rs.getString(2),rs.getInt(3));
                    }
                });
        return result;
    }
}
