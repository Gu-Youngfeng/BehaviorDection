package cstar.yongfeng.collect;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.visualstudio.DebuggerEvent;
import cc.kave.commons.model.events.visualstudio.DebuggerMode;
import cc.kave.commons.model.events.visualstudio.IDEStateEvent;
import cc.kave.commons.model.events.visualstudio.LifecyclePhase;
import cc.kave.commons.model.events.visualstudio.WindowAction;
import cc.kave.commons.model.events.visualstudio.WindowEvent;
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
//						lsStream.add(de);
						
					}else if(de.Mode == DebuggerMode.Design){
						// Debug ends
						flag = 0;
//						lsStream.add(de);
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
					else{
						// debugging is over
						continue;
					}
				}
			}
		}
		catch(Exception e){
			System.out.println("getNext() function is failed to get the next event!");
			e.printStackTrace();
		}
		
//		this.lslsEvents = new ArrayList<ArrayList<IDEEvent>>(lslsevents);
		this.lslsEvents = lslsevents;
	}
	
	/**
	 * <p>Mining the list of working event stream. 
	 * TODO: How to detect activity interval 
	 */
	private void miningWork(){
		
		List<ArrayList<IDEEvent>> lslsevents = new ArrayList<ArrayList<IDEEvent>>(); // list of debug stream
		
		try (IReadingArchive ra = new ReadingArchive(new File(this.userZip))) { // read from the userZip
			
			ArrayList<IDEEvent> lsStream = new ArrayList<IDEEvent>(); // each debug stream
			
			IDEEvent testE = ra.getNext(IDEEvent.class); // read the first IDEEvent
			 // get the start time
			int dateYear = testE.getTriggeredAt().getYear();
			int dateMonth = testE.getTriggeredAt().getMonthValue();
			int dateDay = testE.getTriggeredAt().getDayOfMonth();
			ZoneId zoneID = ZoneId.of("UTC+1");
//			ZonedDateTime dateStart = new ZonedDateTime(dateYear, dateMonth, dateDay, 3, 0, 0, 0l, zoneID);
			
			while (ra.hasNext()) { // read event one by one

				IDEEvent e = ra.getNext(IDEEvent.class); // read the next IDEEvent
				
				
				
			}
		}
		catch(Exception e){
			System.out.println("getNext() function is failed to get the next event!");
			e.printStackTrace();
		}
		
//		this.lslsEvents = new ArrayList<ArrayList<IDEEvent>>(lslsevents);
		this.lslsEvents = lslsevents;
	}

}
