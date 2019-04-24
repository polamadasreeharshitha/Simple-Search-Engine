import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DocumentParser {

    //This variable hold terms of each document in an array.
    private List<String[]> termsDocsArray = new ArrayList<String[]>();
    private List<String> allTerms = new ArrayList<String>(); //to hold all terms
    private List<double[]> tfidfDocsVector = new ArrayList<double[]>();
    
    public void parseFiles(String filePath) throws FileNotFoundException, IOException {
        File[] allfiles = new File(filePath).listFiles();
        BufferedReader in = null;
        for (File f : allfiles) {
            if (f.getName().endsWith(".txt")) {
                in = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
                String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
                for (String term : tokenizedTerms) {
                    if (!allTerms.contains(term)) {  // to avoid duplicate entry
                        allTerms.add(term);
                    }
                }
                termsDocsArray.add(tokenizedTerms);
            }
        }

    }
    
    public void tfIdfCalculator() {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term frequency inverse document frequency        
        for (String[] docTermsArray : termsDocsArray) {
            double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            for (String terms : allTerms) {
            	TfIdf tfidfscore = new TfIdf();
                tf = tfidfscore.tfCalculator(docTermsArray, terms);
                idf = tfidfscore.idfCalculator(termsDocsArray, terms);
                tfidf = tf * idf;
                tfidfvectors[count] = tfidf;
                count++;
            }
            tfidfDocsVector.add(tfidfvectors);  //storing document vectors;            
        }
        
    }

    public void getCosineSimilarity() throws IOException {
    	Random rand = new Random();
    	FileWriter writer1 = new FileWriter("D:\\IR_Project\\CosineSimilarity\\score"+rand.nextInt()+".txt");
        PrintWriter writer_tf = new PrintWriter(writer1);
        for (int i = 0; i < tfidfDocsVector.size()-1; i++) {
            for (int j = tfidfDocsVector.size()-1; j < tfidfDocsVector.size(); j++) {
            	CosineSimilarity cos=new CosineSimilarity();
                writer_tf.println(cos.cosineSimilarity (
                                tfidfDocsVector.get(i), 
                                tfidfDocsVector.get(j)
                              ));
                
            }
        }
        writer_tf.close();
    }
}
