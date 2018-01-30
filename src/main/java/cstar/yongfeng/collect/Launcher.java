package cstar.yongfeng.collect;

public class Launcher {

	public static void main(String[] args) {
		
		Collector collector = new Collector("C:/MSR18Dataset/Events-170301-2/Events-170301-2/2016-09-28/11.zip", CollectType.DebugTime);
		collector.Collecting();
//		collector.showProcess();
		
//		Collector collector = new Collector("C:/MSR18Dataset/Events-170301-2/Events-170301-2/2016-09-28/11.zip", CollectType.WorkTime);
//		collector.Collecting();
//		collector.showProcess();
		
		Analyzer analyzer = new Analyzer(collector.getlslsEvent());
		System.out.println("Debugging            :" + analyzer.getStreamTimes());
		System.out.println("[Breakpoint]         : " + analyzer.getBreakpoint());
		System.out.println("[Restart Debugging]  : " + analyzer.getRestart());
		System.out.println("[StepInto & StepOver]: " + analyzer.getStepIntoOver());
		System.out.println("[StepIntoSpecific]   : " + analyzer.getStepIntoSpecific());
		System.out.println("[Monitor]            : " + analyzer.getMonitors());
		System.out.println("[StepOut]            : " + analyzer.getStepOut());
		System.out.println("[RuntoCursor]        : " + analyzer.getRuntoCursor());
		System.out.println("[Add Watch]          : " + analyzer.getAddWatch());
	}

}
