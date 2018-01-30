package cstar.yongfeng.preprocess;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class <b>ShowPairs</b> is used to get event stream pair from the whole event streams.</p>
 * @see#getPairs(String path) To get event pair from a file
 * @see#getPairs(String dir, int flag) To get event pair from a directory 
 * @author Yongfeng
 */
public class ConstructPairs {

	/*** This main function is ONLY used for debugging ***/
//	public static void main(String[] args) {
//		List<Pairs> lsPairs = new ArrayList<Pairs>();
//		lsPairs = getPairs("src/main/resources/total/", 0); // stream context directory
//	}
	
	/***
	 * <p>To get event stream pair from the file in path</p>
	 * @param path file path
	 * @return lsPairs List of Object Pairs
	 *  
	 */
	public static List<Pairs> getPairs(String path){
		
		List<Pairs> lsPairs = new ArrayList<Pairs>();
		List<Integer> lsEvents = new ArrayList<Integer>();
		
		try {
			lsEvents = getEvents(path); // get all event from file in path
		} catch (Exception e) {
			System.out.println("File Not Found!");
			e.printStackTrace();
		}
		
		for(int i=0; i<lsEvents.size()-1; i++){
			// initialize the Pair object and save into lsPair
			Pairs pair = new Pairs(lsEvents.get(i), lsEvents.get(i+1));
			lsPairs.add(pair);
//			System.out.println("[" + pair.getPrior() + ", " + pair.getNext() + "]");
		}
		
		if(lsPairs.size() == 0){ // in case of none event lsPair
			System.out.println("Nothing to extract!");
		}
		
		return lsPairs;
	}
	
	/***
	 * <p>To get event stream pair from the all files in directory, and print each Transfer Probability Matrix.</p>
	 * @param dir files directory
	 * @param flag directory flag
	 * @return lsPairs List of Object Pairs
	 *  
	 */
	public static List<Pairs> getPairs(String dir, int flag){
		
		List<Pairs> lsPairs = new ArrayList<Pairs>();
		File pathDir = new File(dir);
		
		if(pathDir.isDirectory()){ // if the dir is a legal directory
			File[] arrFiles = pathDir.listFiles(); // get all files under dir
			for(int i=0; i<arrFiles.length; i++){ // each time, taking arrFiles[i] as the testing set
				
				System.out.println("Testing  set: " + i + " [" + arrFiles[i].getName() + "]");
				System.out.print("Training set: ");
				int j=-1;
				while(j<arrFiles.length-1){
					j++;
					if(j == i){
						continue;
					}
//				for(int j=0; j<arrFiles.length && j!=i; j++){ // taking other arrFiles as the training set 
					List<Pairs> lsTemp = getPairs(arrFiles[j].getAbsolutePath().toString());
					System.out.print(j + ",");
					lsPairs.addAll(lsTemp);
				}
				
				System.out.println("");
				
				ConstructMatrix.showMatrix(lsPairs);
				
				lsPairs.clear();
				
				System.out.println("");
			}
				
		}else{ // if the dir is a file path
			lsPairs = getPairs(dir);
		}
		return lsPairs;
	}
	
	/**
	 * <p>To get all events from the file in path.</p>
	 * @param path
	 * @return lsEvents event list
	 * @throws Exception
	 */
	public static List<Integer> getEvents(String path) throws Exception{
		List<Integer> lsEvents = new ArrayList<Integer>();
		File file = new File(path);
		
//		System.out.println(file.getAbsolutePath());
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		String str = "";
		while((str=br.readLine())!=null){
			int num = -1;
			try{
				num = Integer.valueOf(str.trim());
			}catch(Exception e){
				System.out.println("CHAR: " + str);
			}
			lsEvents.add(num);
		}

		br.close(); // close the stream
		fr.close();
		
		return lsEvents;
	}

}

/***
 * <p>Pairs class is used to save the event stream pair(prior, next).
 * function {@link getPrior()} and {@link getNext()} can get the prior and the next event ID in one pair.</p>
 */
class Pairs{
	private int prior;
	private int next;
	
	Pairs(int p, int n){
		this.prior = p;
		this.next = n;
	}
	
	public int getPrior(){
		return this.prior;
	}
	
	public int getNext(){
		return this.next;
	}
	
	public void showPairs(){
		System.out.println("<" + this.prior + "," + this.next + ">");
	}
}

/***
 * <p>Pairs class is used to save the event stream pair(prior, next).
 * function {@link getPrior()} and {@link getNext()} can get the prior and the next event ID in one pair.</p>
 */
class Stamps{
	private int prior;
	private int next;
	
	Stamps(int p, int n){
		this.prior = p;
		this.next = n;
	}
	
	public int getPrior(){
		return this.prior;
	}
	
	public int getNext(){
		return this.next;
	}
	
	public void showPairs(){
		System.out.println("<" + this.prior + "," + this.next + ">");
	}
}
