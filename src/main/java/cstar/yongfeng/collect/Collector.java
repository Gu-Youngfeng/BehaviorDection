package cstar.yongfeng.collect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.visualstudio.DebuggerEvent;
import cc.kave.commons.model.events.visualstudio.DebuggerMode;
import cc.kave.commons.model.events.visualstudio.IDEStateEvent;
import cc.kave.commons.model.events.visualstudio.LifecyclePhase;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

/**
 * <p>Class <b>Collector</b> can mining <b>list of event stream</b> from the given developers' zip dataset.
 * The <b>collect type</b> defines the event type that we will collect. see {@link Collector(String zip, CollectType ct)}
 * </p> 
 *
 */
public class Collector {
	/** userZip path */
	private String userZip;
	/** list of event stream */
	private List<ArrayList<IDEEvent>> lslsEvents;
	/** collect type */
	private CollectType collectType;
	
	private int flag;
	
	/**
	 * Initializing the Collector using <b>userZip</b> and <b>collectType</b>.
	 * @param zip userZip path
	 * @param ct collect type, which includes TotalTime, WorkTime, DebugTime
	 */
	public Collector(String zip, CollectType ct){
		this.userZip = zip;
		this.collectType = ct;
		this.flag = 0;
		
		Collecting();
	}
	
	/** To get the list of event stream of userZip. */
	public List<ArrayList<IDEEvent>> getlslsEvent(){
		return this.lslsEvents;
	}
	
	public void showProcess(){
		
		System.out.println("[" + this.collectType.toString() + "]:" + this.lslsEvents.size());
		
		for(ArrayList<IDEEvent> lsStream: this.lslsEvents){
			System.out.println("\n------ " + this.collectType.toString() + " ------ {" + lsStream.size() + "}");
			for(IDEEvent event: lsStream){	
				String strInfo = (new MsgGetter(event)).getInfo();
//				System.out.println("|TYPE|: " + event.getClass().getSimpleName());
				System.out.println(strInfo);
			}
		}
	}
	
