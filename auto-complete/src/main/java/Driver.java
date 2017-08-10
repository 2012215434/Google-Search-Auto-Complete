import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * Created by zhangsheng on 8/7/17.
 */
public class Driver {
    public static void main(String[] args)
            throws ClassNotFoundException, IOException, InterruptedException{
        Configuration conf1 = new Configuration();
        conf1.set("textinputformat.record.delimiter", ".");
        conf1.set("noGram", args[2]);

        Job job1 = Job.getInstance(conf1);
        job1.setJobName("NGram");
        job1.setJarByClass(Driver.class);

        job1.setMapperClass(NGramLibraryBuilder.NGramMapper.class);
        job1.setReducerClass(NGramLibraryBuilder.NGramReducer.class);

        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(IntWritable.class);

        job1.setInputFormatClass(TextInputFormat.class);
        job1.setOutputFormatClass(TextOutputFormat.class);

        TextInputFormat.setInputPaths(job1, new Path(args[0]));
        TextOutputFormat.setOutputPath(job1, new Path(args[1]));

        job1.waitForCompletion(true);


        //job2
        Configuration conf2 = new Configuration();
        conf2.set("threshold", args[3]);
        conf2.set("n", args[4]);

        DBConfiguration.configureDB(conf2, "com.mysql.jdbc.Driver",
                "jdbc:mysql://127.0.0.1:3306/autoComplete",
                "root",
                "");

        Job job2 = Job.getInstance(conf2);
        job2.setJobName("LanguageModel");
        job2.setJarByClass(Driver.class);

        job2.setMapperClass(LanguageModel.LanguageModelMapper.class);
        job2.setReducerClass(LanguageModel.LanguageModelReducer.class);

        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(Text.class);

        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(NullWritable.class);

        job2.setInputFormatClass(TextInputFormat.class);
        job2.setOutputFormatClass(DBOutputFormat.class);

        DBOutputFormat.setOutput(job2, "search",
                new String[] {"startingPhrase", "followingWord", "count"});

        TextInputFormat.setInputPaths(job2, new Path(args[1]));
        //TextOutputFormat.setOutputPath(job2, new Path(args[5]));

        job2.waitForCompletion(true);
    }
}
