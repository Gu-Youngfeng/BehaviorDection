package cstar.yongfeng.collect;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.kave.commons.model.events.CommandEvent;
import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.visualstudio.DebuggerEvent;
import cc.kave.commons.model.events.visualstudio.DebuggerMode;
import cc.kave.commons.model.events.visualstudio.SolutionAction;
import cc.kave.commons.model.events.visualstudio.SolutionEvent;
import cc.kave.commons.model.events.visualstudio.WindowAction;
import cc.kave.commons.model.events.visualstudio.WindowEvent;

/***
 * <p>This class <b>Analyzer</b> provides the function to calculate the debugging operations
 * of debugging in Visual Studio. Including the <b>Debugging Common Sences, Debugging Foundations, Debugging Tricks</b>.</p>
 * <p>To instantiate an object of Analyzer initialized with the parameter, lslsEvent, list of event stream. 
 * </p>
 */
public class Analyzer {

	/** list of event stream */
	private List<ArrayList<IDEEvent>> lslsEvents;
	
	public Analyzer(List<ArrayList<IDEEvent>> ls){
		if(ls == null){
			System.out.println("[ERROR]: Developers' list of event stream can not be NULL!");
			return;
		}
//		this.lslsEvents = new ArrayList<ArrayList<IDEEvent>>(ls);
		this.lslsEvents = ls;
	}
		
	/////////////////////////////////
	// Common Sences
	
	/** Feature 17: To get the stream times of whole event streams.*/
	public int getStreamTimes(){
		return this.lslsEvents.size();
	}
	
	/** To get time duration between two event.
	 * @return duration in million seconds.
	 * */
	public long getDurationBy(IDEEvent eventStart, IDEEvent eventEnd){
		long duration = 0l;
		
		Date dateStart = Date.from(eventStart.getTriggeredAt().toInstant());
		Date dateEnd = Date.from(eventEnd.getTriggeredAt().toInstant());
		
		long longStart = dateStart.getTime();
		long longEnd = dateEnd.getTime();
		duration = (longEnd - longStart);
		
//		System.out.println("[start]:" + eventStart.getTriggeredAt().toString() + "[end]:" + eventEnd.getTriggeredAt().toString());
//		System.out.println("[start]:" + dateStart.getTime() + "[end]:" + dateEnd.getTime());
//		System.out.println("[duration]:" + duration + "\n");
		
		return duration;
	}
	
