package cstar.yongfeng.collect;

/***
 * <p>Class <b>UsageGetter</b> provides the interface of accessing the metrics in developers' debugging process.
 * The function {@link#getMtric()} will return the metric values of one developer. The <b>19</b> metrics are,</p> 
 *
 * <p><b>1. Debugging Foundations</b> <li> debugBreakpoint, debugRestart, debugStepIO, debugStepSP, 
 * debugMonitor, debugStepOUT, debugRunCursor, debugAddWatch </li></p>
 * <p><b>2. Debugging Tricks</b> <li> debugEditing, debugBreakCondition, debugExeChanged, debugOutScope, 
 * debugBreakException, debugMultiThread, debugPerformance, debugNextStatement </li></p>
 * <p><b>3. Debugging Common Sense</b> <li> debugTimes, workTime, debugTime </li></p>
 */
public class UsageGetter {
	
//	private Collector collectorDebugger; // to get out of the risk of memory leak
//	
//	private Collector collectorWorkTime;
	
	private Analyzer analyzerDebugger;
	
//	private Analyzer analyzerWorkTime;
	
	private float[] attributes;
	
	/**
	 * <p>UsageGetter constructor.</p>
	 * @param path developers' zip data path.
	 */
	public UsageGetter(String path, CollectType type){
		
		Collector collectorDebugger = new Collector(path, type);
		analyzerDebugger = new Analyzer(collectorDebugger.getlslsEvent());
		
		if(type == CollectType.DebugTime){			
			getDebugUsage();
		}else if(type == CollectType.WorkTime){
			getTotalUsage();
		}else{
			return;
		}
	}
	
	/** To get the 17 metrics*/
	private void getDebugUsage(){
		
		/** Print in Console Windows */
//		System.out.println("Debugging            :" + analyzerDebugger.getStreamTimes());
//		System.out.println("[Breakpoint]         : " + analyzerDebugger.getBreakpoint());
//		System.out.println("[Restart Debugging]  : " + analyzerDebugger.getRestart());
//		System.out.println("[StepInto & StepOver]: " + analyzerDebugger.getStepIntoOver());
//		System.out.println("[StepIntoSpecific]   : " + analyzerDebugger.getStepIntoSpecific());
//		System.out.println("[Monitor]            : " + analyzerDebugger.getMonitors());
//		System.out.println("[StepOut]            : " + analyzerDebugger.getStepOut());
//		System.out.println("[RuntoCursor]        : " + analyzerDebugger.getRuntoCursor());
//		System.out.println("[Add Watch]          : " + analyzerDebugger.getAddWatch());
//		
//		System.out.println("[Editing]            : " + analyzerDebugger.getEditing());
//		System.out.println("[Break Condition    ]: " + analyzerDebugger.getBreakCondition());
//		System.out.println("[Execution Changed]  : " + analyzerDebugger.getExecutionChanged());
//		System.out.println("[OutofScope]         : " + analyzerDebugger.getOutScope());
//		System.out.println("[Break at Handled]   : " + analyzerDebugger.getBreakException());
//		System.out.println("[MultiThreads View]  : " + analyzerDebugger.getMultiThread());
//		System.out.println("[Obeserving Performs]: " + analyzerWorkTime.getPerformance());
//		System.out.println("[Set Next Statement] : " + analyzerDebugger.getSetNextStatement());
		
		/** Print in vector pattern */
		int debugTimes = analyzerDebugger.getStreamTimes();
//		float workTime = analyzerWorkTime.getStreamDuration(); // see getTotalUsage()
		float debugTime = analyzerDebugger.getStreamDuration();
		
		int debugBreakpoint = analyzerDebugger.getBreakpoint();
		int debugRestart = analyzerDebugger.getRestart();
		int debugStepIO = analyzerDebugger.getStepIntoOver();
		int debugStepSP = analyzerDebugger.getStepIntoSpecific();
		int debugMonitor = analyzerDebugger.getMonitors();
		int debugStepOUT = analyzerDebugger.getStepOut();
		int debugRunCursor = analyzerDebugger.getRuntoCursor();
		int debugAddWatch = analyzerDebugger.getAddWatch();
		
		int debugEditing = analyzerDebugger.getEditing();
		int debugBreakCondition = analyzerDebugger.getBreakCondition();
		int debugExeChanged = analyzerDebugger.getExecutionChanged();
		int debugOutScope = analyzerDebugger.getOutScope();
		int debugBreakException = analyzerDebugger.getBreakException();
		int debugMultiThread = analyzerDebugger.getMultiThread();
//		int debugPerformance = analyzerWorkTime.getPerformance(); // see getTotalUsage()
		int debugNextStatement = analyzerDebugger.getSetNextStatement();
		
		this.attributes = new float[]{debugBreakpoint, debugRestart, debugStepIO, debugStepSP, 
				debugMonitor, debugStepOUT, debugRunCursor, debugAddWatch,
				debugEditing, debugBreakCondition, debugExeChanged, debugOutScope, 
				debugBreakException, debugMultiThread, 
//				debugPerformance, 
				debugNextStatement,
				
				debugTimes, 
//				workTime, 
				debugTime
				};	
		
	}
	
