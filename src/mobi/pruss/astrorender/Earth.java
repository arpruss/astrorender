package mobi.pruss.astrorender;

public class Earth extends Planet {
	public static final int PRIORITY = SiderealTime.PRIORITY + 1;

	public Earth(double u, double[] e) {
		super(u, e);
		
		distance = 0;
		brightness = Double.NaN;
	}

	@Override
	public int getPriority() {
		return PRIORITY;
	}
}
