package cstar.yongfeng.preprocess;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class <b>ConstructMatrix</b> is used to construct the Transfer Probability Matrix by event pair list.</p>
 * @see#getMatrix(lsPairs)
 */
public class ConstructMatrix {
	
	/** To configure the event type number to 20*/
	final public static int EVENT_TYPE_SIZE = 20;

	/*** This main function is ONLY used for debugging ***/
//	public static void main(String[] args) {
//		List<Pairs> lsPairs = ConstructPairs.getPairs("src/main/resources/SimpleEventStream.txt"); // stream context directory
//		double[][] eventMatrix = getMatrix(lsPairs);
//		
//		for(int i=0; i<eventMatrix.length; i++){
//			for(int j=0; j<eventMatrix[i].length; j++){
//				System.out.printf("%-2.6f  ", eventMatrix[i][j]);
//			}
//			System.out.println("");
//		}
//	}
	
	/**
	 * <p>To get <b>State Transfer Probability Matrix</b> (20&times;20) based on Pairs list.</p>
	 * @param lsPairs
	 * @return
	 */
	public static double[][] getMatrix(List<Pairs> lsPairs){
		System.out.println("-----------------  State Transfer Probability Matrix  -----------------");
		
		double [][] eventMatrix = new double[EVENT_TYPE_SIZE][EVENT_TYPE_SIZE];
		
		for(int i=0; i<EVENT_TYPE_SIZE; i++){
			// get event pair starts with event i
			List<Pairs> lsEventStartWithI = getPairsByPrior(lsPairs, i);
			int sum = lsEventStartWithI.size(); // total size of lsEventStartWithI
			for(int j=0; j<EVENT_TYPE_SIZE; j++){
				// get event pair starts with event i and ends with event j
				List<Pairs> lsEventEndWithJ = getPairsByNext(lsEventStartWithI, j);
				if(sum != 0){
					eventMatrix[i][j] = lsEventEndWithJ.size()*1.0/sum*1.0;
				}else{
					eventMatrix[i][j] = 0;
				}
			}
		}
		
		return eventMatrix;
	} 
	
	/**
	 * <p>To select event pairs starts with <b>eventID</b> from pair list <b>lsPairs</b>.</p>
	 * @param lsPairs
	 * @param eventID
	 * @return lsEventsI
	 */
	public static List<Pairs> getPairsByPrior(List<Pairs> lsPairs, int eventID){
		List<Pairs> lsEventsI = new ArrayList<Pairs>();
		
		for(int i=0; i<lsPairs.size(); i++){
			Pairs currentPair = lsPairs.get(i);
			if(currentPair.getPrior() == eventID){
				lsEventsI.add(currentPair);
			}
		}
		
		return lsEventsI;
	}
	
	/**
	 * <p>To select event pairs ends with <b>eventID</b> from pair list <b>lsPairs</b>.</p>
	 * @param lsPairs
	 * @param eventID
	 * @return lsEventsJ
	 */
	public static List<Pairs> getPairsByNext(List<Pairs> lsPairs, int eventID){
		List<Pairs> lsEventsJ = new ArrayList<Pairs>();
		
		for(int i=0; i<lsPairs.size(); i++){
			Pairs currentPair = lsPairs.get(i);
			if(currentPair.getNext() == eventID){
				lsEventsJ.add(currentPair);
			}
		}
		
		return lsEventsJ;
	}
	
	public static void showMatrix(List<Pairs> lsPairs){
		double[][] eventMatrix = getMatrix(lsPairs);
		
		for(int i=0; i<eventMatrix.length; i++){
			for(int j=0; j<eventMatrix[i].length; j++){
				System.out.printf("%-2.6f  ", eventMatrix[i][j]);
			}
			System.out.println("");
		}
	}

}