	/**
	 * <p>Feature 18/19: To get the duration time of whole event streams.</p>
	 * @return duration time in minutes
	 */
	public float getStreamDuration(){
		long tim = 0l;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
//			System.out.println("[stream size]:" + lsStream == null?0:lsStream.size());
			int lenStream = lsStream.size();
			IDEEvent eventStart = lsStream.get(0);
			IDEEvent eventEnd = lsStream.get(lenStream-1);
			long deltTime = getDurationBy(eventStart, eventEnd);
			tim += deltTime;
		}
		float sumTime = tim *(1.0f)/(1000*60*1.0f);
		return sumTime;
	}
	
	/////////////////////////////////
	// Debugging Foundations
	
	/**
	 * <p>Feature 1: To calculate the number of using breakpoint.</p>
	 * @return breakpoint usage time
	 * @BUG If the developer sets breakpoint by clicking and the program did not stop at the breakpoint,
	 *      the operation <b>CAN NOT</b> be captured.
	 */
	public int getBreakpoint(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);
				if(event instanceof CommandEvent){
					/** insert breakpoint by using right click menu */
					boolean flag1 = ((CommandEvent)event).getCommandId().contains(":375:EditorContextMenus.CodeWindow.Breakpoint.InsertBreakpoint");
					/** insert breakpoint by pressing shortcut F9 */
					boolean flag2 = ((CommandEvent)event).getCommandId().contains(":255:Debug.ToggleBreakpoint");
					
					if( flag1 || flag2){
						count++;
//						System.out.println("[time]:" + event.TriggeredAt.toString());
						break;
					}
				}else if(event instanceof DebuggerEvent){
					/** program stops at the breakpoint */
					boolean flag3 = (((DebuggerEvent)event).Mode==DebuggerMode.Break) && (((DebuggerEvent)event).Reason.equals("dbgEventReasonBreakpoint"));
					
					if( flag3 ){
						count++;
//						System.out.println("[time]:" + event.TriggeredAt.toString());
						break;
					}
				}else{
					continue;
				}
			}
		}
		
		return count;
	}
	
	/**
	 * <p>Feature 2: To calculate the number of using restart.</p>
	 * @return restart usage time
	 */
	public int getRestart(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);
				if(event instanceof CommandEvent){
					/** restart debugging in the original debugging process */
					boolean flag1 = ((CommandEvent)event).getCommandId().contains(":296:Debug.Restart");
					
					if(flag1){
						count++;
						break;
					}
				}
			}
			
		}
		
		return count;
	}
	
	/**
	 * <p>Feature 3: To calculate the number of using StepInto and StepOver.</p>
	 * @return StepInto, StepOver usage time
	 */
	public int getStepIntoOver(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);

			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);
				if(event instanceof CommandEvent){
					/** stepInto operation in debugging process */
					boolean flag1 = ((CommandEvent)event).getCommandId().contains(":248:Debug.StepInto");
					/** stepOver operation in debugging process */
					boolean flag2 = ((CommandEvent)event).getCommandId().contains(":249:Debug.StepOver");
					
					if( flag1 || flag2){
						count++;
						break;
					}
				}
			}
			
			
		}
		
		return count;
	}
	
	/**
	 * <p>Feature 4: To calculate the number of using StepIntoSpecific.</p>
	 * @return StepIntoSpecific usage time
	 */
	public int getStepIntoSpecific(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);

			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);

				if(event instanceof DebuggerEvent && j>0){
					DebuggerEvent de = (DebuggerEvent) event;
					if(de.Mode == DebuggerMode.Run && de.Reason.equals("dbgEventReasonStep")){
						IDEEvent eventbefore = lsStream.get(j-1);
						if(eventbefore instanceof CommandEvent){
							CommandEvent ce = (CommandEvent) eventbefore;
							boolean f1 = !ce.getCommandId().contains("VsAction:1:Debug");
							boolean f2 = !ce.getCommandId().contains("Step Into");
							boolean f3 = !ce.getCommandId().contains("}:");
							boolean f4 = !ce.getCommandId().contains("Step Over");
							boolean f5 = !ce.getCommandId().contains("Step Out");
							
							if( f1 && f2 && f3 && f4 && f5 ){
								count++;
								break;
							}
						}
					}
				}		
				
			}
			
		}
		
		return count;
	}
	
	/**
	 * <p>Feature 5: To calculate the number of using Monitor Windows to check the value of variables.</p>
	 * <p>Monitor Windows includes <b>Auto, Locals, Watch, Call Stack, Breakpoints, Exception Settings, Command Window, Immediate Window, Diagnostic Tools</b></p>
	 * @return Monitor Windows usage time 
	 */
	public int getMonitors(){
		int count=0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);

				if(event instanceof WindowEvent){
					WindowEvent we = (WindowEvent)event;
					
					/** activate the following 9 monitoring windows*/
					boolean flag1 = (we.Action == WindowAction.Activate) && we.Window.getCaption().contains("Auto");
					boolean flag2 = (we.Action == WindowAction.Activate) && we.Window.getCaption().contains("Locals");
					boolean flag3 = (we.Action == WindowAction.Activate) && we.Window.getCaption().contains("Watch 1");
					boolean flag4 = (we.Action == WindowAction.Activate) && we.Window.getCaption().contains("Call Stack");
					boolean flag5 = (we.Action == WindowAction.Activate) && we.Window.getCaption().contains("Breakpoints");
					boolean flag6 = (we.Action == WindowAction.Activate) && we.Window.getCaption().contains("Exception Settings");
					boolean flag7 = (we.Action == WindowAction.Activate) && we.Window.getCaption().contains("Command Window");
					boolean flag8 = (we.Action == WindowAction.Activate) && we.Window.getCaption().contains("Immediate Window");
					boolean flag9 = (we.Action == WindowAction.Activate) && we.Window.getCaption().contains("Diagnostic Tools");
	
					if(flag1 || flag2 || flag3 || flag4 || flag5 || flag6 || flag7 || flag8 || flag9){ // activate one of them
						count++;
//						System.out.println("[window]: " + we.Window.getCaption());
						break;
					}

				}	
				
			}
		}
		
		return count;
	}
	
	/**
	 * <p>Feature 6: To calculate the number of using operation <b>StepOut</b> (Shift + F11).</p>
	 * @return StepOut usage time 
	 */
	public int getStepOut(){
		int count=0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);

				if(event instanceof CommandEvent){
					CommandEvent ce = (CommandEvent)event;
					/** stepOut operation*/
					boolean flag1 = ce.getCommandId().contains(":250:Debug.StepOut");
	
					if( flag1 ){
						count++;
//						System.out.println("[command]: " + ce.getCommandId());
						break;
					}

				}	
				
			}
		}
		
		return count;
	}
	
	/**
	 * <p>Feature 7: To calculate the number of using operation <b>Run To Cursor</b>.</p>
	 * <p>Note that the operation can be conducted <b>BEFORE</b> or <b>DURING</b> the debugging process, 
	 * 
	 * </p>
	 * @return Run to cursor operations usage time
	 */
	public int getRuntoCursor(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);

				if(event instanceof CommandEvent){
					CommandEvent ce = (CommandEvent)event;
					/** run to cursor operation*/
					boolean flag1 = ce.getCommandId().contains(":251:Debug.RunToCursor");
	
					if( flag1 ){
						count++;
//						System.out.println("[command]: " + ce.getCommandId());
						break;
					}

				}else{
					continue;
				}	
				
			}
		}
		
		return count;
	}
	
	/**
	 * <p>Feature 8: To calculate the number of using operation <b>Add to Watch</b>.</p>
	 * @return Run to cursor operations usage time
	 */
	public int getAddWatch(){
		int count=0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);

				if(event instanceof CommandEvent){
					CommandEvent ce = (CommandEvent)event;
					/** run to cursor operation*/
					boolean flag1 = ce.getCommandId().contains(":252:Debug.AddWatch");
	
					if( flag1 ){
						count++;
//						System.out.println("[command]: " + ce.getCommandId());
						break;
					}

				}else{
					continue;
				}	
				
			}
		}
		
		return count;
	}
	
	/////////////////////////////////
	// Debugging Tricks
	
	/**
	 * <p>Feature 9: To calculate the number of using operation <b>Editing</b>.</p>
	 * @return Editing usage time
	 */
	public int getEditing(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);

				if(event instanceof CommandEvent){
					CommandEvent ce = (CommandEvent)event;
					/** editing during the debugging */
					boolean flag1 = ce.getCommandId().contains("VsAction:1:Edit");
	
					if( flag1 ){
						count++;
//						System.out.println("[command]: " + ce.getCommandId());
						break;
					}

				}else{
					continue;
				}	
				
			}
		}
		
		return count;
	}
	
	/**
	 * <p>Feature 10: To calculate the number of using operation <b>Breakpoint Condition Setting</b>.</p>
	 * @return Breakpoint Condition Setting usage time
	 */
	public int getBreakCondition(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);

				if(event instanceof CommandEvent){
					CommandEvent ce = (CommandEvent)event;
					/** setting Breakpoint Condition  */
					boolean flag1 = ce.getCommandId().contains("Conditions...");
					boolean flag2 = ce.getCommandId().contains(":320:EditorContextMenus.CodeWindow.Breakpoint.BreakpointConditions");
	
					if( flag1 || flag2){
						count++;
//						System.out.println("[command]: " + ce.getCommandId());
						break;
					}

				}else{
					continue;
				}	
				
			}
		}
		
		return count;
	}
	
	/**
	 * <p>Feature 11: To calculate the number of using operation <b>Changing Execution Stream</b>.</p>
	 * @return Execution changing usage time
	 * TODO: bugs
	 */
	public int getExecutionChanged(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);

			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);
