package cstar.yongfeng.preprocess;

/**
 * <p>This class <b>DipDevp</b> can split the whole event stream context into several single-developer context.</p>
 *
 */
public class DivDevp {
	
	final public static String zipPath = "C:/MSR18Dataset/Events-170301/Events-170301/2016-05-09/";

	public static void main(String[] args) {
		
		
	}

	public static void readContext(String path){
		ExtractEvents ee = new ExtractEvents(path);
		ee.extract();
	}
}
