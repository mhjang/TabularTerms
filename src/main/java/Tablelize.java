
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
     static String[] colors = {"#ffeda3", "#428bca", "#5cb85c", "#5bc0de", "#f0ad4e", "#d9534f"};

     /**
       *
       * @param args: -i [input filepath] -c [the number of columns] -k [top k terms to display] -o [output filename]
       * @throws java.io.IOException
       */
     public static void main(String[] args) throws IOException {
         String dir = "/Users/mhjang/Documents/workspace/TeachingTest/testdata";
         int columnNum = 5, k = 10;
         String outFileName = "./output/output.html";

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

         /***
          * add heading
          */
            bw.write("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "  <head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "    <title> Term Tablize Ver 1.0 </title>\n" +
                    "\n" +
                    "    <!-- Bootstrap -->\n" +
                    "    <link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                    "  </head>\n");

             bw.write("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script> \n");
         bw.write("<center><H2> High TF/IDF Term Tablization </H2> <hr> <br><br>");
        // bw.write("<span class=\"label label-default\">View All</span>");

         bw.write("<table border=\"0\" width=\"1051\"> \n");


             HashMap<String, Document> documentSet = dc.getDocumentSet();
             for (String docName : documentSet.keySet()) {
                 Document doc = documentSet.get(docName);
                 LinkedList<Map.Entry<String, Double>> topRankedTerms = doc.getTopTermsTFIDF(k);
                 generateHTMLTable(doc.getName(), topRankedTerms, colors[0], bw);
             }

             bw.write("</table> \n");
             bw.write("<script>\n" +
                     "$(document).ready(function(){\n" +
                     "var idx = 0;" +
                     "var colour = [\"#ffeda3\", \"#428bca\", \"#5cb85c\", \"#5bc0de\", \"#f0ad4e\", \"#d9534f\"];\n" +
                     "   $(\"td\").click(function() {\n" +
                     "        var rand = Math.floor(Math.random() * colour.length);\n" +
                     "        var classId = $(this).attr(\"class\");" +
                     "        if ($(\".\"+classId).css(\"background-color\") == 'rgba(0, 0, 0, 0)') \n" +
                     "        \t$(\".\"+classId).css(\"background-color\", colour[rand]);\n" +
                     "        else $(\".\"+classId).css(\"background-color\", 'rgba(0, 0, 0, 0)');\n" +
                     "        });\n" +
                     "}); \n </script>");
             bw.close();




     }



        public static void generateHTMLTable(String tableName, LinkedList<Map.Entry<String, Double>> elements, String tableColor, BufferedWriter bw) throws IOException {
            if(lineIndex % 5 == 0) {
                if(lineIndex != 0) bw.write("</tr> \n");
                bw.write("<tr> \n");
            }
            bw.write("<td>\n");
            bw.write("<table class = \"table table-condensed\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\" style=\"width:250px\" text-align: center;\"> \n");
            bw.write("<tr> <td style=\"background-color: "+tableColor +"; id=\""+ tableName + "\"><b>" + tableName + "</b></td></tr> \n");
            for(Map.Entry<String, Double> ele : elements) {
                String[] tokens = ele.getKey().split(" ");

                bw.write("<tr> <td class=\""+tokens[0]+"\">" + ele.getKey() + "("+ numberFormat.format(ele.getValue()) +") </td></tr> \n");
            }
            bw.write("</table> \n");
            bw.write("</td> \n");
            lineIndex++;
        }


}
