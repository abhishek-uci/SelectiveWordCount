import java.io.*;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("unused")
public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
        Integer[] index = getIndexes();
        Map<String, Integer> hmap = new HashMap<String, Integer>();
        Map<Integer, String> lines = new HashMap<Integer, String>();
        String line = "";
        String data = "";
        BufferedReader bf = new BufferedReader(new FileReader(this.inputFileName));
        
        int lineIndex =0;
        while((line=bf.readLine())!=null){
        	lines.put(lineIndex,line);
        	lineIndex+=1;
        }
        bf.close();
        	
        for(Integer i : index){
        	data = lines.get(i);
        		StringTokenizer st = new StringTokenizer(data,delimiters);
        		while(st.hasMoreTokens()){
        			String word = st.nextToken();
        			word = word.toLowerCase();
        		
        			if(checkWord(word)){ 
        				if(hmap.get(word)==null)
        					hmap.put(word,1);
        				else{
        					int count = hmap.get(word);
        					hmap.put(word,++count);
        				}
        			}
        		}
        	}	
    
       
      
        Set<Entry<String, Integer>> set = hmap.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
            {
                
            	if(o2.getValue() == o1.getValue())
                return  (o1.getKey().toString().compareToIgnoreCase(o2.getKey().toString()));
            	else 
            		return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        
        int count =0;
        for(Map.Entry<String, Integer> entry:list){
        	if(count <20){
        		ret[count] = entry.getKey();
        		System.out.println(entry.getValue());
        		count+=1;
        	}
        	
        }
        return ret;
    }

    
    boolean checkWord(String word){
    	for(int i=0; i<stopWordsArray.length; i++){
			if(stopWordsArray[i].equals(word)) 
				return false;
		}
    	return true;
    }
    
  
    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
