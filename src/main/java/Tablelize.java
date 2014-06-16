
import Clustering.Document;
import Clustering.DocumentCollection;
import TeachingDocParser.Tokenizer;
import TermScoring.TFIDF.TFIDFCalculator;

import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Myung-ha Jang (mhjang@cs.umass.edu)
 * @since 0.10
 *
 * This class generates a HTML page of ranked terms within the document in a table form.
 */
public class Tablelize {
     static int lineIndex = 0;
     static DecimalFormat numberFormat = new DecimalFormat(("#.000"));
     static String[] colors = {"yellow", "Coral", "orange", "DarkSalmon", "DarkTurquoise", "GreenYellow", "lime", "teal", "Pink", "Salmon", "SlateBlue", "Skyblue", "RoyalBlue", "Violet", "Tomato"};

     /**
       *
       * @param args: -i [input filepath] -c [the number of columns] -k [top k terms to display] -o [output filename]
       * @throws java.io.IOException
       */
     public static void main(String[] args) throws IOException {
         String dir = "/Users/mhjang/Documents/workspace/TeachingTest/testdata";
         int columnNum = 5, k = 10;
         String outFileName = "output.html";

         /**
          *  process the arguments
          */
         for (int i = 0; i < args.length; i++) {
             if (args[i].equals("-i")) {
                 dir = args[++i];
             } else if (args[i].equals("-c")) {
                 columnNum = Integer.parseInt(args[++i]);
             } else if (args[i].equals("-o")) {
                 outFileName = args[++i];
             } else if (args[i].equals("-k")) {
                 k = Integer.parseInt(args[++i]);

             }
         }

             File file = new File(outFileName);
             BufferedWriter bw = new BufferedWriter(new FileWriter(file));

             TFIDFCalculator tfidf = new TFIDFCalculator(false);
             tfidf.calulateTFIDF(TFIDFCalculator.LOGTFIDF, dir, Tokenizer.UNIGRAM, false);
             DocumentCollection dc = tfidf.getDocumentCollection();

             bw.write("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script> \n");
             bw.write("<table border=\"0\" width=\"1051\"> \n");


             HashMap<String, Document> documentSet = dc.getDocumentSet();
             for (String docName : documentSet.keySet()) {
                 Document doc = documentSet.get(docName);
                 LinkedList<Map.Entry<String, Double>> topRankedTerms = doc.getTopTermsTFIDF(k);
                 generateHTMLTable(doc.getName(), topRankedTerms, colors[0], bw);
             }

             bw.write("<H2> High TF/IDF Term Tablization </H2>");
             bw.write("</table> \n");
             bw.write("<script>\n" +
                     "$(document).ready(function(){\n" +
                     "var idx = 0;" +
                     "var colour = ['yellow', 'Coral', 'orange', 'DarkSalmon', 'DarkTurquoise', 'GreenYellow', 'lime', 'teal', 'Pink', 'Salmon', 'SlateBlue', 'Skyblue', 'RoyalBlue', 'Violet', 'Tomato'];\n" +
                     "   $(\"td\").click(function() {\n" +
                     "        var rand = Math.floor(Math.random() * colour.length);\n" +
                     "        var classId = $(this).attr(\"class\");" +
                     "        $(\".\"+classId).css(\"background-color\", colour[rand]);\n" +
                     "        });\n" +
                     "});\n" +
                     "</script>");

             bw.close();




     }



        public static void generateHTMLTable(String tableName, LinkedList<Map.Entry<String, Double>> elements, String tableColor, BufferedWriter bw) throws IOException {
            if(lineIndex % 5 == 0) {
                bw.write("</tr>");
                bw.write("<tr>");
            }
            bw.write("<td>");
            bw.write("<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\" style=\"width:250px\" text-align: center;\">");
            bw.write("<tr> <td style=\"background-color: "+tableColor +" id=\""+ tableName + "\"><b>" + tableName + "</b></td></tr>");
            for(Map.Entry<String, Double> ele : elements) {
                String[] tokens = ele.getKey().split(" ");

                bw.write("<tr> <td class=\""+tokens[0]+"\">" + ele.getKey() + "("+ numberFormat.format(ele.getValue()) +") </td></tr>");
            }
            bw.write("</table>");
            bw.write("</td>");
            lineIndex++;
        }


}
