// package SalesCountry;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.io.LongWritable;

public class SalesCountryDriver {

    public static class SalesMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
        //private final static IntWritable one = new IntWritable(1);
        IntWritable numero;
        Text txttotal = new Text("cteSumaTotal");
        public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            String valueString = value.toString();
            String[] SingleCountryData = valueString.split(",");
            if(!"Price".equals(SingleCountryData[2])){
                numero = new IntWritable(Integer.parseInt(SingleCountryData[2]));
            }else{
                numero = new IntWritable(0);
            }
            output.collect(txttotal, numero);
            //output.collect(new Text(SingleCountryData[1] + SingleCountryData[3]), numero);
        }
    }

    public static class SalesCountryReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text t_key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            Text key = t_key;
            //int frequencyForCountry = 0;
            int mayor_tipo = Integer.MIN_VALUE;
            while (values.hasNext()) {            
                IntWritable value = (IntWritable) values.next();
                if(value.get() > mayor_tipo){
                    mayor_tipo = value.get();                        
                }
            }
            output.collect(key, new IntWritable(mayor_tipo));
        }
    }
    

    public static void main(String[] args) {
        JobClient my_client = new JobClient();
        // Create a configuration object for the job
        JobConf job_conf = new JobConf(SalesCountryDriver.class);
        // Set a name of the Job
        job_conf.setJobName("SalePerCountry");
        // Specify data type of output key and value
        job_conf.setOutputKeyClass(Text.class);
        job_conf.setOutputValueClass(IntWritable.class);
        // Specify names of Mapper and Reducer Class
        job_conf.setMapperClass(SalesMapper.class);
        job_conf.setReducerClass(SalesCountryReducer.class);
        // Specify formats of the data type of Input and output
        job_conf.setInputFormat(TextInputFormat.class);
        job_conf.setOutputFormat(TextOutputFormat.class);
        // Set input and output directories using command line arguments, 
        //arg[0] = name of input directory on HDFS, and arg[1] =  name of output directory to be created to store the output file.
        FileInputFormat.setInputPaths(job_conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(job_conf, new Path(args[1]));
        my_client.setConf(job_conf);
        try {
            // Run the job 
            JobClient.runJob(job_conf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
