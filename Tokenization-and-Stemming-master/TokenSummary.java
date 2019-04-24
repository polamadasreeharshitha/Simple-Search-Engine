import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TokenSummary {

    // Private variables
    private int tokenCount;
    private int onlyOccuranceCount;
    private int documentCount;
    private HashMap<String, Integer> tokenMap;
    private long timeTake;
	private PrintWriter writer_query;

    // Constructor
    public TokenSummary() {
        tokenMap = new HashMap<>();
    }

    //  Getters and Setters
    public int getTokenCount() {
        return tokenCount;
    }

    public int getOnlyOccuranceCount() {
        return onlyOccuranceCount;
    }

    public void setOnlyOccuranceCount(int onlyOccuranceCount) {
        this.onlyOccuranceCount = onlyOccuranceCount;
    }

    public HashMap<String, Integer> getTokenMap() {
        return tokenMap;
    }

    public int getDocumentCount() {
        return documentCount;
    }

    public void setTokenMap(HashMap<String, Integer> tokenMap) {
        this.tokenMap = tokenMap;
    }

    public void setDocumentCount(int documentCount) {
        this.documentCount = documentCount;
    }

    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    public long getTimeTake() {
        return timeTake;
    }

    public void setTimeTake(long timeTake) {
        this.timeTake = timeTake;
    }

    

    //Public Methods
    public double getAvgTokens() {
        return tokenCount / documentCount;
    }

    public double getAvgUniqueTokens() {
        return tokenMap.size() / documentCount;
    }

    public void incrementTokenCount() {
        this.tokenCount++;
    }

    public void incrementDocumentCount() {
        this.documentCount++;
    }

    public void addToDictionary(String word) {
        tokenCount++;
        if (tokenMap.containsKey(word)) {
            int count = tokenMap.get(word);
            if (count == 1)
                onlyOccuranceCount--;
            tokenMap.put(word, count + 1);
        } else {
            onlyOccuranceCount++;
            tokenMap.put(word, 1);
        }
    }

    //Function for printStatistics
    public void printStatistics(String s) throws IOException {
        Map<String, Integer> sortedMapValue = Utility.sortMap(tokenMap);
        if(s.equalsIgnoreCase("query")) {
        	File file1=new File("D:\\IR_Project\\QueryStemmed\\text.txt");
			file1.createNewFile();
	        FileWriter writer = new FileWriter(file1);
	        writer_query = new PrintWriter(writer);
	        for (Map.Entry<String, Integer> entry : sortedMapValue.entrySet()) {
	            writer_query.println(entry.getKey());
	        }
	        writer_query.close();
        }else {
        	System.out.println("-------------Stemmed Words-----------------");
        	System.out.println("Number of tokens in the Document: " + tokenCount);
	        Random rand = new Random();
	        File file1=new File("D:\\IR_Project\\Token-Stem\\text"+rand.nextInt()+".txt");
			file1.createNewFile();
	        FileWriter writer = new FileWriter(file1);
	        writer_query = new PrintWriter(writer);
	        
	        for (Map.Entry<String, Integer> entry : sortedMapValue.entrySet()) {
	            System.out.println(entry.getKey() + "\t" + entry.getValue());
	            writer_query.println(entry.getKey());
	        }
	        writer_query.close();
        }
    }

}
