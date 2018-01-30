/**
 * Copyright 2016 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package utils;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.Set;

import cc.kave.commons.model.events.CommandEvent;
import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.testrunevents.TestCaseResult;
import cc.kave.commons.model.events.testrunevents.TestRunEvent;
import cc.kave.commons.model.events.userprofiles.UserProfileEvent;
import cc.kave.commons.model.events.visualstudio.DebuggerEvent;
import cc.kave.commons.model.events.visualstudio.IDEStateEvent;
import cc.kave.commons.model.events.visualstudio.SolutionEvent;
import cc.kave.commons.model.events.visualstudio.WindowEvent;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

/**
 * Simple example that shows how the interaction dataset can be opened, all
 * users identified, and all contained events deserialized.<br>
 * 
 * GettingStarted 类展示了如何打开 Event Dataset，如何确定每个用户(ZIP文件对应一个用户)， 获取每一条事件信息
 */
public class GettingStarted {

	private String eventsDir;
	
	public static int count=0;

	public GettingStarted(String eventsDir) {
		this.eventsDir = eventsDir; // 初始化，赋值 eventsDir
	}

	public static void main(String[] args){
		GettingStarted gs = new GettingStarted("C:/MSR18Dataset/Events-170301/Events-170301/2016-09-26/");
		gs.run();
	}
	
	public void run() {

//		System.out.printf("looking (recursively) for events in folder %s\n", new File(eventsDir).getAbsolutePath()); //打印出 eventDir绝对路径

		/*
		 * Each .zip that is contained in the eventsDir represents all events that we
		 * have collected for a specific user, the folder represents the first day when
		 * the user uploaded data.
		 */
		/**
		 * 每一个 zip 文件表示对一个特定用户的事件收集，且所有的zip文件都被包含在 eventsDir下，文件表示了用户上传数据的第一天
		 */
		Set<String> userZips = IoHelper.findAllZips(eventsDir); // 找到eventsDir目录下所有的zip文件

		for (String userZip : userZips) {
//			System.out.printf("\n#### processing user zip: %s #####\n", userZip); // 打印每个用户信息
			processUserZip(userZip);
		}
		
//		System.out.println("count: " + count);
	}

	/** 处理每个用户userZip的事件*/
	private void processUserZip(String userZip) {
		int numProcessedEvents = 0;
//		System.out.println("----------- NEW USER -----------");
		// open the .zip file ... 打开zip文件
		try (IReadingArchive ra = new ReadingArchive(new File(eventsDir, userZip))) {
			System.out.println("USER:" + eventsDir + userZip); // 打印每个zip位置
			// ... and iterate over content.
			// the iteration will stop after 200 events to speed things up. 循环查看 userZip 中的事件
//			while (ra.hasNext() && (numProcessedEvents++ < 500)) { // TODO: 去掉 < 200 限制
			while (ra.hasNext()) {
				/*
				 * within the userZip, each stored event is contained as a single file that
				 * contains the Json representation of a subclass of IDEEvent.
				 */
				IDEEvent e = ra.getNext(IDEEvent.class); // 下一条 IDEEvent 事件

				// the events can then be processed individually 打印该条事件的信息
//				if(e != null){
					processEvent(e);
//				}
			}
		}
		catch(Exception e){
			System.out.println("Bad Small.");
			e.printStackTrace();
		}
	}

