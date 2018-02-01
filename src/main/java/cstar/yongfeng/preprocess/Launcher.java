package cstar.yongfeng.preprocess;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class <b>Launcher</b> provides the main entry of the project.</p> 
 *
 */
public class Launcher {

	/** event root directory */
	final public static String EVENT_DATA = "C:/MSR18Dataset/Events-170301-2/Events-170301-2/";

	public static void main(String[] args) {

		/** STEP 1: Extract event streams from all zip files in EVENT_DATA directory. 
		 *          Note that: each developers' stream data is stored in "total" directory, eg. 2016-05-09-10.zip.txt.
		 *          The results are saved in src/main/resources/total/ directory
		 * **/
		ExtractEvents ee1 = new ExtractEvents(EVENT_DATA);
		ee1.extract();
		
		/** STEP 2: Get the event pair from the event streams and obtain the Transfer Probability Matrix(TPM) from these pair.
		 *          Note that: for all 81 developers, each time we take 1 as the testing set, and  take other 80
		 *          as the training set to construct the TPM. 
		 *          The results are saved in src/main/resources/matrix.txt
		 * **/
//		List<Pairs> lsPairs = new ArrayList<Pairs>();
//		lsPairs = ConstructPairs.getPairs("src/main/resources/total/", 0);
		
		/**
		 * STEP 3: Identify the user profile information.
		 *         Note that: we only identify the position and C# ability of each user.
		 *         The results are saved in src/main/resources/userProfile.txt
		 * **/
//		ExtractEvents ee2 = new ExtractEvents(EVENT_DATA);
//		ee2.identify();
		
		/**
		 * STEP 4: Slice the event stream of one developer based on the session ID and time interval
		 *         Note that the time interval is set to 1 minutes in this experiment.
		 * 		   The results are saved in src/main/resources/slice-id-1/ directory
		 * **/
//		ExtractEvents ee3 = new ExtractEvents(EVENT_DATA);
//		ee3.slice();
	}
}