package cstar.yongfeng.collect;

public class Launcher {

	public static void main(String[] args) {
		
		UsageGetter uger = new UsageGetter("C:/MSR18Dataset/Events-170301-2/Events-170301-2/2016-09-28/11.zip");
		long[] metrics = uger.getMetric();
		
		for(int i=0; i<metrics.length; i++){
			System.out.println(metrics[i] + " ");
		}
	}

}
