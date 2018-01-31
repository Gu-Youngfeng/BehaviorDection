package cstar.yongfeng.collect;

public class UsageGetter {
	
	private Collector collectorDebugger;
	
	private Collector collectorWorkTime;
	
	private Analyzer analyzerDebugger;
	
	private Analyzer analyzerWorkTime;
	
	public UsageGetter(String path){
		
		collectorDebugger = new Collector(path, CollectType.DebugTime);
		
		collectorWorkTime = new Collector(path, CollectType.WorkTime);
		
		analyzerDebugger = new Analyzer(collectorDebugger.getlslsEvent());
		
		analyzerWorkTime = new Analyzer(collectorWorkTime.getlslsEvent());
	}
	
	public void getUsage(){
		
		System.out.println("Debugging            :" + analyzerDebugger.getStreamTimes());
		System.out.println("[Breakpoint]         : " + analyzerDebugger.getBreakpoint());
		System.out.println("[Restart Debugging]  : " + analyzerDebugger.getRestart());
		System.out.println("[StepInto & StepOver]: " + analyzerDebugger.getStepIntoOver());
		System.out.println("[StepIntoSpecific]   : " + analyzerDebugger.getStepIntoSpecific());
		System.out.println("[Monitor]            : " + analyzerDebugger.getMonitors());
		System.out.println("[StepOut]            : " + analyzerDebugger.getStepOut());
		System.out.println("[RuntoCursor]        : " + analyzerDebugger.getRuntoCursor());
		System.out.println("[Add Watch]          : " + analyzerDebugger.getAddWatch());
		
		System.out.println("[Editing]            : " + analyzerDebugger.getEditing());
		System.out.println("[Break Condition    ]: " + analyzerDebugger.getBreakCondition());
		System.out.println("[Execution Changed]  : " + analyzerDebugger.getExecutionChanged());
		System.out.println("[OutofScope]         : " + analyzerDebugger.getOutScope());
		System.out.println("[Break at Handled]   : " + analyzerDebugger.getBreakException());
		System.out.println("[MultiThreads View]  : " + analyzerDebugger.getMultiThread());
		System.out.println("[Obeserving Performs]: " + analyzerWorkTime.getPerformance());
		System.out.println("[Set Next Statement] : " + analyzerDebugger.getSetNextStatement());
	}

}
