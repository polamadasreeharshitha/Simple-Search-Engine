import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;

public class ApplicationRunner {
	//Using Simple Analyzer
	private static Analyzer sAnalyzer = new SimpleAnalyzer(Version.LUCENE_47);
    private IndexWriter writer;
    private ArrayList<File> queue = new ArrayList<File>();
    
    //Main Method throwing IOException
    public static void main(String[] args) throws IOException {
    	
    	int count=0;
    	//Take Input of Indexed Files
    	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    	System.out.println("Enter the files path to be tokened:(D:\\IR_Project\\TeluguFiles)");
    	String inputpath = br.readLine();
    	
        if (inputpath == null)			//If input is empty
            System.out.println("Please provide valid path");
        else {
            processFile(inputpath);		//Calling processFile method to tokenize,stopword removal and stemming.
            String indexLocation = null;
            String s = "D:\\IR_Project\\FileInputs";
            ApplicationRunner indexer = null;
            try {
                indexLocation = s;
                indexer = new ApplicationRunner(s);
            } catch (Exception ex) {
                System.out.println("Cannot create index..." + ex.getMessage());
                System.exit(-1);
            }

            while (!s.equalsIgnoreCase("q")) {
                try {
                    System.out.println("Enter the path to add into the index (q=quit): (D:\\IR_Project\\Token-Stem)");
                    s = br.readLine();
                    if (s.equalsIgnoreCase("q")) {
                        break;
                    }
                    // Function call to create index
                    indexer.indexFileOrDirectory(s);
                } catch (Exception e) {
                    System.out.println("Error indexing " + s + " : "
                            + e.getMessage());
                }
            }
            indexer.closeIndex();
            // Fetch term frequencies and obtain query results
            getTermFrequencyPairs(indexLocation);                      // Get the frequencies for all the terms indexed.
            searchForQuery(indexLocation, s, br,count);                      // Search for queries.

        }
        
    }

    private static void processFile(String directoryPath) throws IOException {
        // list of files are taken
    	File[] lstFiles = Utility.getFiles(directoryPath);
        if (lstFiles.length == 0)		//If number of files in the directory is zero. 
            System.out.println("Error loading the directory");
        else {
        	 String s="token";
        	 for (File file : lstFiles) {
                 if (file.isFile()) {
		            System.out.println("Tokenization Analysis of Documents");
		            System.out.println("----------------------------------");
		            TokenSummary tokenSummary = new TokenSummary();
		            TokenExecutor tokenExecutor = new TokenExecutor(tokenSummary, file);
		            System.out.println("Stopword Removal");
		            System.out.println("-----------------------------------");
		            StopWords stopWord= new StopWords(file);	//Calling StopWord Function to remove stop words. 
		            String file1="D:\\IR_Project\\Results";
		            File[] StopwordsRemovedFiles = Utility.getFiles(file1);
		            for (File f : StopwordsRemovedFiles) {
		                if (f.isFile()) {
		                	TokenExecutor tokenExecutor1 = new TokenExecutor(tokenSummary, f);
		                }
		            }
		            System.out.println("Stemming Analysis of Documents");
		            System.out.println("------------------------------");
		            StemmingExecutor stemmingExecutor = new StemmingExecutor(tokenSummary); //Calling Stemming Function to stem the results obtained after stop word removal
		            stemmingExecutor.processStemming(s);  
                 }
        	 }
        }
    }

