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
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

public class GettingStartedContexts {

	private String ctxsDir;

	public GettingStartedContexts(String ctxsDir) { // 赋值 ctxsDir
		this.ctxsDir = ctxsDir;
	}

	/** 在ctxsDir 目录下查找所有的 context*/
	public void run() { 

		System.out.printf("looking (recursively) for solution zips in folder %s\n",
				new File(ctxsDir).getAbsolutePath()); // 打印 ctxsDir 绝对路径

		/*
		 * Each .zip that is contained in the ctxsDir represents all contexts that we
		 * have extracted for a specific C# solution found on GitHub. The folder
		 * represents the structure of the repository. The first level is the GitHub
		 * user, the second the repository name. After that, the folder structure
		 * represents the file organization within the respective repository. If you
		 * manually open the corresponding GitHub repository, you will find a "<x>.sln"
		 * file for a "<x>.sln.zip" that is contained in our dataset.
		 */
		/**
		 * ctxsDir 中的每一个 zip 文件记录了一个特定的 C# 解决方案的所有 context 信息。文件夹代表着 repository 的层次结构。
		 * 第一层是 GitHub 上的用户，第二层是 repository 的名字，再往下，文件代表着repository 的 file organization。
		 * 如果你网上手动查询这些 GitHub 的库，你会在库中找到一个类似于  “<x>.sln” 的文件，这样一个解决方案信息对应于我们数据集中的一个 “<x>.sln.zip”
		 */
		Set<String> slnZips = IoHelper.findAllZips(ctxsDir); // 找到目录下的所有 zip 文件

		for (String slnZip : slnZips) {
			System.out.printf("\n#### processing solution zip: %s #####\n", slnZip);
			processSlnZip(slnZip);
		}
	}

	/** 处理每个zip文件 */
	private void processSlnZip(String slnZip) {
		int numProcessedContexts = 0;

		// open the .zip file ...
		try (IReadingArchive ra = new ReadingArchive(new File(ctxsDir, slnZip))) {
			// ... and iterate over content.

			// the iteration will stop after 10 contexts to speed things up.
			while (ra.hasNext() && (numProcessedContexts++ < 10)) {
				/*
				 * within the slnZip, each stored context is contained as a single file that
				 * contains the Json representation of a Context.
				 */
				Context ctx = ra.getNext(Context.class); // 对该 zip 文件，获取下一个 context 内容

				// the events can then be processed individually
				processContext(ctx);
			}
		}
	}

	/** 处理该 context， 每个 context 信息包含了2部分内容：1)简化抽象语法树 SST；2)类型 type shape*/
	private void processContext(Context ctx) {
		// a context is an abstract view on a single type declaration that contains of
		// two things:

		// 1) a simplified syntax tree of the type declaration 
		/** 类型声明的 SST */
		process(ctx.getSST());

		// 2) a "type shape" that provides information about the hierarchy of the 
		// declared type TypeShape 
		/** 提供了声明类型的层次结构 */
		process(ctx.getTypeShape());
	}

	private void process(ITypeShape ts) {
		
		//1. a type shape contains hierarchy info for the declared type
		/** TypeShape 存放的是所有在SST中声明的类型的层次结构*/ // th = ts.getTypeHierarchy() 获取层次结构信息
		ITypeHierarchy th = ts.getTypeHierarchy();
		// the type that is being declared in the SST
		ITypeName tElem = th.getElement();
//		System.out.println("TypeHierarchy >> " + th.toString());
		System.out.println("Element >> " + tElem.toString());
		// the type might extend another one (that again has a hierarchy)
//		ITypeName tExt = th.getExtends().getElement(); // TODO: bug fixed
//		System.out.println("tExte >> " + ts.getTypeHierarchy().getExtends().toString());
		
		
		// or implement interfaces...
		/** 得到接口信息 */ // th.getImplements() 获取层次的接口信息
//		for (ITypeHierarchy tImpl : th.getImplements()) {
//			ITypeName tInterf = tImpl.getElement();
//			System.out.println("FullName >> " + tInterf.getFullName().toString());
//		}

		
		//2. a type shape contains hierarchy info for all methods declared in the SST
		/** TypeShape 存放的是所有在SST中声明的函数的层次结构 */ // mhs = ts.getMethodHierarchy() 
		Set<IMemberHierarchy<IMethodName>> mhs = ts.getMethodHierarchies();
		for (IMemberHierarchy<IMethodName> mh : mhs) {
			// the declared element (you will find the same name in the SST) 声明的函数
			IMethodName elem = mh.getElement();

			// potentially, the method overrides another one higher in the hierarchy 被重写的更高一级函数
			// (may be null)
			IMethodName sup = mh.getSuper();

			// in deep hierarchies, the method signature might have been introduced earlier 
			// (may be null)
			IMethodName first = mh.getFirst();
			
			System.out.println("method >>" + mh.getElement().toString()); 
//			System.out.println("method FULL >>" + mh.getElement().getFullName().toString());
//			System.out.println("method Name >>" + mh.getElement().getName().toString()); // TODO: print added
//			System.out.println("method Turn >>" + mh.getElement().getReturnType().toString()); 
//			System.out.println("method Def  >>" + mh.getElement().getDeclaringType().toString()); 
//			System.out.println("method Indf >>" + mh.getElement().getIdentifier().toString());
//			System.out.println("method Psiz >>" + mh.getElement().getParameters().size());
		}
	}

	private void process(ISST sst) {
		// SSTs represent a simplified meta model for source code. You can use the
		// various accessors to browse the contained information
		
		// which type was edited? 哪种类型被编辑
		ITypeName declType = sst.getEnclosingType();

		// which methods are defined? 哪种方法被定义
		for (IMethodDeclaration md : sst.getMethods()) {
			IMethodName m = md.getName();

			for (IStatement stmt : md.getBody()) {
				// process the body...
				/// most likely, you will have to write an <see>ISSTNodeVisitor</see>
//				stmt.accept(null, null); // TODO: bug fixed
			}
		}

		// all references to types or type elements are fully qualified and preserve
		// many information about the resolved type
		declType.getNamespace();
		declType.isInterfaceType();
		declType.getAssembly();

		// you can distinguish reused types from types defined in a local project
		boolean isLocal = declType.getAssembly().isLocalProject();

		// the same is possible for all other <see>IName</see> subclasses, e.g.,
//		 <see>IMethodName</see>
		IMethodName m = Names.getUnknownMethod();
		m.getDeclaringType();
		m.getReturnType();
		// or inspect the signature
		for (IParameterName p : m.getParameters()) {
			String pid = p.getName();
			ITypeName ptype = p.getValueType();
		}

	}
}