//				System.out.println("[j]    :" + (j));
				if(event instanceof DebuggerEvent && j>0){
					DebuggerEvent de = (DebuggerEvent) event;
					if(de.Mode == DebuggerMode.Run && de.Reason.equals("dbgEventReasonGo")){
//						System.out.println("[event]:" + de.toString());
//						System.out.println("[j-1]  :" + (j-1));
						IDEEvent eventbefore = lsStream.get(j-1);
						if(eventbefore instanceof CommandEvent){
							CommandEvent ce = (CommandEvent) eventbefore;
							if(ce.getCommandId().contains(":295:Debug.Start") || ce.getCommandId().contains(":Debug.Start")){
								count++;
								break;
							}
							
						}
					}
					
				}								
			}
		}
		
		return count;
	}
	
	/**
	 * <p>Feature 12: To calculate the number of using operation <b>Tracking out-scope objects</b>.</p>
	 * @return tracking usage time
	 */
	public int getOutScope(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			int flag1 = 0;
			int flag2 = 0;
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);			

				if(event instanceof CommandEvent){
					CommandEvent ce = (CommandEvent)event;
					if( ce.getCommandId().contains("Make Object ID") || ce.getCommandId().contains(":327:DebuggerContextMenus.AutosWindow.MakeObjectID")){
						flag1++;
					}else if( ce.getCommandId().contains("Add Watch") ||  ce.getCommandId().contains(":252:Debug.AddWatch")){
						flag2++;
					}

				}else{
					continue;
				}	
			}
			if(flag1 >= 1 && flag2 >= 1){
				count++;
			}
		}	
		return count;
	}
	
	/**
	 * <p>Feature 13: To calculate the number of using operation <b>Breaking at an Handled Exception</b>.</p>
	 * @return break at an handled exception usage time
	 */
	public int getBreakException(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
//			int flag1 = 0;
//			int flag2 = 0;
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);			

				if(event instanceof CommandEvent){
					CommandEvent ce = (CommandEvent)event;
					if(ce.getCommandId().contains("Exception Settings") || ce.getCommandId().contains(":339:Debug.ExceptionSettings")){
//						flag1++;
						count++;
						break;
					}
//				}else if(event instanceof DebuggerEvent){
//					DebuggerEvent de = (DebuggerEvent)event;
//					if(de.Mode == DebuggerMode.Run && de.Reason.equals("dbgEventReasonExceptionThrown")){
//						flag2++;
//					}
				}else{
					continue;
				}	
			}