	/** To get the 2 metrics*/
	private void getTotalUsage(){
		/** Print in Console Windows */
//		System.out.println("Debugging            :" + analyzerDebugger.getStreamTimes());
//		System.out.println("[Breakpoint]         : " + analyzerDebugger.getBreakpoint());
//		System.out.println("[Restart Debugging]  : " + analyzerDebugger.getRestart());
//		System.out.println("[StepInto & StepOver]: " + analyzerDebugger.getStepIntoOver());
//		System.out.println("[StepIntoSpecific]   : " + analyzerDebugger.getStepIntoSpecific());
//		System.out.println("[Monitor]            : " + analyzerDebugger.getMonitors());
//		System.out.println("[StepOut]            : " + analyzerDebugger.getStepOut());
//		System.out.println("[RuntoCursor]        : " + analyzerDebugger.getRuntoCursor());
//		System.out.println("[Add Watch]          : " + analyzerDebugger.getAddWatch());
//		
//		System.out.println("[Editing]            : " + analyzerDebugger.getEditing());
//		System.out.println("[Break Condition    ]: " + analyzerDebugger.getBreakCondition());
//		System.out.println("[Execution Changed]  : " + analyzerDebugger.getExecutionChanged());
//		System.out.println("[OutofScope]         : " + analyzerDebugger.getOutScope());
//		System.out.println("[Break at Handled]   : " + analyzerDebugger.getBreakException());
//		System.out.println("[MultiThreads View]  : " + analyzerDebugger.getMultiThread());
//		System.out.println("[Obeserving Performs]: " + analyzerWorkTime.getPerformance());
//		System.out.println("[Set Next Statement] : " + analyzerDebugger.getSetNextStatement());
		
		/** Print in vector pattern */
//		int debugTimes = analyzerDebugger.getStreamTimes();
		float workTime = analyzerDebugger.getStreamDuration(); // see getTotalUsage()
//		float debugTime = analyzerDebugger.getStreamDuration();
		
//		int debugBreakpoint = analyzerDebugger.getBreakpoint();
//		int debugRestart = analyzerDebugger.getRestart();
//		int debugStepIO = analyzerDebugger.getStepIntoOver();
//		int debugStepSP = analyzerDebugger.getStepIntoSpecific();
//		int debugMonitor = analyzerDebugger.getMonitors();
//		int debugStepOUT = analyzerDebugger.getStepOut();
//		int debugRunCursor = analyzerDebugger.getRuntoCursor();
//		int debugAddWatch = analyzerDebugger.getAddWatch();
		
//		int debugEditing = analyzerDebugger.getEditing();
//		int debugBreakCondition = analyzerDebugger.getBreakCondition();
//		int debugExeChanged = analyzerDebugger.getExecutionChanged();
//		int debugOutScope = analyzerDebugger.getOutScope();
//		int debugBreakException = analyzerDebugger.getBreakException();
//		int debugMultiThread = analyzerDebugger.getMultiThread();
//		int debugPerformance = analyzerDebugger.getPerformance(); // see getTotalUsage()
//		int debugNextStatement = analyzerDebugger.getSetNextStatement();
		
		this.attributes = new float[]{
//				debugBreakpoint, debugRestart, debugStepIO, debugStepSP, 
//				debugMonitor, debugStepOUT, debugRunCursor, debugAddWatch,
//				debugEditing, debugBreakCondition, debugExeChanged, debugOutScope, 
//				debugBreakException, debugMultiThread, 
//				debugPerformance, 
//				debugNextStatement,
//				
//				debugTimes, 
				workTime, 
//				debugTime
				};	
	}
	
	/**
	 * <p>To return the selected metrics in developers' debugging process.
	 * The detailed metric list see {@link UsageGetter}</p>
	 * @return
	 */
	public float[] getMetric(){
		return this.attributes;
	}
	
	/** To show the metric */
	public void showMetric(){
		for(float metric: this.attributes){
			System.out.print(metric + " ");
		}
	}

}
