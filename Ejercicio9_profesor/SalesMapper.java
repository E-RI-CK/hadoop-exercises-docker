package SalesCountry;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class SalesMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);

    public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
        if (key.get() == 0) {
            return;
        }
        String valueString = value.toString();
        String[] singleRowData = valueString.split(",");
        output.collect(new Text(singleRowData[7] + "," + singleRowData[5] + "," + singleRowData[3]), one);
    }
}

class SalesMapper2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

    public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {

        String[] rowData = value.toString().split("\t");
        String[] rowValue = rowData[0].split(",");
        int valueInt = Integer.parseInt(rowData[1]);
        output.collect(new Text(rowValue[0] + "," + rowValue[1]), new Text(rowValue[2] + "," + valueInt));
    }
}
