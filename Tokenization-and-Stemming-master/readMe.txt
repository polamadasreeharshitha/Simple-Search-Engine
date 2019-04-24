SEARCH-ENGINE FOR TELUGU LANGUAGE
----------------------------------

Steps to run the application:

1. First Run the ApplicationRunner.java which has the main method.
2.However, if dataset is available in the path D:\IR_Project\TeluguFiles, then provide the path when it asks for input.

Following are the information about the java files attached:

1.	ApplicationRunner.java - Based on the input the class creates calls the respective functions to tokenize,removing stopwords and stemming are done on the documents.
2.	Stemmer.java  - Class downloaded which does the actual stemming process
3.	StemmingExecutor.java - Class which uses Stemmer.java and stemmed tokens are stored in a file.
4.	TokenSummary.java - Entity which holds all the parameters of tokenization
5.	TokenExecutor.java - Class which calls TokenSummary, processes each file and tokens are obtained.
6.	StopWords.java - Class where stop words are removed from each document.
7.	CosineSimilarity.java - Class which calculates the cosine similarility between two vectors.
8.	TfIdf.java - Class where tfidf of documents are calculated.
9.	DocumentParser.java - Class where vectors are created for each document and  Cosine Similarity is calculated between query and document and stored in a file. 
10.	Constant.java -	Has static constant member variables	
11.	Utility.java - Class which has static helper methods

Information about Inputs:

While running it asks for the inputs for which following need to be provided:-
-First provide dataset path
-Second provide path where all the above functions are performed to index them.Enter 'q' to stop adding documents to be added.
-Next provide the query to get the results.

 