import java.io.*;
import java.util.StringTokenizer;

public class TokenExecutor {

	//Private variables
    private TokenSummary tokenSummary;
    private File lstFiles;
    
    //Constructor
    public TokenExecutor(TokenSummary tokenSummary, File listFiles) {
        this.tokenSummary = tokenSummary;
        lstFiles = listFiles;
        processFiles();
    }

    //Private Methods
    private void processFiles() {
        BufferedReader bufferedReader = null;
        //long startTime = System.currentTimeMillis();
        try {
            
        			File file = lstFiles;
                    tokenSummary.incrementDocumentCount();
                    bufferedReader = new BufferedReader(new FileReader(file));
                    
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        StringTokenizer tokenizer = new StringTokenizer(line, Constants.TOKENIZER_SPLIT);
                        while (tokenizer.hasMoreTokens()) {

                            String word = tokenizer.nextToken().trim().toLowerCase();
                           
                            // split the word if it has - or _ or white spaces like tab or \n
                            String[] subWords = word.split("\\s+|\\-|\\_|\\(|\\)|\\,|\\\\|\\/");

                            for (String subWord : subWords) {

                                if (subWord.trim().isEmpty())
                                    continue;

                                // remove words which are just numbers or just symbols
                                if (subWord.matches("(\\d)*") || subWord.matches("(\\d)*.") || subWord.matches("(\\d)*.(\\d)*") ||
                                        subWord.matches("[^\\w\\s]+"))
                                    continue;

                                // handle the 's by spliting the part and taking the actual work
                                /*if (subWord.matches("(.*)\\'s"))
                                    subWord = subWord.replace("'s", "");*/

                                tokenSummary.addToDictionary(subWord);
                            }
                        }
                    }
                
            //long endTime = System.currentTimeMillis();
            //tokenSummary.setTimeTake(endTime - startTime);
        } catch (Exception ex) {
            System.out.println(ex.getCause());
        }
    }


}
