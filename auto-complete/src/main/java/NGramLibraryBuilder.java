/**
 * Created by zhangsheng on 8/6/17.
 */

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class NGramLibraryBuilder {
    public static class NGramMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        int noGram;

        @Override
        public void setup(Context context) {
            Configuration conf = context.getConfiguration();
            noGram = conf.getInt("noGram", 5);
        }

        @Override
        public void map(LongWritable keyIn, Text valueIn, Context context)
                throws IOException, InterruptedException {
            String line = valueIn.toString();

            line = line.trim().toLowerCase();
            //replace all non alphabet Character to space
            line = line.replaceAll("[^a-z]+"," ");
            //split by ' ', '\t', '\n'
            String[] words = line.split("\\s+");

            //ignore one gram
            if (words.length < 2) {
                return;
            }

            for (int i = 0; i < words.length - 1; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(words[i]);
                for (int j = 1; i + j < words.length && j < noGram; j++) {
                    sb.append(" ");
                    sb.append(words[i + j]);
                    context.write(new Text(sb.toString()), new IntWritable(1));
                }
            }
        }
    }

    public static class NGramReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable v : values) {
                sum += v.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }
}
