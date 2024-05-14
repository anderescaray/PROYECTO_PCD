/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LALIGA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.nio.charset.StandardCharsets;
import java.security.PrivilegedExceptionAction;
import java.util.Scanner;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.security.UserGroupInformation;

/**
 *
 * @author alumno
 */
public class HadoopMapReduce {

    private static class MapClassEquipo extends Mapper<LongWritable, Text, Text, Text> {

        @Override
        protected void map(LongWritable key, Text value, Mapper.Context context) {
            try {
                String[] str = value.toString().split(",", -1);
                String equipo = str[0];
               
                if (!("Team".equals(equipo))) {
                    context.write(new Text(equipo), new Text(value));
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("Exception" + e.getMessage());
                e.printStackTrace(System.err);
            }
        }

    }

    private static class ReduceClassDelanteros extends Reducer<Text, Text, Text, Text> {

        private long max = -1;
        private String nombre = null;

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            try {
                max = -1;
                for (Text val : values) {
                    String[] str = val.toString().split(",", -1);
                    //CALCULAMOS PUNTUACION DE DELANTEROS, Ponderando GOLES, ASISTENCIAS, Y REGATES
                    long puntuacion = Long.parseLong(str[16]) + ((Long.parseLong(str[40])) / 2) + ((Long.parseLong(str[41])) / 10);
                    if (puntuacion > max) {
                        max = puntuacion;
                        nombre = str[3];
                    }

                }
                context.write(new Text(key), new Text(nombre + "\t" + max));

            } catch (IOException | InterruptedException e) {
                System.err.println("Exception" + e.getMessage());
                e.printStackTrace(System.err);
            }
        }

    }

    private static class ReduceClassMedios extends Reducer<Text, Text, Text, Text> {

