package mobi.pruss.astrorender;

public class Calculators extends SkyCalculator {
	public Earth earth;
	public NonEarthPlanet[] nonEarthPlanets; 
	public NonEarthPlanet mercury;
	public NonEarthPlanet venus;
	public NonEarthPlanet mars;
	public NonEarthPlanet jupiter;
	public NonEarthPlanet saturn;
	public NonEarthPlanet uranus;
	public NonEarthPlanet neptune;
	public NonEarthPlanet pluto;  /* not really a planet */
	
	public Time timeDriver;
	public PrecessionNutation precNut;
	public SiderealTime siderealTime;
	
	static final double[] mercuryElements = {
	0.38709893,	 0.00000066,
	0.20563069,	 0.00002527,
	7.00487,		-0.006530556,
	48.33167,		-0.123972222,
	77.45645,		 0.159325,
	252.25084,	149472.6745};
	static final double[] venusElements = {
	0.72333199,  0.00000092,
	0.00677323, -0.00004938,
	3.39471,		-0.000794444,
	76.68069,		-0.276913889,
	131.53298,	-0.030222222,
	181.97973, 	58517.81557};
	static final double[] earthElements = {
	1.00000011, -0.00000005,
	0.01671022, -0.00003804,
	0.00005,		-0.013038889,
	-11.26064,	-5.063402778,
	102.94719,	 0.332855556,
	100.46435,	35999.3724};
	static final double[] marsElements = {
	1.52366231, -0.00007221,
	0.09341233,  0.00011902,
	1.85061,		-0.007075,
	49.57854,		-0.28338611,
	336.04084,	 0.43355,
	355.45332,	19140.30661};
	static final double[] jupiterElements = {
	5.20336301,  0.00067074, // fix this?
	0.04839266, -0.0001288,
	1.3053,			-0.00115278,
	100.55615,	 0.33810278,
	14.75385,		 0.23331389,
	34.40438,		3034.74399};
	static final double[] saturnElements = {
	9.5370732, 	-0.0030153,
	0.0541506, 	-0.0003676,
	2.48446,		 0.0016972,
	113.71504, 	-0.4419583,
	92.43194,		-0.5413583,
	49.94432,		1222.5147};
	static final double[] uranusElements = {
	19.19126393, 0.00152025,
	0.04716771,	-0.0001915,
	0.76986,		-0.000580556,
	74.22988,		 0.467055556,
	170.96424,	 0.3646,
	313.23218,	428.4854972};
	static final double[] neptuneElements = {
	30.06896348,-0.00125196,
	0.00858587,	 0.00002514,
	1.76917,		-0.00101111,
	131.72169,	-0.04201389,
	44.97135,		-0.23456389,
	304.88003,	218.4581139};
	static final double[] plutoElements = {
	39.4816868, -0.0007691,
	0.24880766,  0.00006465,
	17.14175,		 0.003075,
	110.30347,	-0.0103694,
	224.06676, 	-0.0367361,
	238.92881,	145.20775	 };
	
	public Calculators() {
		earth = new Earth(SkyCalculator.ssApproxMinimumSecPerArcsec[SkyCalculator.SS_MERCURY] 
		             * SkyCalculator.PRECISION_ARCSEC,
		             earthElements);
		mercury = new NonEarthPlanet(earth, 
				SkyCalculator.ssApproxMinimumSecPerArcsec[SkyCalculator.SS_MERCURY] 
				    * SkyCalculator.PRECISION_ARCSEC, mercuryElements);
		venus = new NonEarthPlanet(earth, 
				SkyCalculator.ssApproxMinimumSecPerArcsec[SkyCalculator.SS_VENUS] 
				    * SkyCalculator.PRECISION_ARCSEC, venusElements);
		mars = new NonEarthPlanet(earth, 
				SkyCalculator.ssApproxMinimumSecPerArcsec[SkyCalculator.SS_MARS] 
				    * SkyCalculator.PRECISION_ARCSEC, marsElements);
		jupiter = new NonEarthPlanet(earth, 
				SkyCalculator.ssApproxMinimumSecPerArcsec[SkyCalculator.SS_JUPITER] 
				    * SkyCalculator.PRECISION_ARCSEC, jupiterElements);
		saturn = new NonEarthPlanet(earth, 
				SkyCalculator.ssApproxMinimumSecPerArcsec[SkyCalculator.SS_SATURN] 
				    * SkyCalculator.PRECISION_ARCSEC, saturnElements);
		uranus = new NonEarthPlanet(earth, 
				SkyCalculator.ssApproxMinimumSecPerArcsec[SkyCalculator.SS_URANUS] 
				    * SkyCalculator.PRECISION_ARCSEC, uranusElements);
		neptune = new NonEarthPlanet(earth, 
				SkyCalculator.ssApproxMinimumSecPerArcsec[SkyCalculator.SS_NEPTUNE] 
				    * SkyCalculator.PRECISION_ARCSEC, neptuneElements);
		pluto = new NonEarthPlanet(earth, 
				SkyCalculator.ssApproxMinimumSecPerArcsec[SkyCalculator.SS_PLUTO] 
				    * SkyCalculator.PRECISION_ARCSEC, plutoElements);

		nonEarthPlanets = new NonEarthPlanet[] {
				null, mercury, venus, null, mars, jupiter, saturn,
				uranus, neptune, pluto, null, null
		};
		
		
		precNut = new PrecessionNutation();
		siderealTime = new SiderealTime(precNut);
		timeDriver = new Time(
				earth, mercury, venus, mars, jupiter, saturn, uranus, neptune,
				pluto, precNut, siderealTime);
	}

	public void setTimeUTC(double t) {
		timeDriver.setTimeUTC(t);
	}
	
	public static float[] toFloatVector(double[] v) {
		float[] xyz = {
		(float)v[0],
		(float)v[1],
		(float)v[2],
		0f };
		
		return xyz;
	}
	
}