    // Function to fetch the term frequencies for the terms.
    public static void getTermFrequencyPairs(String indexLocation) throws IOException {
        Map<String, Integer> termfrequency = new HashMap<String, Integer>();
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
                indexLocation)));
        
        FileWriter writer1 = new FileWriter("D:\\IR_Project\\terf-frequency.csv");
        PrintWriter writer_tf = new PrintWriter(writer1);
        //int docnum = reader.numDocs();
        Fields fields1 = MultiFields.getFields(reader);
        //for (String field : fields1) {
            Terms terms1 = fields1.terms("contents");
            TermsEnum termsEnum = terms1.iterator(null);
            int noWords = 0;
            while (termsEnum.next() != null) {
                noWords++;
                int count = 0;
                DocsEnum docsEnum = termsEnum.docs(null, null);
                int docIdEnum;
                while ((docIdEnum = docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                    count += docsEnum.freq();
                }
                termfrequency.put(termsEnum.term().utf8ToString(), count);
            }
            System.out.println("Total Number of Words:" + noWords);
        //}

        // Write the terms and their frequencies in a file
        for (String key : termfrequency.keySet()) {
            writer_tf.print(key + ",");
            writer_tf.println(termfrequency.get(key));
        }
        writer_tf.close();

    }
    
    // Function to search the given queries
    public static void searchForQuery(String indexLocation, String s, BufferedReader br,int count) throws IOException {

        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
                indexLocation)));
        IndexSearcher searcher = new IndexSearcher(reader);
        BufferedReader bufferedReader = null;
        s = "";
        String query;    
        String str="query";

        while (!s.equalsIgnoreCase("q")) {
        	//Top 10 results are displayed
            TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
            count++;
            query="";
            FileWriter writer2 = new FileWriter("D:\\IR_Project\\Query-Results\\query"+count+".txt");
            PrintWriter writer_query = new PrintWriter(writer2); 
            FileWriter writer1 = new FileWriter("D:\\IR_Project\\Query\\query.txt");
            PrintWriter writer = new PrintWriter(writer1);
            try {
            	//Input for search query
                System.out.println("Enter the search query (q=quit):");
                s = br.readLine();
                writer.println(s);
                writer.close();
                //Tokenize,stopword removal,stemming of the given query
                TokenSummary tokenSummary = new TokenSummary();
                String file1="D:\\IR_Project\\Query";
	            File[] StopwordsRemovedFiles = Utility.getFiles(file1);
	            for (File f : StopwordsRemovedFiles) {
	                if (f.isFile()) {
	                	StopWords stopWord= new StopWords(f);
	                }
	            }
	            String file2="D:\\IR_Project\\Results";
	            File[] StopwordsRemoved = Utility.getFiles(file2);
	            for (File f : StopwordsRemoved) {
	                if (f.isFile()) {
	                	TokenExecutor tokenExecutor1 = new TokenExecutor(tokenSummary, f);
	                }
	            }
	            StemmingExecutor stemmingExecutor = new StemmingExecutor(tokenSummary);
	            stemmingExecutor.processStemming(str);
	            String filename ="D:\\IR_Project\\QueryStemmed";
	            File[] Stemmed = Utility.getFiles(filename);
	            for (File file : Stemmed) {
	                if (file.isFile()) {
	                	bufferedReader = new BufferedReader(new FileReader(file));
	                }
	            }
                List<String>lines=new ArrayList<String>();
                String line;
                
				while ((line = bufferedReader.readLine()) != null) {
					lines.add(line);
					query=line;
                }
				bufferedReader.close();
                if (s.equalsIgnoreCase("q")) {
                    writer_query.close();
                    break;
                }               
                
                /*FileWriter wt = new FileWriter("D:\\IR_Project\\TeluguFiles\\query.txt");
                PrintWriter writer_tf = new PrintWriter(wt);
                writer_tf.println(query);
                writer_tf.close();
                DocumentParser dp = new DocumentParser();
                dp.parseFiles("D:\\IR_Project\\TeluguFiles");
                dp.tfIdfCalculator(); //calculates tfidf
                dp.getCosineSimilarity();*/
                
                if(query.isEmpty()) {
                	System.out.println("Found 0 hits");
                }else {
                	//Using Simple Analyzer
	                Query q = new QueryParser(Version.LUCENE_47, "contents", sAnalyzer).parse(query);
	                searcher.search(q, collector);
	                ScoreDoc[] hits = collector.topDocs().scoreDocs;
	                
	                // Write the results in to a file
	                System.out.println("Found " + hits.length + " hits.");
	                for (int i = 0; i < hits.length; ++i) {
	                    int docId = hits[i].doc;
	                    Document d = searcher.doc(docId);
	                    writer_query.println((i + 1) + "," + s + ", " + d.get("filename")
	                            + "," + hits[i].score);
	                    System.out.println((i + 1) + "," + s + ", " + d.get("filename")
	                    + "," + hits[i].score);
	                }
                }

            } catch (Exception e) {
                System.out.println("Error searching " + s + " : "
                        + e.getMessage());
                break;
            }
            writer_query.close();
        }

    }

    //Constructor
    ApplicationRunner(String indexDir) throws IOException {

        FSDirectory dir = FSDirectory.open(new File(indexDir));

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,sAnalyzer);

        writer = new IndexWriter(dir, config);

    }
    
    public void indexFileOrDirectory(String fileName) throws IOException {
        
        addFiles(new File(fileName));
        int originalNumDocs = writer.numDocs();
        for (File f : queue) {
            FileReader fr = null;
            StringReader strread = null;
            BufferedReader br = null;
            try {
                Document doc = new Document();

                // add contents of the file
                fr = new FileReader(f);

                // Read the file into BufferedReader 
                br = new BufferedReader(fr);
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                
                // Convert each Reader into a String of words
                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = br.readLine();
                }
                strread = new StringReader(Jsoup.parse(sb.toString()).text());

                FieldType type = new FieldType();
                type.setIndexed(true);
                type.setStored(true);

                doc.add(new TextField("contents", strread));
                doc.add(new StringField("path", f.getPath(), Field.Store.YES));
                doc.add(new StringField("filename", f.getName(),
                        Field.Store.YES));

                writer.addDocument(doc); // Write the index
                System.out.println("Added: " + f);
            } catch (Exception e) {
                System.out.println("Could not add: " + f);
            } finally {
                fr.close();
            }
        }
        int newNumDocs = writer.numDocs();
        System.out.println("************************");
        System.out
                .println((newNumDocs - originalNumDocs) + " documents added.");
        System.out.println("************************");
        queue.clear();
    }
    
    //Function to add the files inorder to index
    private void addFiles(File file) {
        if (!file.exists()) {
            System.out.println(file + " does not exist.");
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                addFiles(f);
            }
        } else {
            String filename = file.getName().toLowerCase();
            // Only index text files
            if (filename.endsWith(".txt")) {
                queue.add(file);
            } else {
                System.out.println("Skipped " + filename);
            }
        }
    }
    public void closeIndex() throws IOException {
        writer.close();
    }
}
