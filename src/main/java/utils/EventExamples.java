/**
 * Copyright 2016 Technische Universität Darmstadt
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
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.CommandEvent;
import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.utils.io.ReadingArchive;
import cc.kave.commons.utils.io.json.JsonUtils;

/**<p>
 * This class contains several code examples that explain how to read enriched
 * event streams with the CARET platform. It cannot be run, the code snippets
 * serve as documentation.</p>
 * <p>类 EventExample 包含了几个例子，他们展示了如何利用 CARET 平台来读取富文本事件流。例子不能运行，仅当做文档查看。
 * </p>
 */
public class EventExamples {

	/**
	 * this variable should point to a folder that contains a bunch of .zip
	 * files that may be nested in subfolders. If you have downloaded the event
	 * dataset from our website, please unzip the archive and point to the
	 * containing folder here.
	 * 
	 * 变量DIR_USERDATA指的是包含所有的zip文件的文件夹。首先从网站上下载相应的 Event dataset，
	 * 解压后，再将 DIR_USERDATA 指向该文件夹即可。
	 */
	private static final String DIR_USERDATA = "/Users/seb/Downloads/All-Clean/";

	/**
	 * 1: Find all users in the dataset. 找到所有的开发者，并返回所有的 zip 文件列表
	 */
	public static List<String> findAllUsers() {
		// This step is straight forward, as events are grouped by user. Each
		// .zip file in the dataset corresponds to one user.

		List<String> zips = Lists.newLinkedList(); // 所有的zip文件列表
		for (File f : FileUtils.listFiles(new File(DIR_USERDATA), new String[] { "zip" }, true)) {
			zips.add(f.getAbsolutePath());
		}
		return zips;
	}

	/**
	 * 2: Reading events 根据遍历的 userZips 逐一查找其中包含的事件
	 */
	public static void readAllEvents() {
		// each .zip file corresponds to a user
		List<String> userZips = findAllUsers();

		for (String user : userZips) {
			// you can use our helper to open a file...
			ReadingArchive ra = new ReadingArchive(new File(user));
			// ...iterate over it...
			while (ra.hasNext()) {
				// ... and desrialize the IDE event.
				IIDEEvent e = ra.getNext(IIDEEvent.class); // 获取下一个事件
				// afterwards, you can process it as a Java object
				process(e);
			}
			ra.close();
		}
	}

	/**
	 * 3: Reading the plain JSON representation
	 */
	public static void readPlainEvents() {
		// the example is basically the same as before, but...
		List<String> userZips = findAllUsers();

		for (String user : userZips) {
			ReadingArchive ra = new ReadingArchive(new File(user));
			while (ra.hasNext()) {
				// ... sometimes it is easier to just read the JSON...
				String json = ra.getNextPlain();
				// .. and call the deserializer yourself.
				IIDEEvent e = JsonUtils.fromJson(json, IIDEEvent.class);
				process(e);

				// Not all event bindings are very stable already, reading the
				// JSON helps debugging possible bugs in the bindings
			}
			ra.close();
		}
	}

	/**
	 * 4: Processing events
	 */
	private static void process(IIDEEvent event) {
		// once you have access to the instantiated event you can dispatch the
		// type. As the events are not nested, we did not implement the visitor
		// pattern, but resorted to instanceof checks.
		if (event instanceof CommandEvent) {
			// if the correct type is identified, you can cast it...
			CommandEvent ce = (CommandEvent) event;
			// ...and access the special context for this kind of event
			System.out.println(ce.CommandId);
		} else {
			// there a many different event types to process, it is recommended
			// that you browse the package to see all types and consult the
			// website for the documentation of the semantics of each event...
		}
	}
}