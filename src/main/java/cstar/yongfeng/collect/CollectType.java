package cstar.yongfeng.collect;

/**
 * <p>To provide the <b>Collect Type</b> in collecting process. 
 * We only provide 3 types: {@link#TotalTime}, {@link#WorkTime}, and {@link#DebugTime}.</p>
 */
public enum CollectType {
	/** Whole time on computer */
	TotalTime,
	/** Whole time inside the Visual Studio */
	WorkTime,
	/** Whole time in debugging process */
	DebugTime
}
