import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Greenest {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
                  
         float minPercent = 75;  
         StringTokenizer s = new StringTokenizer(value.toString());
         s.nextToken();
         s.nextToken();
         String percent = s.nextToken();
         float percentage1 = Float.parseFloat(percent);
      
         word.set("Green>75%");
         if(percentage1>=minPercent)
         	context.write(word,one);
      }
    }
  

  public static class CheckMinValue
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
     int sum = 0;
     
     for (IntWritable val : values) {
        sum += val.get();
      }
      
      result.set(sum);
      context.write(key,result);  
    }
  }

public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "greenest");
    job.setJarByClass(Greenest.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(CheckMinValue.class);
    job.setReducerClass(CheckMinValue.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
