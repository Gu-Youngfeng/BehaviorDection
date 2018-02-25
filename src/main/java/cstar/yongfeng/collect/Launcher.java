package cstar.yongfeng.collect;

import java.text.ParseException;
//import java.util.ArrayList;
import java.util.List;

//import cc.kave.commons.model.events.IDEEvent;

public class Launcher {

	public static void main(String[] args) throws ParseException {
		
		/** TESTING **/
//		Collector collector = new Collector("C:/MSR18Dataset/Events-170301-2/Events-170301-2/2016-05-09/1.zip", CollectType.DebugTime);
//		for(ArrayList<IDEEvent> lsStream: collector.getlslsEvent()){
//			for(IDEEvent event: lsStream){
//				MsgGetter getter = new MsgGetter(event);
//				System.out.println(getter.getInfo());
//			}
//			System.out.println("--------");
//		}
//		long count = Collector.getInIDETime("E:/workspaceee/BehaviorDection/src/main/resources/total/2016-05-09-10.zip.txt");
//		System.out.println("[SumTime]: " + count*1.0/(1000*60*1.0));
//		int count2 = Collector.getPerformance("E:/workspaceee/BehaviorDection/src/main/resources/total/2016-05-09-10.zip.txt");
//		System.out.println("[perform]: " + count2);
		
//		System.out.println("[path]:" + Collector.searchEventFile("C:/MSR18Dataset/Events-170301-2/Events-170301-2/2016-05-09/1.zip"));
		
//		System.out.println("[strDate]: " + Collector.extractDate("### IDEStateEvent | 2016-12-12T23:36:35.259339900+01:00 | [lifeCycle]:Shutdown"));
//		Collector.getDevelopDays("E:/workspaceee/BehaviorDection/src/main/resources/total/2016-05-09-10.zip.txt");
		
		/**STEP 1: Divide the user based on Programming Skills: Positive & Negative & Neutral
		 *         We can also divide the user based on other attribute, such as position, etc..
		 * */
		String userProfile = "E:/workspaceee/BehaviorDection/src/main/resources/userProfile.txt";
		
//		List<String> lsPositivePaths = UserDivition.getUserByKeyWrods(userProfile, "Positive");
//		System.out.println("\n[positive size]----" + lsPositivePaths.size());
//		for(String path: lsPositivePaths){
//			System.out.println("[userZip]: " +path);
//		}
//		
//		List<String> lsNegativePaths = UserDivition.getUserByKeyWrods(userProfile, "Negative");
//		System.out.println("\n[Negative size]----" + lsNegativePaths.size());
//		for(String path: lsNegativePaths){
//			System.out.println("[userZip]: " + path);
//		}
//		
//		List<String> lsNeutralPaths = UserDivition.getUserByKeyWrods(userProfile, "Neutral");
//		System.out.println("\n[Neutral size]----" + lsNeutralPaths.size());
//		for(String path: lsNeutralPaths){
//			System.out.println("[userZip]: " + path);
//		}
		
		/**STEP 2: Analyze the user debugging behaviors. We just print them in the Console Window.
		 * 
		 */
//		for(String path: lsPositivePaths){
//			UsageGetter usager = new UsageGetter(path);
//			usager.showMetric();
//		}
//		System.out.println("-------");
//		for(String path: lsNegativePaths){
//			UsageGetter usager = new UsageGetter(path);
//			usager.showMetric();
//		}
//		System.out.println("-------");
//		for(String path: lsNeutralPaths){
//			UsageGetter usager = new UsageGetter(path);
//			usager.showMetric();
//		}
		
//		UsageGetter usager = new UsageGetter("C:/MSR18Dataset/Events-170301-2/Events-170301-2/2016-09-28/11.zip");
//		usager.showMetric();
		
//		System.out.println("[FREE]: " + Runtime.getRuntime().freeMemory()/(1024*1024));
//		System.out.println("[TOTA]: " + Runtime.getRuntime().totalMemory()/(1024*1024));
		
		/**
		 * STEP 1 & 2
		 */

		DoWorks(userProfile, "Positive"); // to get rid of the risk of memory leak.
		DoWorks(userProfile, "Negative");
		DoWorks(userProfile, "Neutral");
//		System.out.println("[Day]" + Collector.getDevelopDays("E:/workspaceee/BehaviorDection/src/main/resources/total/2016-09-28-11.zip.txt"));
	}
	
	public static void DoWorks(String userProfile, String prefix){
		List<String> lsPositivePaths = UserDivition.getUserByKeyWrods(userProfile, prefix);
		System.out.println("\n[" + prefix + "]----{" + lsPositivePaths.size() + "}");
//		for(String path: lsPositivePaths){
//			System.out.println("[userZip]: " + path);
//			System.out.println("[path   ]: " + Collector.searchEventFile(path));
//		}
		for(String path: lsPositivePaths){
			UsageGetter usager = new UsageGetter(path);
			usager.showMetric();
			
			System.out.println("");
//			break;
		}
	}

}