	private void Collecting(){
		
		switch(this.collectType){
		case TotalTime:
			//TODO: collect developmental time
			break;
		case WorkTime:
			miningWork();
			break;
		case DebugTime:
			miningDebug();
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * <p>Mining the list of debugging event stream. 
	 * Basically, we only mine the evens between these <b>[DebuggerEvents]</b>, whose actions are <b>Run</b> and <b>Design</b>.</p>
	 */
	private void miningDebug(){
		
		List<ArrayList<IDEEvent>> lslsevents = new ArrayList<ArrayList<IDEEvent>>(); // list of debug stream
		
		try (IReadingArchive ra = new ReadingArchive(new File(this.userZip))) { // read from the userZip
			
			ArrayList<IDEEvent> lsStream = new ArrayList<IDEEvent>(); // each debug stream
			
			while (ra.hasNext()) { // read event one by one

				IDEEvent e = ra.getNext(IDEEvent.class); // read the next IDEEvent

				if(e instanceof DebuggerEvent){ // find DebuggerEvent
					DebuggerEvent de = (DebuggerEvent) e;
					if(de.Mode == DebuggerMode.Run && de.Reason.equals("dbgEventReasonLaunchProgram")){
						// Debug starts
						flag = 1;
						lsStream.add(de);
						
					}else if(de.Mode == DebuggerMode.Design){
						// Debug ends
						flag = 0;
						lsStream.add(de);
						ArrayList<IDEEvent> lsTemp = new ArrayList<IDEEvent>(lsStream); // list copy
						lslsevents.add(lsTemp); // put the debug stream into lslsevent
						lsStream.clear(); // clear the debug stream
					}else{
						// Debugging
						lsStream.add(de);
					}
				}else{ // other IDEEvent
					if(flag == 1){
						// debugging continues
						lsStream.add(e);
					}
				}
			}
		}
		catch(Exception e){
			System.out.println("getNext() function is failed to get the next event!");
			e.printStackTrace();
		}
		
		this.lslsEvents = lslsevents;
	}
	
	/**
	 * <p>Mining the list of working event stream. To simplify the problem, 
	 * we just use the tag <b>"LifCyclePhase"</b> of <b>[IDEStateEvent]</b> to extract the In-IDE time of developers.
	 * </p>
	 */
	private void miningWork(){
		
		List<ArrayList<IDEEvent>> lslsevents = new ArrayList<ArrayList<IDEEvent>>(); // list of debug stream
		
		try (IReadingArchive ra = new ReadingArchive(new File(this.userZip))) { // read from the userZip
			
			ArrayList<IDEEvent> lsStream = new ArrayList<IDEEvent>(); // each debug stream
						
			/** The first event is the IDEStateEvent **/
			while (ra.hasNext()) { // read event one by one

				IDEEvent e = ra.getNext(IDEEvent.class); // read the next IDEEvent
				
				if(e instanceof IDEStateEvent){ // find IDEStateEvent
					IDEStateEvent ide = (IDEStateEvent) e;
					if(ide.IDELifecyclePhase == LifecyclePhase.Startup){
						// Visual Studio starts
						flag = 1;
						lsStream.add(ide);
						
					}else if(ide.IDELifecyclePhase == LifecyclePhase.Shutdown){
						// Visual Studio ends
						flag = 0;
						lsStream.add(ide);
						if(lsStream != null){
							ArrayList<IDEEvent> lsTemp = new ArrayList<IDEEvent>(lsStream); 
							lslsevents.add(lsTemp); 
							lsStream.clear(); 
						}
					}else{
						// IDE state events within the Visual Studio
						lsStream.add(ide);
					}
				}else{ // other IDEEvent
					if(flag == 1){
						// Other events within the Visual Studio
						lsStream.add(e);
					}
				}
				
			}
			if(lsStream != null){
				lslsevents.add(lsStream); // the rest event might without the Startup and Shutdown
			}
		}
		catch(Exception e){
			System.out.println("getNext() function is failed to get the next event!");
			e.printStackTrace();
			return;
		}
		
		this.lslsEvents = lslsevents;
	}	
	
	/**
	 * <p>To count the <b>In-IDE</b> time of developers. 
	 * We count events between pair of first <b>Startup</b> and last <b>Shutdown</b>.</p>
	 * @return count in minutes
	 */
	public static float getInIDETime(String path){
		
		long delt = 0;
		
		try {
			List<String> lsContext = readContext(path);
			long start = 0l;
			long end = 0l;
			List<String> lsShutdown = new ArrayList<String>();
			for(String line: lsContext){
//				System.out.println(">>" + line);
				if(line.contains("IDEStateEvent") && line.contains("Startup")){
//					System.out.println("[Startup ]: " + line);
					if( start == 0 ){ // new start
						start = extractDate(line);
					}else if( lsShutdown.size() != 0 && start !=0 ){ // another start
						end = extractDate(lsShutdown.get(lsShutdown.size()-1));
						delt += end - start;
//						System.out.println("[Duration]: " + delt);
						start = extractDate(line); end = 0;
						lsShutdown.clear();
					}else{
						
					}
				}else if(line.contains("IDEStateEvent") && line.contains("Shutdown")){
//					System.out.println("[Shutdown]: " + line);
					lsShutdown.add(line);		
				}else{
					continue;
				}
			}
			
			if(start != 0){ // if the end event is not the IDEStateEvent.Shutdown
				end = extractDate(lsContext.get(lsContext.size()-1));
				delt += end - start;
//				System.out.println("[Duration]: " + delt*1.0/1000*60*1.0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		float deltMiute = (float) ((float) delt*1.0/(1000*60*1.0));
		
		return deltMiute;

	}
	
	private static List<String> readContext(String userProfile) throws Exception{
		
		File file = new File(userProfile);
		FileReader fr = new FileReader(file);
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(fr);
		
		List<String> lsItems = new ArrayList<String>();
		
		String line = "";
		String lineAdd = "";
		while((line = br.readLine()) != null){ // we only collect the legal developers
			
			if(line.startsWith("### ")){
				lineAdd += line;
//				lsItems.add(line);
			}else if(line.startsWith("[") || line.equals("EMPTY")){
				lineAdd += " | " + line;
			}else{
				lsItems.add(lineAdd);
				lineAdd = "";
			}
		}
		
		return lsItems;
	}
	
	public static long extractDate(String line){
		long tim = 0l;
		
//		System.out.println("[line]: " + line);
		int indexStart = line.indexOf("|");
//		System.out.println(indexStart);
		int indexEnd = line.lastIndexOf("|");
//		System.out.println(indexEnd);
		String strDate = line.substring(indexStart+2, indexEnd-1);
//		System.out.println("[mid ]: " + strDate);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd'T'hh:mm:ss");
		
		try {
			Date date = sdf.parse(strDate);
			tim = date.getTime();
//			System.out.println("[date]: " + date.toString());
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		return tim;
	}
	
	/**
	 * <p>Feature 15: To get the performance profile... usage times.</p>
	 * @return count in long type
	 */
	public static int getPerformance(String path){
		
		int count = 0;
		
		try {
			List<String> lsContext = readContext(path);

			for(String line: lsContext){
				if(line.contains("Performance Profiler...")){
					count++;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;

	}
	
	/**
	 * <p>To turn the userZip to path format.</p>
	 * <pre>C:/MSR18Dataset/Events-170301-2/Events-170301-2/2016-05-09/1.zip
	 *>>>>>> 
	 *E:/workspaceee/BehaviorDection/src/main/resources/total/2016-05-09-1.zip.txt 
	 * </pre>
	 * @param userZip
	 * @return
	 */
	public static String searchEventFile(String userZip){
		String path = "";
		
		String[] splitZip = userZip.split("/");
		int len = splitZip.length;
		if(len >= 2){
			if(userZip.contains("earlier") && userZip.contains("data")){
				String head = splitZip[len-4];
				String next1 = splitZip[len-3];
				String next2 = splitZip[len-2];
				String next3 = splitZip[len-1];
				path = "E:/workspaceee/BehaviorDection/src/main/resources/total/" + head + "-" + next1 + "-" + next2 + "-" + next3+ ".txt";
			}else{
				String head = splitZip[len-2];
				String next = splitZip[len-1];
				path = "E:/workspaceee/BehaviorDection/src/main/resources/total/" + head + "-" + next + ".txt";
			}
		}
		
		return path;
	}
	
	/**
	 * <p>To get the developmental day. I.e, to count the appearing date in the dataset.</p>
	 * @param path
	 * @return
	 */
	public static int getDevelopDays(String path){

		Set<String> lsDate = new HashSet<String>();
		
		try {
			List<String> lsContext = readContext(path);

			for(String line: lsContext){
//				System.out.println(line);
				String date = extracrDateString(line);
				lsDate.add(date);
//				System.out.println("[Day]: " + date);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		for(String line: lsDate){
//			System.out.println("[Day]: " + line);
//		}
		
		return lsDate.size();
	}

	/**
	 * <p>To get the date string of the line</p>
	 * <pre>
	 *### CommandEvent | 2016-05-23T06:36:48.563124200-07:00 | [command]:VsAction:1:File.Exit
	 *>>>>
	 *2016-05-23
	 * </pre>
	 * @param line
	 * @return
	 */
	private static String extracrDateString(String line){
		String strTime = "";
		
		if(!line.contains("###") || !line.contains("Event")){
			strTime = "";
		}else{
			int indexStart = line.indexOf("|");
			int indexEnd = line.lastIndexOf("|");
			String strDate = line.substring(indexStart+2, indexEnd-1);
			
			String[] strList = strDate.split("T");
			
			strTime = strList[0];
		}
		return strTime;
	}
}
