// Copyright (C) 2011 Alexander Pruss 
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

package mobi.pruss.astrorender;

/* 
 * Subclasses of SkyCalculator do various time-dependent calculations, such
 * as movements of planets or precession/nutation.  The subclasses do not have 
 * to recalculate at every tick.  Overriding getUpdateInterval() will adjust
 * how often updates are done, or one can override the whole setTime() method. 
 */

public class SkyCalculator {
	double mjd_tt;
	double mjd_ut1;
	double mjd_utc;
	static final double DEG2RAD = Math.PI/180.;
	static final double RAD2DEG = 180./Math.PI;
	static final double ARCSEC2RAD = Math.PI/(180.*60.*60.);
	
	/* Major solar system objects */
	public static final int SS_SUN = 0;
	public static final int SS_MERCURY = 1;
	public static final int SS_VENUS = 2;
	public static final int SS_EARTH = 3;
	public static final int SS_MARS = 4;
	public static final int SS_JUPITER = 5;
	public static final int SS_SATURN = 6;
	public static final int SS_URANUS = 7;
	public static final int SS_NEPTUNE = 8;
	public static final int SS_PLUTO = 9;
	public static final int SS_MOON = 10;
	
	static final double PRECISION_ARCSEC = 0.25;
	
	public static final int PRIORITY = 0;
	public static final double UPDATE = 1/86400.;
	
	public static final double[] ssApproxMinimumSecPerArcsec = {
		/* these are kind of approximate */
		/* update intervals for main SS objects will be these values times
		 * PRECISION_ARCSEC */
		24.35,
		12.95,
		19.27,
		24.35, /* same as sun for now */
		32.01,
		100.71,
		184.86,
		384.43,
		646.61,
		644.83,
		27.32*86400/(360*60*60) /* Moon: 1.82 */
	};
	
	SkyCalculator() {
		mjd_tt =  Time.INVALID_TIME;
		mjd_ut1 = Time.INVALID_TIME;
		mjd_utc = Time.INVALID_TIME;
	}
	
	public int getPriority() {
		return PRIORITY;
	}
	
	double getUpdateInterval() {
		return UPDATE;
	}
	
	void setTime(double tt, double ut1, double utc) {
		boolean needUpdate;
		needUpdate = ! Time.isValid(mjd_tt) || 
			Math.abs(mjd_tt - tt) >= getUpdateInterval();
			
		if (needUpdate) {
			mjd_tt = tt;
			mjd_ut1 = ut1;
			mjd_utc = utc;
			update();
		}
	}

	protected void update() {
	}

	static double fixRadians(double theta) {
		theta = theta % (2 * Math.PI);
		if (theta < 0)
			theta += 2 * Math.PI;
		return theta;
	}

	static double[] negateXYZ(double[] v) {
		double[] nv = {
			-v[0], -v[1], -v[2]	
		};
		
		return nv;
	}
}
