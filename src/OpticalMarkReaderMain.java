import FileIO.PDFHelper;
import Filters.DisplayInfoFilter;
import Filters.FixedThresholdFilter;
import core.DImage;
import processing.core.PImage;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class OpticalMarkReaderMain {
    public static void main(String[] args) throws IOException {
        String pathToPdf = fileChooser();
        System.out.println("Loading pdf at " + pathToPdf);

        DImage img1 = new DImage(PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf",1));
        FixedThresholdFilter page1 = new FixedThresholdFilter(img1);
        int numAnswer = page1.getNumAnswers();
        ArrayList<String> keyAnswers = page1.calculateAnswerList();
        DImage currImg;
        ArrayList<String> currAnswers = new ArrayList<>();
        writeDataToFile("gradedPapersReport.csv", "Page #, # correct, q1, q2, q3, q4 ...\n\n");
        ArrayList<String> gradedAns = new ArrayList<>();
        for(int i = 1; i <= 6; i++) {
            currImg = new DImage(PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf",i));
            FixedThresholdFilter currPage = new FixedThresholdFilter(currImg);
            currAnswers = currPage.calculateAnswerList();
            writeDataToFile("gradedPapersReport.csv", ""+i);


            for (int j = 0; j < currAnswers.size(); j++) {
                String currAns = currAnswers.get(j);
                String currKeyAns = keyAnswers.get(j);
                if(currAns.equals(currKeyAns)){
                    gradedAns.add("right");
                }
                else{
                    gradedAns.add("wrong");
                }

            }
            writeDataToFile("gradedPapersReport.csv", ", "+getNumCorrectAns(gradedAns, i));
            for (int j = (i-1)*12; j < (i-1)*12+12; j++) {
                writeDataToFile("gradedPapersReport.csv", ", "+gradedAns.get(j));
            }

            writeDataToFile("gradedPapersReport.csv", "\n");


            String data = readFile("gradedPapersReport.csv");
            System.out.println("Our gradedPapers file contains: " + data);
        }
        int count;
        for (int i = 0; i < numAnswer; i++) {
            count = 0;
            for (int j = 0; j < 6; j++) {
                int currIndex = i+(12*j);
                if(gradedAns.get(currIndex).equals("right")){
                    count++;
                }
            }
            writeDataToFile("eachQuestionReport.csv", "Question "+ (i+1) + ": " + count + "/6\n");
        }




        /*
        Your code here to...
        (1).  Load the pdf
        (2).  Loop over its pages
        (3).  Create a DImage from each page and process its pixels
        (4).  Output 2 csv files
         */

    }
    private static int getNumCorrectAns(ArrayList<String> checkedAnswers, int start){
        int count = 0;
        for (int i = (start-1)*12; i < ((start-1)*12)+12; i++) {
            if(checkedAnswers.get(i).equals("right")){
                count++;
            }
        }
        return count;
    }
    private static String fileChooser() {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fc = new JFileChooser(userDir);
        int returnVal = fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return file.getAbsolutePath();
    }
    private static void RunTheFilter(int page) {
        System.out.println("Loading pdf....");
        PImage in = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf",page);
        DImage img = new DImage(in);       // you can make a DImage from a PImage

        System.out.println("Running filter on page " + page + "....");
        FixedThresholdFilter filter = new FixedThresholdFilter(img);
        filter.processImage(img);  // if you want, you can make a different method
        // that does the image processing an returns a DTO with
        // the information you want
    }

    public static void writeDataToFile(String filePath, String data) throws IOException {
        try (FileWriter f = new FileWriter(filePath, true);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter writer = new PrintWriter(b);) {


            writer.print(data);


        } catch (IOException error) {
            System.err.println("There was a problem writing to the file: " + filePath);
            error.printStackTrace();
        }
    }
    public static String readFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

}