        private long max = -1;
        private String nombre = null;

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            try {
                max = -1;
                for (Text val : values) {
                    String[] str = val.toString().split(",", -1);
                    //CALCULAMOS PUNTUACION DE MEDIOS, Ponderando ASISTENCIAS, GOLES (menor importancia), RECUPERACIONES, y PASES LARGOS 
                    long puntuacion = Long.parseLong(str[40]) + ((Long.parseLong(str[16])) / 2) + ((Long.parseLong(str[59])) / 10) + ((Long.parseLong(str[22])) / 10);
                    if (puntuacion > max) {
                        max = puntuacion;
                        nombre = str[3];
                    }

                }
                context.write(new Text(key), new Text(nombre + "\t" + max));

            } catch (IOException | InterruptedException e) {
                System.err.println("Exception" + e.getMessage());
                e.printStackTrace(System.err);
            }
        }

    }

    private static class ReduceClassDefensas extends Reducer<Text, Text, Text, Text> {

        private long max = -1;
        private String nombre = null;

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            try {
                max = -1;
                for (Text val : values) {
                    String[] str = val.toString().split(",", -1);
                    //CALCULAMOS PUNTUACION DE DEFENSAS, Ponderando DUELOS GANADOS, DUELOS PERDIDOS, TARJETAS ROJAS e INTERCEPCIONES 
                    long puntuacion = ((Long.parseLong(str[24])) / 10) - ((Long.parseLong(str[25])) / 10) - (Long.parseLong(str[14])) + ((Long.parseLong(str[23])) / 2);
                    if (puntuacion > max) {
                        max = puntuacion;
                        nombre = str[3];
                    }

                }
                context.write(new Text(key), new Text(nombre + "\t" + max));

            } catch (IOException | InterruptedException e) {
                System.err.println("Exception" + e.getMessage());
                e.printStackTrace(System.err);
            }
        }

    }

    private static class ReduceClassPorteros extends Reducer<Text, Text, Text, Text> {

        private long max = -1;
        private String nombre = null;

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            try {
                max = -1;
                for (Text val : values) {
                    String[] str = val.toString().split(",", -1);
                    //CALCULAMOS PUNTUACION DE PORTEROS, Ponderando GOLES EN CONTRA y PARTIDOS JUGADOS
                    long puntuacion = ((Long.parseLong(str[5]))*2) - (Long.parseLong(str[19]));
                    if (puntuacion > max) {
                        max = puntuacion;
                        nombre = str[3];
                    }

                }
                context.write(new Text(key), new Text(nombre + "\t" + max));

            } catch (IOException | InterruptedException e) {
                System.err.println("Exception" + e.getMessage());
                e.printStackTrace(System.err);
            }
        }

    }

    private static class PartitionerClassPosicion extends Partitioner<Text, Text> {

        @Override
        public int getPartition(Text key, Text value, int i) {
            String[] str = value.toString().split(",", -1);
            String pos = str[1];
            if (pos.equals("Goalkeeper")) {
                return 0;
            } else if (pos.equals("Defender")) {
                return 1;

            } else if (pos.equals("Midfielder")) {
                return 2;
            } else {
                return 3;
            }

        }
    }

    public static void writeFileToHDFS(String fileName, String pathToHDFS) throws IOException, URISyntaxException, InterruptedException {
        //Setting up the details of the configuration
        Configuration configuration = new Configuration();//192.168.10.1
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.10.1:9000"), configuration, "a_83013");

        Path hdfsWritePath = new Path(pathToHDFS + (new File(fileName).getName()));
        FSDataOutputStream fsDataOutputStream = fileSystem.create(hdfsWritePath, true);
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fsDataOutputStream, StandardCharsets.UTF_8));
        String linea;
        while ((linea = br.readLine()) != null) {
            bufferedWriter.write(linea);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        br.close();
        fileSystem.close();
    }
        public static void readFileFromHDFS(String hdfsFilePath,FileSystem fs  ) throws IOException{
       
        Path hdfsReadPath = new Path(hdfsFilePath);
        FSDataInputStream fsDataInputStrean = fs.open(hdfsReadPath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fsDataInputStrean));
        String linea;
        int contador = 1;
        StringBuilder sb = new StringBuilder();
        while((linea=br.readLine())!= null){
            System.out.println("Linea"+(contador++)+" : "+linea);
        }
        br.close();
        fs.close();
        
        
        
    }

    public static void main(String[] args) {
        try {
            writeFileToHDFS("./resources/laliga_player_stats_english.csv", "/PCD2024/a_83013/laLigaHadoop/");
        } catch (Exception e) {
            System.err.println("Exception" + e.getMessage());
            e.printStackTrace(System.err);

        }
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("a_83013");
        try {
            ugi.doAs(new PrivilegedExceptionAction<Void>() {
                public Void run() throws Exception {
                    Configuration conf = new Configuration();
                    FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.10.1:9000"), conf,"a_83013");
                    conf.set("fs.defaultFS", "hdfs://192.168.10.1:9000");
                    Job job = Job.getInstance(conf, "topsal");
                    job.setJarByClass(HadoopMapReduce.class);
                    job.setMapperClass(MapClassEquipo.class);
                    job.setMapOutputKeyClass(Text.class);
                    job.setMapOutputValueClass(Text.class);

                    job.setPartitionerClass(PartitionerClassPosicion.class);

                    Scanner teclado = new Scanner(System.in);
                    System.out.println("ESCOJA POSICION A COMPARAR: ");
                    System.out.println("1: Porteros");
                    System.out.println("2: Defensas");
                    System.out.println("3: Mediocentros");
                    System.out.println("4: Delanteros");

                    String eleccion = teclado.nextLine();
                    String fileName ="/PCD2024/a_83013/mapreduce_particionLaLiga/";
                    if (eleccion.equals("1")) {
                        job.setReducerClass(ReduceClassPorteros.class);
                        fileName +="part-r-00000";
                    } else if (eleccion.equals("2")) {
                        job.setReducerClass(ReduceClassDefensas.class);
                        fileName +="part-r-00001";
                    } else if (eleccion.equals("3")) {
                        job.setReducerClass(ReduceClassMedios.class);
                        fileName +="part-r-00002";
                    } else {
                        job.setReducerClass(ReduceClassDelanteros.class);
                        fileName +="part-r-00003";
                    }

                    job.setNumReduceTasks(4);
                    job.setInputFormatClass(TextInputFormat.class);
                    job.setOutputFormatClass(TextOutputFormat.class);
                    job.setOutputKeyClass(Text.class);
                    job.setOutputValueClass(Text.class);

                    FileInputFormat.addInputPath(job, new Path("/PCD2024/a_83013/laLigaHadoop/"));
                    FileOutputFormat.setOutputPath(job, new Path("/PCD2024/a_83013/mapreduce_particionLaLiga"));

                    boolean finalizado = job.waitForCompletion(true);
                    System.out.println("Finalizado: " + finalizado);
                    System.out.println("RESULTADO: ");
                    readFileFromHDFS(fileName, fileSystem);
                    return null;
                }
            });
            
            
        } catch (Exception e) {
            System.err.println("Exception" + e.getMessage());
            e.printStackTrace(System.err);
        }

    }
}
