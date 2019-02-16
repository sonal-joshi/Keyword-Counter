import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sonal
 *
 */
public class keywordcounter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//Store file path 
			String filepath = args[0];
			
			//Reading data from the input files
			List<String> lines = Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8);
			
			//output file for the top keywords
			PrintWriter output_file = new PrintWriter("output_file.txt");
			
			//HashMap to store the keywords and corresponding pointers to their fibonodes
			HashMap<String,fibonode> keywordMap = new HashMap<String,fibonode>();
			
			//Fibonacci heap for keeping track of keywords
			fibonacciheap fiboheap = new fibonacciheap();
			
			//Input format for keyword and search count : $keyword search_count
			Pattern keyword = Pattern.compile("([$])([^\\s]+)(\\s)(\\d+)");
			//input format for query number: query_number
	        Pattern number = Pattern.compile("(\\d+)");
	        
			for(int i=0;i<lines.size();i++) {
				
				String currentStr = lines.get(i);
				
				//match the current string with the pattern
		        Matcher keywordStr = keyword.matcher(currentStr);
	            Matcher numberStr = number.matcher(currentStr);
	            
	            //if current string is a keyword
				if(keywordStr.find()) {
					String keywordVal = keywordStr.group(2);
					int countVal = Integer.parseInt(keywordStr.group(4));
					
					//if keyword is already inserted into fibonacci heap, increment the search count. Else add the new node into the fibonacci heap
					if(keywordMap.containsKey(keywordVal)) {
						fiboheap.incrementcount(keywordMap.get(keywordVal),countVal);
					} else {
						fibonode newNode = new fibonode(keywordVal,countVal);
						fiboheap.insert(newNode);
						keywordMap.put(newNode.getKeyword(), newNode);
					}
				
				} 
				//if current string is a query number
				else if (numberStr.find()) {
					  int queryNumber = Integer.parseInt(numberStr.group(1));
	                ArrayList<fibonode> removedNodes = new ArrayList<fibonode>(queryNumber);
	                
	                //remove max node from heap removeNumber times
	                for ( int k=0;k<queryNumber;k++)
	                {
	                    //Removed top node
	                    fibonode topnode = fiboheap.removemax();
	                    
	                    if(topnode != null) {
	                    	keywordMap.remove(topnode.getKeyword());

		                        //Create new node for insertion
		                    fibonode newNode= new fibonode(topnode.getKeyword(),topnode.getCount());

		                        //add the new node for insertion into removed nodes list
		                    removedNodes.add(newNode);

		                        //Write the removed nodes to the output file
		                    if ( k < queryNumber-1) {
		                        output_file.print(topnode.getKeyword() + ",");
		                    }
		                    else {
		                    	output_file.print(topnode.getKeyword());
		                    }
	                    } else 
	                    	break;		//If Query number is greater than the nodes in heap, top node returned after removemax would be null. In this case break the loop.
	                  
	                }
	                output_file.println("");
	                //insertion step
	                for (fibonode node : removedNodes)
	                {
	                    fiboheap.insert(node);
	                    keywordMap.put(node.getKeyword(),node);
	                }
	            } else if(currentStr!=null && currentStr.toLowerCase().equals("stop")) {
	            	break;
	            }
				}
			output_file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}