//			if(flag1 >= 1 && flag2 >= 1){
//				count++;
//			}
		}	
		return count;
	}
	
	/**
	 * <p>Feature 14: To calculate the number of using operation <b>Show Threads in Source</b>.</p>
	 * @return Show Threads in Source usage time
	 */
	public int getMultiThread(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);			

				if(event instanceof CommandEvent){
					CommandEvent ce = (CommandEvent)event;
					/** press the button of Show Threads in Source*/
					boolean flag1 = ce.getCommandId().contains("Show Threads in Source") || 
							ce.getCommandId().contains(":346:DebuggerContextMenus.GPUThreadsWindowShortcutMenu.Debug.LocationToolbar.ShowThreadIpIndicators");
					if( flag1 ){
						count++;
						break;
					}
				}else{
					continue;
				}	
			}
		}	
		return count;
	}
	
	/**
	 * <p>Feature 15: To calculate the number of using operation <b>Performance Observation</b>.</p>
	 * <p>Note: Performance Observation CANNOT triggered <b>DebuggerEvent</b>. 
	 * So we have to search this operation in the <b>whole event stream</b> RATHER THAN in <b>debugging stream</b>.</p>
	 * @return observe performance time
	 * @deprecated Instead by function Collector.getPerformance()
	 */
	public int getPerformance(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			int f1 = 0;
			int f2 = 0;
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);			

				if(event instanceof CommandEvent){
					CommandEvent ce = (CommandEvent)event;
					/** press the button of Show Threads in Source*/
					boolean flag1 = ce.getCommandId().contains("Performance Profiler...") || 
							ce.getCommandId().contains(":775:Debug.DiagnosticsHub.Launch");
					if( flag1 ){
						f1++;
					}
				}else if(event instanceof SolutionEvent){
					SolutionEvent se = (SolutionEvent)event;
					
					boolean flag2 = se.Target.getIdentifier().contains("Report20180122‐2246.diagsession") && 
							se.Action == SolutionAction.AddProjectItem;
					if( flag2 ){
						f2++;
					}
				}else{
					continue;
				}	
			}
			
			if(f2 >= 1 && f1 >= 1){
				count++;
			}
		}	
		return count;
	}
	
	/**
	 * <p>Feature 16: To calculate the number of using operation <b>Performance Observation</b>.</p>
	 * <p></p>
	 * @return observe performance time
	 */
	public int getSetNextStatement(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);			

				if(event instanceof CommandEvent){
					CommandEvent ce = (CommandEvent)event;
					/** press the button of Show Threads in Source*/
					boolean flag1 = ce.getCommandId().contains("Set Next Statement") || 
							ce.getCommandId().contains(":258:Debug.SetNextStatement");
					if( flag1 ){
						count++;
						break;
					}
				}else{
					continue;
				}	
			}
		}	
		return count;
	}
	
	/**
	 * <p>To get the unexpected exception break</p>
	 * @return
	 */
	public int getFialedDebugging(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);			

				if(event instanceof DebuggerEvent){
					DebuggerEvent de = (DebuggerEvent)event;

					boolean flag1 = (de.Mode == DebuggerMode.Break &&
							de.Reason.equals("dbgEventReasonExceptionNotHandled"));
					if( flag1 ){
						count++;
						break;
					}
				}	
			}
		}	
		return count;
	}
	
	/**
	 * <p>To get the successful debugging</p>
	 * @return
	 */
	public int getEndDebugging(){
		int count = 0;
		
		for(int i=0; i<lslsEvents.size(); i++){ // for each stream
			ArrayList<IDEEvent> lsStream = lslsEvents.get(i);
			for(int j=0; j<lsStream.size(); j++){ // for each event
				IDEEvent event = lsStream.get(j);			

				if(event instanceof DebuggerEvent){
					DebuggerEvent de = (DebuggerEvent)event;

					boolean flag1 = (de.Mode == DebuggerMode.Design &&
							de.Reason.equals("dbgEventReasonEndProgram"));
					if( flag1 ){
						count++;
						break;
					}
				}	
			}
		}	
		return count;
	}
}
