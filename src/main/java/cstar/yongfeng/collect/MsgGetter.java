package cstar.yongfeng.collect;

import java.time.ZonedDateTime;

import cc.kave.commons.model.events.CommandEvent;
import cc.kave.commons.model.events.ErrorEvent;
import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.InfoEvent;
import cc.kave.commons.model.events.NavigationEvent;
import cc.kave.commons.model.events.SystemEvent;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.testrunevents.TestCaseResult;
import cc.kave.commons.model.events.testrunevents.TestRunEvent;
import cc.kave.commons.model.events.versioncontrolevents.VersionControlEvent;
import cc.kave.commons.model.events.visualstudio.BuildEvent;
import cc.kave.commons.model.events.visualstudio.DebuggerEvent;
import cc.kave.commons.model.events.visualstudio.DocumentEvent;
import cc.kave.commons.model.events.visualstudio.EditEvent;
import cc.kave.commons.model.events.visualstudio.FindEvent;
import cc.kave.commons.model.events.visualstudio.IDEStateEvent;
import cc.kave.commons.model.events.visualstudio.SolutionEvent;
import cc.kave.commons.model.events.visualstudio.WindowEvent;

public class MsgGetter {
	
	private IDEEvent event;
	
	MsgGetter(IDEEvent e){
		this.event = e;
	}
	