	/**
	 * <pre> if you review the type hierarchy of IDEEvent, you will realize that several
	 * subclasses exist that provide access to context information that is specific
	 * to the event type.
	 * 
	 * To access the context, you should check for the runtime type of the event and
	 * cast it accordingly.
	 * 
	 * As soon as I have some more time, I will implement the visitor pattern to get
	 * rid of the casting. For now, this is recommended way to access the contents.
	 * 
	 * 通过 IDEEvent 事件，打印事件的相关信息</pre>
	 */
	private void processEvent(IDEEvent e) {		

//		if(e instanceof IDEStateEvent){
//			IDEStateEvent ce = (IDEStateEvent) e;
//
//			System.out.println("LifeCycle     :" + ce.IDELifecyclePhase);
//			System.out.println("ActiveWindow  : " + ce.ActiveWindow.getCaption().toString() + ", Type: " + ce.ActiveWindow.getType().toString());
//			
//			for(int i=0;i<ce.OpenWindows.size();i++){
//				System.out.println("opWindow: " + ce.OpenWindows.get(i).getCaption().toString());
//			}
//		}

//		if(e instanceof SolutionEvent){
//			SolutionEvent ce = (SolutionEvent) e;
//			System.out.println("Action: " + ce.Action.toString());
//			System.out.println("Target: " + ce.Target.toString());
//		}

//		if(e instanceof WindowEvent){
//			WindowEvent ce = (WindowEvent) e;
//			System.out.println("Action: " + ce.Action.toString());
//			System.out.println("Window: " + ce.Window.getCaption().toString());
//		}
		
//		if (e instanceof CommandEvent) { // 命令调用事件
//			process((CommandEvent) e);
//		} else if (e instanceof CompletionEvent) { // 代码完成事件
//			process((CompletionEvent) e);
//		} else { // 其他事件 TODO: 针对不同的 Event 获取不同的内容
//			/*
//			 * CommandEvent and Completion event are just two examples, please explore the
//			 * type hierarchy of IDEEvent to find other types and review their API to
//			 * understand what kind of context data is available.
//			 * 
//			 * CommandEvent 和 CompletionEvent 只是两个例子，可以根据不同的需求来调用相应 Event 的 API 来获得 context 
//			 * 
//			 * We include this "fall back" case, to show which basic information is always
//			 * available.
//			 */
//			processBasic(e);
//		}
//		if(e instanceof UserProfileEvent && count ==0){
//			UserProfileEvent ue = (UserProfileEvent) e;
//			System.out.print("[Position]:" + ue.Position);
//			System.out.print("[C#]:" + ue.ProgrammingCSharp + "\n");	
//		}
//		if(e == null){
//			System.out.println("e is null");
//		}
		String eventType = e.getClass().getSimpleName();
		int typeID = -1;
		switch(eventType){
		case"ActivityEvent":
			typeID = 0;
			break;
		case"CommandEvent":
			typeID = 1;
			break;
		case"CompletionEvent":
			typeID = 2;
			break;
		case"BuildEvent":
			typeID = 3;
			break;
		case"DebuggerEvent":
			typeID = 4;
			break;
		case"DocumentEvent":
			typeID = 5;
			break;
		case"EditEvent":
			typeID = 6;
			break;
		case"FindEvent":
			typeID = 7;
			break;
		case"IDEStateEvent":
			typeID = 8;
			break;
		case"SolutionEvent":
			typeID = 9;
			break;
		case"WindowEvent":
			typeID = 10;
			break;
		case"VersionControlEvent":
			typeID = 11;
			break;
		case"UserProfileEvent":
			typeID = 12;
			break;
		case"NavigationEvent":
			typeID = 13;
			break;
		case"SystemEvent":
			typeID = 14;
			break;
		case"TestRunEvent":
			typeID = 15;
			break;
		case"InfoEvent":
			typeID = 16;
			break;
		case"ErrorEvent":
			typeID = 17;
			break;
		case"InstallEvent":
			typeID = 18;
			break;
		case"UpdateEvent":
			typeID = 19;
			break;
		default:
			System.out.println(":" + e.getClass().getSimpleName());
			break;
		}
		System.out.println(typeID);
		
//		if(typeID == 5){
//			DebuggerEvent de = (DebuggerEvent) e;
//			System.out.print("[type]: " + de.getClass().getSimpleName().toString());
//			System.out.print(". [mode]: " + de.Mode.toString());
//			System.out.print(". [reason]: " + de.Reason);
//			System.out.print(". [action]: " + de.Action);
//			System.out.print(". [triger]: " + de.getTriggeredAt().toString());
//			System.out.print(". [end]: " + de.getTerminatedAt().toString() + "\n\n");
//		}
		
//		if(typeID == 2){
//			CommandEvent ce = (CommandEvent) e;
//			System.out.print("[type]: " + ce.getClass().getSimpleName().toString());
//			System.out.print(". [ID]: " + ce.getCommandId() + "\n\n");
//		}
		
//		if(typeID == 16){
//			TestRunEvent te = (TestRunEvent) e;
//			System.out.print("[type]: " + te.getClass().getSimpleName().toString());
//			System.out.print(". [abort]: " + te.WasAborted);
//			System.out.print(". [tests]: " + te.Tests.size() + "\n");
////			if(te.WasAborted == true){
//				Set<TestCaseResult> setTest = te.Tests;
//				for(TestCaseResult result: setTest){
////					if(!result.Result.equals("Success")){
//						System.out.println(result.TestMethod + ": " + result.Duration.toMillis() + "s -> " + result.Result);
////					}
//				}
////			}
//			
//		}

	}

	/** 打印 CommandID*/
	private void process(CommandEvent ce) {
//		System.out.printf("found a CommandEvent (id: %s) (window: %s)\n", ce.getCommandId(), ce.ActiveWindow.getCaption().toString());
		System.out.printf("%-25s | %s\n", ce.getClass().getSimpleName(), ce.getTriggeredAt().toString());
	}

	/** 打印 enClosingTypeName类型名*/
	private void process(CompletionEvent e) {
		ISST snapshotOfEnclosingType = e.context.getSST();
		ZonedDateTime t = e.getTriggeredAt();
		String enclosingTypeName = snapshotOfEnclosingType.getEnclosingType().getFullName();

//		System.out.printf("found a [CompletionEvent ] triggered at: (%s)\n", enclosingTypeName);
		System.out.printf("%-25s | %s\n", e.getClass().getSimpleName(), e.getTriggeredAt().toString());
	}

	/** 打印其他事件类型名*/
	private void processBasic(IDEEvent e) {
		String eventType = e.getClass().getSimpleName();
		ZonedDateTime triggerTime = e.getTriggeredAt();

		System.out.printf("%-25s | %s\n", e.getClass().getSimpleName(), e.getTriggeredAt().toString());

	}
}