package cstar.yongfeng.preprocess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cc.kave.commons.model.events.CommandEvent;
import cc.kave.commons.model.events.ErrorEvent;
import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.InfoEvent;
import cc.kave.commons.model.events.NavigationEvent;
import cc.kave.commons.model.events.SystemEvent;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.testrunevents.TestCaseResult;
import cc.kave.commons.model.events.testrunevents.TestRunEvent;
import cc.kave.commons.model.events.userprofiles.Likert7Point;
import cc.kave.commons.model.events.userprofiles.Positions;
import cc.kave.commons.model.events.userprofiles.UserProfileEvent;
import cc.kave.commons.model.events.versioncontrolevents.VersionControlEvent;
import cc.kave.commons.model.events.visualstudio.BuildEvent;
import cc.kave.commons.model.events.visualstudio.DebuggerEvent;
import cc.kave.commons.model.events.visualstudio.DocumentEvent;
import cc.kave.commons.model.events.visualstudio.EditEvent;
import cc.kave.commons.model.events.visualstudio.FindEvent;
import cc.kave.commons.model.events.visualstudio.IDEStateEvent;
import cc.kave.commons.model.events.visualstudio.SolutionEvent;
import cc.kave.commons.model.events.visualstudio.WindowEvent;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;
import utils.IoHelper;

/**
 * <p>ExtractEvents present how to unzip the zip files and extract all event ID in these zip files.</p>
 * <p>All event streams will be saved in a file in EVENT_SAVE_PATH directory.</p>
 */
public class ExtractEvents {
	
	final public static String EVENT_SAVE_PATH = "src/main/resources/total/";
	
	final public static String EVENT_SLICE_PATH = "src/main/resources/slice-id-1/";
	
	public static Set<String> lsComStr = new HashSet<String>();

	public String eventsDir;

	public ExtractEvents(String eventsDir) {
		this.eventsDir = eventsDir; // eventsDir is the root directory of events
	}

	public void extract() {

		/**
		 * Each .zip that is contained in the eventsDir represents all events that we
		 * have collected for a specific user, the folder represents the first day when
		 * the user uploaded data.
		 */
		Set<String> userZips = IoHelper.findAllZips(eventsDir); // find all zip file in root directory
		System.out.println("eventsDir  : " + eventsDir);
		System.out.println("zip numbers: " + userZips.size());
		for (String userZip : userZips) {
//			System.out.printf("\n#### processing user zip: %s #####\n", userZip); 
			processUserZip(userZip);
		}

//		/***** START ****/
//		for(String x: lsComStr){
//			System.out.println(x);
//			try {
//				writeSplitEvent("all-command-str.txt", x);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		/***** END ****/
		
	}

	/** <p>To deal with events from one user userZip</p>*/
	private void processUserZip(String userZip) {
		// open the .zip file ... 
		try (IReadingArchive ra = new ReadingArchive(new File(eventsDir, userZip))) {
			System.out.println("USER:" + userZip); // print each userZip directory

			while (ra.hasNext()) {

				IDEEvent e = ra.getNext(IDEEvent.class); // 下一条 IDEEvent 事件

				if(e != null){
					readEvent(e, userZip);
				}
			}
		}
		catch(Exception e){
			System.out.println("getNext() function is failed to get the next event!");
			e.printStackTrace();
		}
	}
	