	/**
	 * <p>To get the detailed information of the IDEEvent.</p>
	 * @return typeID a description string of the event
	 */
	public String getInfo(){
		
		String eventType = this.event.getClass().getSimpleName();
		ZonedDateTime triggeredTime = this.event.getTriggeredAt();
		String typeBasic = "### " + eventType + " | " + triggeredTime.toString() + "\n";
		String typeID = "";
		
		switch(eventType){
		case"ActivityEvent":
			typeID = "EMPTY\n\n";
			break;
		case"CommandEvent":
			CommandEvent ce = (CommandEvent) this.event;
			typeID = processCommand(ce);
			break;
		case"CompletionEvent":
			CompletionEvent cpe = (CompletionEvent) this.event;
			typeID = processCompletion(cpe);
			break;
		case"BuildEvent":
			BuildEvent be = (BuildEvent) this.event;
			typeID = processBuild(be);
			break;
		case"DebuggerEvent":
			DebuggerEvent de = (DebuggerEvent) this.event;
			typeID = processDebugger(de);
			break;
		case"DocumentEvent":
			DocumentEvent dce = (DocumentEvent) this.event;
			typeID = processDocument(dce);
			break;
		case"EditEvent":
			EditEvent ee = (EditEvent) this.event;
			typeID = processEdit(ee);
			break;
		case"FindEvent":
			FindEvent fe = (FindEvent) this.event;
			typeID = processFind(fe);
			break;
		case"IDEStateEvent":
			IDEStateEvent ie = (IDEStateEvent) this.event;
			typeID = processIDEState(ie);
			break;
		case"SolutionEvent":
			SolutionEvent se = (SolutionEvent) this.event;
			typeID = processSolution(se);
			break;
		case"WindowEvent":
			WindowEvent we = (WindowEvent) this.event;
			typeID = processWindow(we);
			break;
		case"VersionControlEvent":
			VersionControlEvent ve = (VersionControlEvent) this.event;
			typeID = processVersionControl(ve);
			break;
		case"UserProfileEvent":
			typeID = "EMPTY\n\n";
			break;
		case"NavigationEvent":
			NavigationEvent ne = (NavigationEvent) this.event;
			typeID = processNavigation(ne);
			break;
		case"SystemEvent":
			SystemEvent sye = (SystemEvent) this.event;
			typeID = processSystem(sye);
			break;
		case"TestRunEvent":
			TestRunEvent tre = (TestRunEvent) this.event;
			typeID = processTestRun(tre);
			break;
		case"InfoEvent":
			InfoEvent ife = (InfoEvent) this.event;
			typeID = processInfo(ife);
			break;
		case"ErrorEvent":
			ErrorEvent eee = (ErrorEvent) this.event;
			typeID = processError(eee);
			break;
		case"InstallEvent":
			typeID = "EMPTY\n\n";
			break;
		case"UpdateEvent":
			typeID = "EMPTY\n\n";
			break;
		default:
			System.out.println(":" + this.event.getClass().getSimpleName());
			break;
		}
		
		String typeInfo = typeBasic + typeID;
		
		return typeInfo;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
    ////  Event Processors
	//
	/////////////////////////////
	
	private static String processCommand(CommandEvent e){ 
		return "[command]:" + e.getCommandId() + "\n\n";
	}
	
	private static String processCompletion(CompletionEvent e){
		return "[count]:" + e.getProposalCount() + ", [terminate]:" + e.getTerminatedState() + "\n\n";
	}
	
	private static String processBuild(BuildEvent e){
		return "[action]:" + e.Action + ", [scope]:" + e.Scope + "\n\n";
	}
	
	private static String processDebugger(DebuggerEvent e){
		return "[mode]:" + e.Mode + ", [reason]:" + e.Reason + "\n\n";
	}
	
	private static String processDocument(DocumentEvent e){
		return "[name]:" + e.Document + ", [action]:" + e.Action + "\n\n";
	}
	
	private static String processEdit(EditEvent e){
		return "[changeNum]:" + e.NumberOfChanges + ", [changeSize]:" + e.SizeOfChanges + "\n\n";
	}
	
	private static String processFind(FindEvent e){
		return "[find]:" + e.Cancelled + "\n\n";
	}
	
	private static String processSolution(SolutionEvent e){
		return "[action]:" + e.Action + ", [target]:" + e.Target.getIdentifier() + "\n\n";
	}
	
	private static String processNavigation(NavigationEvent e){
		return "[type]:" + e.TypeOfNavigation + ", [target]:" + e.Target.getIdentifier() + "\n\n";
	}

	private static String processTestRun(TestRunEvent e){
		String context = "";
		context += "[abort]:" + e.WasAborted + "\n";
		if(e.Tests!=null){
			for(TestCaseResult test: e.Tests){
				context += test.toString() + "\n";
			}
		}else{
			context += "-- No Test --\n";
		}
		
		return context + "\n";
	}
	
	private static String processIDEState(IDEStateEvent e){
		String context = "[lifeCycle]:" + e.IDELifecyclePhase + "\n";
		if(e.OpenWindows!=null){
			for(int i=0; i<e.OpenWindows.size(); i++){
				context += e.OpenWindows.get(i).getIdentifier() + "\n";
			}
		}else{
			context += "-- No Window --\n";
		}
		
		return context + "\n";
	}
	
	private static String processWindow(WindowEvent e){
		return "[window]:" + e.Window.getIdentifier() + ", [action]:" + e.Action + "\n\n";
	}
	
	private static String processVersionControl(VersionControlEvent e){
		String context = "[solution]:" + e.Solution.getIdentifier() + "\n";
		if(e.Actions!=null){
			for(int i=0; i<e.Actions.size(); i++){
				context += e.Actions.get(i).ExecutedAt + " | " + e.Actions.get(i).ActionType + "\n";
			}
		}else{
			context += "-- No Commit --\n";
		}
		
		return context + "\n";
	}
	
	private static String processInfo(InfoEvent e){
		return "[information]:" + e.Info + "\n\n";
	}
	
	private static String processError(ErrorEvent e){
		String context = "[content]:" + e.Content + "\n";
		if(e.StackTrace.length > 0){
			for(int i=0; i<e.StackTrace.length; i++){
				context += e.StackTrace[i] + "\n";
			}
		}else{
			context += "-- No Stack Trace --\n";
		}
		
		return context + "\n";
	}
	
	private static String processSystem(SystemEvent e){
		return "[information]:" + e.Type + "\n\n";
	}

}
