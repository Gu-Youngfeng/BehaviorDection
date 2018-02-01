package cstar.yongfeng.collect;

import java.util.List;

public class Launcher {

	public static void main(String[] args) {
		
//		UsageGetter uger = new UsageGetter("C:/MSR18Dataset/Events-170301-2/Events-170301-2/2016-09-28/11.zip");
//		long[] metrics = uger.getMetric();
//		
//		for(int i=0; i<metrics.length; i++){
//			System.out.println(metrics[i] + " ");
//		}
		
		/**STEP 1: Divide the user based on Programming Skills: Positive & Negative & Neutral
		 *         We can also divide the user based on other attribute, such as position, etc..
		 * */
		String userProfile = "E:/workspaceee/BehaviorDection/src/main/resources/userProfile.txt";
		
		
		List<String> lsPositivePaths = UserDivition.getUserByKeyWrods(userProfile, "Positive");
		System.out.println("\n[positive size]----" + lsPositivePaths.size());
		for(String path: lsPositivePaths){
			System.out.println("[userZip]: " +path);
		}
		
		List<String> lsNegativePaths = UserDivition.getUserByKeyWrods(userProfile, "Negative");
		System.out.println("\n[Negative size]----" + lsNegativePaths.size());
		for(String path: lsNegativePaths){
			System.out.println("[userZip]: " + path);
		}
		
		List<String> lsNeutralPaths = UserDivition.getUserByKeyWrods(userProfile, "Neutral");
		System.out.println("\n[Neutral size]----" + lsNeutralPaths.size());
		for(String path: lsNeutralPaths){
			System.out.println("[userZip]: " + path);
		}
		
		/**STEP 2: Analyze the user debugging behaviors. We just print them in the Console Window.
		 * 
		 */
		for(String path: lsPositivePaths){
			UsageGetter usager = new UsageGetter(path);
			usager.showMetric();
		}
		for(String path: lsNegativePaths){
			UsageGetter usager = new UsageGetter(path);
			usager.showMetric();
		}
		for(String path: lsNeutralPaths){
			UsageGetter usager = new UsageGetter(path);
			usager.showMetric();
		}
		
	}

}
