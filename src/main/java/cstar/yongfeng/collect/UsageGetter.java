package cstar.yongfeng.collect;

/***
 * <p>Class <b>UsageGetter</b> provides the interface of accessing the metrics in developers' debugging process.
 * The function {@link#getMtric()} will return the metric values of one developer. The <b>21</b> metrics are,</p> 
 *
 * <p><b>1. Debugging Foundations</b> {8} <li> debugBreakpoint, debugRestart, debugStepIO, debugStepSP, 
 * debugMonitor, debugStepOUT, debugRunCursor, debugAddWatch </li></p>
 * <p><b>2. Debugging Tricks</b> {7} <li> debugEditing, debugBreakCondition, debugExeChanged, debugOutScope, 
 * debugBreakException, debugMultiThread, debugNextStatement </li></p>
 * <p><b>3. Debugging Common Sense</b> {6} <li> debugTimes, debugTime, workTime, workDay, failedDebugTimes, debugPerformance, </li></p>
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
	public UsageGetter(String path){
		
		Collector collectorDebugger = new Collector(path, CollectType.DebugTime);
		analyzerDebugger = new Analyzer(collectorDebugger.getlslsEvent());
				
		getUsage(path);
	}
	
	/** To get the 17 metrics*/
	private void getUsage(String path){
		
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
		float debugTime = analyzerDebugger.getStreamDuration();
		float workTime = Collector.getInIDETime(Collector.searchEventFile(path));		
		int workDay = Collector.getDevelopDays(Collector.searchEventFile(path));
		int failedDebugTimes = analyzerDebugger.getFialedDebugging();
//		int successDebugTimes = analyzerDebugger.getEndDebugging();
		int debugPerformance = Collector.getPerformance(Collector.searchEventFile(path));
		
		
		float debugBreakpoint;
		float debugRestart;
		float debugStepIO;
		float debugStepSP;
		float debugMonitor;
		float debugStepOUT;
		float debugRunCursor;
		float debugAddWatch;
		
		float debugEditing;
		float debugBreakCondition;
		float debugExeChanged;
		float debugOutScope;
		float debugBreakException;
		float debugMultiThread;
		float debugNextStatement;
		
		if(debugTimes != 0){
			debugBreakpoint = (float) (analyzerDebugger.getBreakpoint()*1.0/debugTimes*1.0);
			debugRestart = (float) (analyzerDebugger.getRestart()*1.0/debugTimes*1.0);
			debugStepIO = (float) (analyzerDebugger.getStepIntoOver()*1.0/debugTimes*1.0);
			debugStepSP = (float) (analyzerDebugger.getStepIntoSpecific()*1.0/debugTimes*1.0);
			debugMonitor = (float) (analyzerDebugger.getMonitors()*1.0/debugTimes*1.0);
			debugStepOUT = (float) (analyzerDebugger.getStepOut()*1.0/debugTimes*1.0);
			debugRunCursor = (float) (analyzerDebugger.getRuntoCursor()*1.0/debugTimes*1.0);
			debugAddWatch = (float) (analyzerDebugger.getAddWatch()*1.0/debugTimes*1.0);
			
			debugEditing = (float) (analyzerDebugger.getEditing()*1.0/debugTimes*1.0);
			debugBreakCondition = (float) (analyzerDebugger.getBreakCondition()*1.0/debugTimes*1.0);
			debugExeChanged = (float) (analyzerDebugger.getExecutionChanged()*1.0/debugTimes*1.0);
			debugOutScope = (float) (analyzerDebugger.getOutScope()*1.0/debugTimes*1.0);
			debugBreakException = (float) (analyzerDebugger.getBreakException()*1.0/debugTimes*1.0);
			debugMultiThread = (float) (analyzerDebugger.getMultiThread()*1.0/debugTimes*1.0);
			debugNextStatement = (float) (analyzerDebugger.getSetNextStatement()*1.0/debugTimes*1.0);
		}else{
			debugBreakpoint = analyzerDebugger.getBreakpoint();
			debugRestart = analyzerDebugger.getRestart();
			debugStepIO = analyzerDebugger.getStepIntoOver();
			debugStepSP = analyzerDebugger.getStepIntoSpecific();
			debugMonitor = analyzerDebugger.getMonitors();
			debugStepOUT = analyzerDebugger.getStepOut();
			debugRunCursor = analyzerDebugger.getRuntoCursor();
			debugAddWatch = analyzerDebugger.getAddWatch();
			
			debugEditing = analyzerDebugger.getEditing();
			debugBreakCondition = analyzerDebugger.getBreakCondition();
			debugExeChanged = analyzerDebugger.getExecutionChanged();
			debugOutScope = analyzerDebugger.getOutScope();
			debugBreakException = analyzerDebugger.getBreakException();
			debugMultiThread = analyzerDebugger.getMultiThread();
			debugNextStatement = analyzerDebugger.getSetNextStatement();
		}
		
		this.attributes = new float[]{debugBreakpoint, debugRestart, debugStepIO, debugStepSP, 
				debugMonitor, debugStepOUT, debugRunCursor, debugAddWatch,
				
				debugEditing, debugBreakCondition, debugExeChanged, debugOutScope, 
				debugBreakException, debugMultiThread, debugNextStatement,
				
				debugTimes,  
				debugTime,
				workTime,
				workDay,
				failedDebugTimes,
				debugPerformance,
//				successDebugTimes
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
