package cstar.yongfeng.preprocess;

public class HMMForward {
	/**N denotes the state number*/
	private int N;
	/**M denotes the observation number*/
	private int M;
	/**T denotes the length of observation sequence*/
	private int T;
	/**default initialization state probability*/
	private double[] iniState;
	/**STPM denotes the State Transfer Probability Matrix*/
	private double[][] STPM;
	/**OTPM denotes the Observation Transfer Probability Matrix*/
	private double[][] OTPM;
	/**O denotes the observation sequence*/
	private int[] O;
	
	/**probability calculate the probability of observation under this HMM*/
	private double probability;

	/**
	 * <p>To set the parameters of HMM model.</p>
	 * <p>Note: These input parameters is directly used to construct HMM <b>without checking</b>. 
	 * You'd better keep the legality of such parameters before instantiate the HMMForward object. </p>
	 * @param n the state number
	 * @param m the observation number
	 * @param t the length of observation sequence
	 * @param stpm the State Transfer Probability Matrix
	 * @param optm Observation Transfer Probability Matrix
	 * @param o observation sequence
	 * @DEFECT 
	 */
	public HMMForward(int n, int m, int t, double[][] stpm, double[][] optm, int[] o, double[] ini){
		this.N = n;
		this.M = m;
		this.T = t;
		this.STPM = stpm;
		this.OTPM = optm;
		this.O = o;
		this.iniState = ini;
	}
	
	public void showParameters(){
		System.out.println("parameter setting of HMM model\n------------------------------------\n");
		System.out.println("N: " + this.N + ", M: " + this.M + "\n");
		System.out.println("State Transfer Probability Matrix");
		Arrays.showDoubleArray(this.STPM);
		System.out.println("Observation Transfer Probability Matrix");
		Arrays.showDoubleArray(this.OTPM);
		System.out.print("Observation Sequence: ");
		Arrays.showArray(this.O);
		System.out.println("Length of Observation Sequence: " + this.T + "\n");
		System.out.print("Initialization probability: ");
		Arrays.showArray(this.iniState);
		System.out.println("------------------------------------\n");	
	}
	
	/**
	 * <p>To get the probability by forward algorithm</p>
	 * @return
	 */
	public double getProbability(){
		
		for(int i=0; i<this.N; i++){
			System.out.println("getAlpha:" + getAlpha(this.T, i));
			this.probability += getAlpha(this.T, i);
		}
		return probability;
	}
	
	private double getAlpha(int t, int i){
		if(t == 1){
			double p1 = 0.0d;
			p1 = this.iniState[i]*this.OTPM[i][this.O[0]];
			return p1;
		}else{
			double pt = 0.0d;
			for(int j=0; j<this.N; j++){
				pt += getAlpha(t-1, j)*this.STPM[j][i];
//				System.out.printf("[T]:%-3d, [alpha]:%-8.2f, [B]:%-4.2f\n", (t-1), getAlpha(t-1, j), this.STPM[j][i]);
			}
			pt *= this.OTPM[i][this.O[t-1]];
			return pt;
		}
	}

	public static void main(String[] args) {
		HMMForward hmm = new HMMForward(3, 2, 2, new double[][]{{0.5, 0.2, 0.3},{0.3, 0.5, 0.2},{0.2, 0.3, 0.5}}, new double[][]{{0.5, 0.5}, {0.4, 0.6}, {0.7, 0.3}}, new int[]{0, 1}, new double[]{0.2, 0.4, 0.4});
//		hmm.showParameters();
		System.out.println("[probability]: " + hmm.getProbability());
	}

}

class Arrays{
	
	public static void showArray(double[] arr){
		int l = arr.length;
		for(int i=0; i<l; i++){
			System.out.print(arr[i] + " ");
		}
		System.out.println("\n");
	}
	
	public static void showArray(int[] arr){
		int l = arr.length;
		for(int i=0; i<l; i++){
			System.out.print(arr[i] + " ");
		}
		System.out.println("\n");
	}
	
	public static void showDoubleArray(double[][] Matrix){
		int h = Matrix.length;
		int w = Matrix[0].length;
		for(int i=0; i<h; i++){
			for(int j=0; j<w; j++){
				System.out.print(Matrix[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println("");
	}
}
