import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StemmingExecutor {
    private TokenSummary tokenSummary;
    private TokenSummary tokenStemSummary;
    public StemmingExecutor(TokenSummary tokenSummary) {
        this.tokenSummary = tokenSummary;
        this.tokenStemSummary = new TokenSummary();
    }
    
    //Function processStemming
    public void processStemming(String s) throws IOException {
        HashMap<String, Integer> tokenMapData = tokenSummary.getTokenMap();
        HashMap<String, Integer> tokenStemMapData = tokenStemSummary.getTokenMap();
        Stemmer stemmer;
        String stem;
        for (Map.Entry<String, Integer> tokenEntry : tokenMapData.entrySet()) {
            String token = tokenEntry.getKey();
            int count = tokenEntry.getValue();
            stemmer = new Stemmer();
            stemmer.add(token.toCharArray(), token.length());
            stemmer.stem();
            stem = stemmer.toString();
            if (!tokenStemMapData.containsKey(stem)) {
                tokenStemMapData.put(stem, count);
            } else {
                tokenStemMapData.put(stem, count + tokenStemMapData.get(stem));
            }
        }

        tokenStemSummary.setTokenMap(tokenStemMapData);
        int totalStemCount = 0;
        int onlyOccurrenceCount = 0;
        for (String key : tokenStemMapData.keySet()) {
            totalStemCount += tokenStemMapData.get(key);
            if(tokenStemMapData.get(key) == 1)
                onlyOccurrenceCount++;
        }
        tokenStemSummary.setTokenCount(totalStemCount);
        tokenStemSummary.setOnlyOccuranceCount(onlyOccurrenceCount);
        tokenStemSummary.printStatistics(s);
    }
}