	private String getTypeID(IDEEvent e){
		String eventType = e.getClass().getSimpleName();
		String typeID = "";
		switch(eventType){
		case"ActivityEvent":
			typeID = "EMPTY\n\n";
			break;
		case"CommandEvent":
			CommandEvent ce = (CommandEvent) e;
			typeID = processCommand(ce);
			break;
		case"CompletionEvent":
			CompletionEvent cpe = (CompletionEvent) e;
			typeID = processCompletion(cpe);
			break;
		case"BuildEvent":
			BuildEvent be = (BuildEvent) e;
			typeID = processBuild(be);
			break;
		case"DebuggerEvent":
			DebuggerEvent de = (DebuggerEvent) e;
			typeID = processDebugger(de);
			break;
		case"DocumentEvent":
			DocumentEvent dce = (DocumentEvent) e;
			typeID = processDocument(dce);
			break;
		case"EditEvent":
			EditEvent ee = (EditEvent) e;
			typeID = processEdit(ee);
			break;
		case"FindEvent":
			FindEvent fe = (FindEvent) e;
			typeID = processFind(fe);
			break;
		case"IDEStateEvent":
			IDEStateEvent ie = (IDEStateEvent) e;
			typeID = processIDEState(ie);
			break;
		case"SolutionEvent":
			SolutionEvent se = (SolutionEvent) e;
			typeID = processSolution(se);
			break;
		case"WindowEvent":
			WindowEvent we = (WindowEvent) e;
			typeID = processWindow(we);
			break;
		case"VersionControlEvent":
			VersionControlEvent ve = (VersionControlEvent) e;
			typeID = processVersionControl(ve);
			break;
		case"UserProfileEvent":
			typeID = "EMPTY\n\n";
			break;
		case"NavigationEvent":
			NavigationEvent ne = (NavigationEvent) e;
			typeID = processNavigation(ne);
			break;
		case"SystemEvent":
			SystemEvent sye = (SystemEvent) e;
			typeID = processSystem(sye);
			break;
		case"TestRunEvent":
			TestRunEvent tre = (TestRunEvent) e;
			typeID = processTestRun(tre);
			break;
		case"InfoEvent":
			InfoEvent ife = (InfoEvent) e;
			typeID = processInfo(ife);
			break;
		case"ErrorEvent":
			ErrorEvent eee = (ErrorEvent) e;
			typeID = processError(eee);
			break;
		case"InstallEvent":
			typeID = "EMPTY\n\n";
			break;
		case"UpdateEvent":
			typeID = "EMPTY\n\n";
			break;
		default:
			System.out.println(":" + e.getClass().getSimpleName());
			break;
		}
		
		return typeID;
	}

	/**
	 * <p> To read event type ID from event e, then write them into EVENT_PATH. 
	 * We collect all 20 event types.</p>
	 */
	private void readEvent(IDEEvent e, String userZip) {
		
		String eventLine = "### " + e.getClass().getSimpleName() + " | " + e.getTriggeredAt().toString() + "\n";
//		System.out.print(eventLine); // print
		String eventDetail = getTypeID(e);
//		System.out.print(eventDetail); // print
		/** TO DELET*/
//		if(typeID == 1){
//			CommandEvent ce = (CommandEvent) e;
//			System.out.println("[type]:" + ce.getClass().getSimpleName());
//			System.out.println("[line]:" + ce.CommandId);
//		}
//		if(e instanceof CommandEvent){
//			String cid = ((CommandEvent)e).getCommandId();
//			
////			String cstr = CommandExtractor.extract(cid);
//			
////			lsComStr.add(cstr);
//			
//			lsComStr.add(cid);
//		}
//		System.out.println("Session ID:" + e.IDESessionUUID);
//		lsComStr.add(e.IDESessionUUID);
		/** TO DELET*/
		
		try {
			writeEvent(userZip, eventLine); // write the event Info into EVENT_PATH
			writeEvent(userZip, eventDetail); // write the event Detail into EVENT_PATH
		} catch (Exception e1) {
			System.out.println("[Writing Error]: Exception occurred when writing event " + eventLine + " to " + EVENT_SAVE_PATH + userZip);
			e1.printStackTrace();
		}

	}
	
	/** <p>Simple write operation, write typeID into path.</p> */
	private static void writeEvent(String userZip, String eventLine) throws Exception{
		
		String path = EVENT_SAVE_PATH + userZip + ".txt";
		path = path.replace("\\", "-");
//		System.out.println("path :" + path);
		
		File file = new File(path);
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(eventLine);
		
		bw.close();
		fw.close();
		
	}
	
	/***
	 * <p>To identify the user <b>profile information</b> from the UserProfileEvent.
	 * Note that one developer might upload message with different user profiles.
	 * We ONLY identify the legal information of them.
	 * </p>
	 */
	public void identify() {

		/**
		 * Each .zip that is contained in the eventsDir represents all events that we
		 * have collected for a specific user, the folder represents the first day when
		 * the user uploaded data.
		 */
		Set<String> userZips = IoHelper.findAllZips(eventsDir); // find all zip file in root directory
		System.out.println("eventsDir  : " + eventsDir);
		System.out.println("zip numbers: " + userZips.size());
		for (String userZip : userZips) {
//			System.out.printf("\n#### processing user zip: %s #####\n", userZip); 
			identifyUserZip(userZip);
		}
		
	}
	
