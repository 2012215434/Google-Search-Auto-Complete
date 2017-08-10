import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhangsheng on 8/8/17.
 */
public class DBOutputWritable implements Writable, DBWritable{
    private String startingPhrase;
    private String followingWord;
    private int count;

    public DBOutputWritable(String startingPhrase, String followingWord, int count) {
        this.startingPhrase = startingPhrase;
        this.followingWord = followingWord;
        this.count = count;
    }


    public void write(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, startingPhrase);
        preparedStatement.setString(2,followingWord);
        preparedStatement.setInt(3, count);
    }


    public void readFields(ResultSet resultSet) throws SQLException {
        startingPhrase = resultSet.getString(1);
        followingWord = resultSet.getString(2);
        count = resultSet.getInt(3);
    }

    public void write(DataOutput dataOutput) throws IOException {

    }

    public void readFields(DataInput dataInput) throws IOException {

    }
}
