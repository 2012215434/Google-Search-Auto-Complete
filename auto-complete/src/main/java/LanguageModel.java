import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

/**
 * Created by zhangsheng on 8/7/17.
 */
public class LanguageModel {
    public static class LanguageModelMapper extends Mapper<LongWritable, Text, Text, Text> {
        int threshold;

        @Override
        public void setup(Context context) {
            Configuration conf = context.getConfiguration();
            threshold = conf.getInt("threshold", 5);
        }

        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException{
            if (value == null || value.toString().trim().length() == 0) {
                return;
            }

            String line = value.toString().trim();
            String[] wordsAndCount = line.split("\t");
            String[] words = wordsAndCount[0].split("\\s+");

            int count = Integer.valueOf(wordsAndCount[wordsAndCount.length - 1]);

            if (wordsAndCount.length < 2 || count <= threshold) {
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < words.length - 1; i++) {
                sb.append(words[i]).append(" ");
            }

            String outputKey = sb.toString().trim();
            String outputValue = words[words.length - 1];
            if (outputKey != null && outputKey.length() >= 1) {
                context.write(new Text(outputKey), new Text(outputValue + "=" + count));
            }
        }
    }

    public static class LanguageModelReducer extends Reducer<Text, Text, DBOutputWritable, NullWritable> {
        int n;

        @Override
        public void setup(Context context) {
            Configuration conf = context.getConfiguration();
            n = conf.getInt("n", 5);
        }

        //get top k frequent next word for a key
        @Override
        public void reduce(Text keyIn, Iterable<Text> valueIn, Context context)
                throws IOException, InterruptedException{
            TreeMap<Integer, List<String>> map = new TreeMap<Integer, List<String>>(Collections.<Integer>reverseOrder());

            for (Text t : valueIn) {
                String s = t.toString().trim();
                String[] temp = s.split("=");
                String word = temp[0].trim();
                int count = Integer.valueOf(temp[1].trim());

                if (map.containsKey(count)) {
                    map.get(count).add(word);
                } else {
                    List<String> list = new ArrayList<String>();
                    list.add(word);
                    map.put(count, list);
                }
            }

            Iterator<Integer> itr = map.keySet().iterator();

            for (int i = 0; itr.hasNext() && i < n; i++) {
                int count = itr.next();
                List<String> words = map.get(count);
                for (String word : words) {
                    context.write(new DBOutputWritable(keyIn.toString(), word, count),
                            NullWritable.get());
                    i++;
                }
            }
        }
    }
}
