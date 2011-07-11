package mobi.pruss.astrorender;

public class Earth extends Planet {
	public static final int PRIORITY = SiderealTime.PRIORITY + 1;

	public Earth(double u, double[] e) {
		super(u, e);
	}

	@Override
	public int getPriority() {
		return PRIORITY;
	}
}