	private void identifyUserZip(String userZip) {
		// open the .zip file ... 
		try (IReadingArchive ra = new ReadingArchive(new File(eventsDir, userZip))) {
			System.out.print("USER:" + userZip); // print each userZip directory

			while (ra.hasNext()) {

				IDEEvent e = ra.getNext(IDEEvent.class); // next event in this developer

				if(e != null){
					boolean isBreak = readIdentify(e, userZip);
					if(isBreak){
						break; // break the loop and go to the next developer
					}
				}
			}
		}
		catch(Exception e){
			System.out.println("getNext() function is failed to get the next event!");
			e.printStackTrace();
		}
	}
	
	private boolean readIdentify(IDEEvent e, String userZip){
		boolean isBreak = false;
		if(e instanceof UserProfileEvent){
			UserProfileEvent ue = (UserProfileEvent) e;
			
			if(ue.Position != Positions.Unknown || ue.ProgrammingCSharp != Likert7Point.Unknown){ 
				isBreak = true;
				System.out.printf(" [Position]:%s [C#]:%s\n", ue.Position, ue.ProgrammingCSharp);
			}
		}

		return isBreak;
	}
	
	
	public void slice() {

		/**
		 * Each .zip that is contained in the eventsDir represents all events that we
		 * have collected for a specific user, the folder represents the first day when
		 * the user uploaded data.
		 */
		Set<String> userZips = IoHelper.findAllZips(eventsDir); // find all zip file in root directory
//		System.out.println("eventsDir  : " + eventsDir);
//		System.out.println("zip numbers: " + userZips.size());
		for (String userZip : userZips) {
//			System.out.printf("\n#### processing user zip: %s #####\n", userZip); 
			sliceUserZip(userZip);
		}
		
	}
	
	private void sliceUserZip(String userZip) {
		// open the .zip file ... 
		try (IReadingArchive ra = new ReadingArchive(new File(eventsDir, userZip))) {
			System.out.println("USER:" + userZip); // print each userZip directory

			IDEEvent ee = ra.getNext(IDEEvent.class); // the first IDEEvent event
			ZonedDateTime priorStamp = ee.getTriggeredAt(); // set temp dateTime
			String priorSessionID = ee.IDESessionUUID;
			
			try {
				writeSplitEvent(userZip, String.valueOf(getTypeID(ee)));
			} catch (Exception e1) {		
				e1.printStackTrace();
			}
			
			while (ra.hasNext()){
				IDEEvent e = ra.getNext(IDEEvent.class); // the next IDEEvent event
			
				if(e != null){
					sliceEvent(e, userZip, priorStamp, priorSessionID); // slice the stream by time stamps and sessionIDs
				}	
				
				priorStamp = e.getTriggeredAt();
				priorSessionID = e.IDESessionUUID;
				
			}
			
			try {
				writeSplitEvent(userZip, "---"); // add bottom split line in event stream.
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}catch(Exception e){
			System.out.println("getNext() function is failed to get the next event!");
			e.printStackTrace();
		}
	}
	
	private void sliceEvent(IDEEvent e, String userZip, ZonedDateTime priorStamp, String priorSessionID){
//		String typeName = e.getClass().getSimpleName();
		
		ZonedDateTime start = e.getTriggeredAt(); 
		Date startDate = Date.from(start.toInstant());
		long startL = startDate.getTime(); // time stamp of current event
		
		Date tempDate = Date.from(priorStamp.toInstant()); 
		long tempL = tempDate.getTime(); // time stamp of prior event
		long diff = startL - tempL;
		
		String currentSessionID = e.IDESessionUUID;
		
		/***
		 * Firstly, we split the event stream by sessionIDs.
		 * Secondly,we split the event stream by time interval()
		 */
		if(diff > 60000 || !currentSessionID.equals(priorSessionID)){ // if the last event duration is more than 5 minutes, then split
//			System.out.println("---");
			try {
				writeSplitEvent(userZip, "---");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
//		
		try {
			writeSplitEvent(userZip, String.valueOf(getTypeID(e)));
		} catch (Exception e1) {		
			e1.printStackTrace();
		}
		
//		System.out.println("[ID]:" + e.IDESessionUUID + ", [Event]:" + e.getClass().getSimpleName() + ", [Start]:" + e.TriggeredAt.toString());

	}
	
	/** <p>Simple write operation, write typeID into path.</p> */
	private static void writeSplitEvent(String userZip, String line) throws Exception{
		
		String path = EVENT_SLICE_PATH + userZip + ".txt";
		path = path.replace("\\", "-");
//		System.out.println("path :" + path);
		
		File file = new File(path);
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(line + "\n");
		
		bw.close();
		fw.close();
		
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