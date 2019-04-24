import java.util.*;
import java.util.regex.Pattern;
import java.io.*;
public class StopWords
{
    // return true if word is in stopWords
    public static Boolean isStopWord(String word, String[] stopWords)
    {
    	boolean found=false;
		for(int i=0;i<=stopWords.length-1;i++) {
			if(word.equalsIgnoreCase(stopWords[i])) {
				found=true;
			}
		}  
	return found;
    }
    //Constructor
	public StopWords(File listFiles) throws IOException
    {
		String FileName="D:\\IR_Project\\Stopwords";
		String[] stopWords = readStopWords(FileName);
		File inputFiles=listFiles;
		removeStopWords(inputFiles, stopWords);
    }
    // return an array of stop words by reading the stop words from the file
    public static String[] readStopWords(String stopWordsFilename) throws IOException 
    {
	String[] stopWords = null;
    BufferedReader bufferedReader = null;
	try
	    {
		File[] lstFiles = Utility.getFiles(stopWordsFilename);
		
		for (File file : lstFiles) {	
            if (file.isFile()) {
            	
                bufferedReader = new BufferedReader(new FileReader(file));
                List<String>lines=new ArrayList<String>();
                String line;
				while ((line = bufferedReader.readLine()) != null) {
					lines.add(line);
					
                }
				bufferedReader.close();
				stopWords=lines.toArray(new String[] {});
			}
		}
	    }
	catch (FileNotFoundException e)
	    {
		System.err.println(e.getMessage());
		System.exit(-1);
	    }
	return stopWords;
    }
    // check if it is a stop word
    // if it is not a stop word store it in a file
    public static void removeStopWords(File textFilename, String[] stopWords) throws IOException
    {
	String word;
	try
	    {
		int count=0;
		File lstFiles = textFilename;
            if (lstFiles.isFile()) {
            	count++;
            	Scanner textFile = new Scanner(new FileReader(lstFiles));
        		textFile.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));
        		File file1=new File("D:\\IR_Project\\Results\\stopwords"+count+".txt");
        		file1.createNewFile();
        		FileWriter writer = new FileWriter("D:\\IR_Project\\Results\\stopwords"+count+".txt");
        		PrintWriter outFile = new PrintWriter(writer);
        		//System.out.println("\nRemoving:");  	
	            	while (textFile.hasNext())
	    		    {
	    			word = textFile.next();
	    			if (isStopWord(word, stopWords))
	    			    System.out.print(" ");
	    			else
	    			    outFile.print(word + " ");
	    		    }
	    		outFile.println();
	    		textFile.close();
	    		outFile.close();
            	
            }
	}
	catch (FileNotFoundException e)
	    {
		System.err.println(e.getMessage());
		System.exit(-1);
	    }
    }
}