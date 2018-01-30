package cstar.yongfeng.preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * <p>class <b>CommandExtractor</b> not only provide the</p>
 * 
 */
public class CommandExtractor {

	public static void main(String[] args) {
		
//		System.out.println(extract("VsAction:1:View.ObjectBrowsingScope"));
//		System.out.println(extract("{6E87CFAD-6C05-4ADF-9CD7-3B7943875B7C}:1280:Debug.StartupProjects"));

		/** Counting the event number, partition numbers, average event of partition, of each developer in directory*/
//		showStatistic("E:\\workspaceee\\BehaviorDection\\src\\main\\resources\\slice-id-1");
		
		
		/** Getting the stream partitions of one developers in path*/
		List<ArrayList<Integer>> lsPartitions = getPartition("src\\main\\resources\\slice-id-1\\1.zip.txt");
		System.out.println("[SIZE]:" + lsPartitions.size());
		for(ArrayList<Integer> lsSmall: lsPartitions){
//			System.out.println(":" + lsSmall.size());
			for(int i=0; i<lsSmall.size(); i++){
				System.out.print(lsSmall.get(i) + " ");
			}
			System.out.println("");
		}
		
	}
	
	
	/**
	 * <p>To extract the detailed command from the CommandID, for example,</p>
	 * <pre>VsAction:1:View.ObjectBrowsingScope -- View.ObjectBrowsingScope</pre>
	 * @param cid CommandID
	 * @return detailed command
	 */
	public static String extract(String cid){
		if(cid == null || cid.trim() == ""){
			System.out.println("Command ID == null!");
			return null;
		}
		
		String commandStr = "";
		
		String reg = "(.*):(\\d*):(.*)";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(cid);
		if(matcher.find()){
			commandStr = matcher.group(3);
		}
		
		return commandStr;
	} 
	
	/**
	 * <p>To show the statistical information of the partition. Including the length of average event of each developer,
	 * the average partition number of each developer, the average event in each partition.</p>
	 * @param dir
	 */
	public static void showStatistic(String dir){
		List<Integer> lsLen = getLength(dir);
		List<Integer> lsSplit = getSplit(dir);
		System.out.println("Size: " + lsLen.size());
		for(int i=0; i<lsLen.size(); i++){
//			System.out.printf("[Length]:%-8d,[partition]:%-8d,[Ave]:%-8f\n", lsLen.get(i), lsSplit.get(i), (lsLen.get(i)-lsSplit.get(i)-1)*1.0/lsSplit.get(i)*1.0);		
			System.out.printf(" "+(lsLen.get(i)-lsSplit.get(i)+1)*1.0/lsSplit.get(i)*1.0);  //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          average event in partition of each developer
//			System.out.printf(" "+lsSplit.get(i)); // number of partition of each developer
//			System.out.printf(" "+(lsLen.get(i)-lsSplit.get(i)-1)); // number of event of each developer
		}
		System.out.println("");
	}
	
	/**<p>To get the <b>partition size</b> of the event stream directory.</p>
	 * <p>Actually, we only identify the <b>"---"</b> in event stream, the partition number is equals to the "---" number</p>
	 * @param dir file directory eg. src/slice-id-1/
	 * @return lsInt the partition number of event streams of 80 developers
	 * */
	public static List<Integer> getSplit(String dir){
		List<Integer> lsInt = new ArrayList<Integer>();
		File directory = new File(dir);
		if(!directory.isDirectory()){
			System.out.println("Directory is illegal!");
			return null;
		}
		
		File[] files = directory.listFiles();
		
		if(files.length == 0){
			System.out.println("Empty directory!");
			return null;
		}
		for(int i=0; i<files.length; i++){
			int numSplits = getSplitNum(files[i]);
			lsInt.add(numSplits);
		}
		
		return lsInt;
	}
	
	public static int getSplitNum(File file){
		int count = 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String str = "";
			while((str = br.readLine())!=null){
				if(str.equals("---")){
					count ++;
				}
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			System.out.println("Read File Error!");
			e.printStackTrace();
		}
		
		return count;
		
	}
	
	/**<p>To get the <b>total line</b> List of the event stream directory.</p>
	 * <p>Actually, this length is the Loc of files, which includes "---" symbol.</p>
	 * @param dir file directory eg. src/slice-id-1/
	 * @return lsInt the length of event streams of 80 developers
	 * */
	public static List<Integer> getLength(String dir){
		List<Integer> lsInt = new ArrayList<Integer>();
		File directory = new File(dir);
		if(!directory.isDirectory()){
			System.out.println("Directory is illegal!");
			return null;
		}
		
		File[] files = directory.listFiles();
		
		if(files.length == 0){
			System.out.println("Empty directory!");
			return null;
		}
		for(int i=0; i<files.length; i++){
			int numSplits = getLengthNum(files[i]);
			lsInt.add(numSplits);
		}
		
		return lsInt;
	}
	
	public static int getLengthNum(File file){
		int count = 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String str = "";
			while((str = br.readLine())!=null){
					count ++;
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			System.out.println("Read File Error!");
			e.printStackTrace();
		}
		
		return count;
		
	}
		
	/** To get event ID in each partition in file*/
	public static List<Integer> getPartitionNum(File file){
		List<Integer> count = new ArrayList<Integer>();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String str = "";
			while((str = br.readLine())!=null){
				if(str.equals("---")){
					int flag = -1;
					count.add(flag);
//					System.out.println("add ---");
				}else{
					int event = Integer.valueOf(str);
					count.add(event);
				}
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			System.out.println("Read File Error!");
			e.printStackTrace();
		}
		
		return count;
		
	}

	/**<p>To get event number in partition of 80 developers.</p>
	 * <p>The return lsInt = {A1, A2, ..., A80}, Ai denotes the i-th developer, Ai = {1, 4, 5, ...}, 
	 * 1, 4, 5 denotes the event ID of the 1-st, 2-nd, 3-rd event partition of developer Ai.</p>
	 * @param dir event stream file contains "---"
	 * @return lsInt
	 * */
	public static List<ArrayList<Integer>> getPartition(String path){
		List<Integer> lsEvents = getPartitionNum(new File(path));
		List<ArrayList<Integer>> lsPartitions = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Integer> lsTemp = new ArrayList<Integer>();
		for(int i=0; i<lsEvents.size(); i++){
			
			int eventID = lsEvents.get(i);
			if(eventID != -1){ // event ID
				lsTemp.add(eventID); // add event ID to lsTemp	
//				System.out.println(eventID);
			}else{ // split line i.e., event ID = -1
//				System.out.println("---");		
				ArrayList<Integer> lsAdded = new ArrayList<Integer>(lsTemp);
				lsPartitions.add(lsAdded); // add
//				System.out.println(lsTemp);
				lsTemp.clear();
			}
		}
		
		return lsPartitions;
	}
}
