/* 
* Copyright (c) 2007 The JAT Project and (c) 2011 Alexander R. Pruss. All rights reserved.
*
* This file is part of AstroRender/AstroObserver. AstroRender/AstroObserver is free software; you can
* redistribute it and/or modify it under the terms of the
* GNU General Public License as published by the Free Software
* Foundation; either version 2 of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*
*/


package mobi.pruss.astrorender;

import android.util.Log;

/* The official time system for AstroObserver is MJD rather than JD
 * in order to have more significant figures;
 */

public class Time extends SkyCalculator {
	static final double MJD0 = 2400000.5;
	static final double MJD_J2000 = 51544.5;
	static final double INVALID_TIME = Double.NaN;
	static final double TT_TAI = 32.184;  // constant
	SkyCalculator[] calculators;

	/* 
	 * Construct with a list of all the SkyCalculators to be updated
	 * when setTime() or setTimeUTC() is called.  
	 */
	Time(SkyCalculator... c) {
		super();
		calculators = c;
	}
	
	@Override
	protected void update() {
		/* The Time class updates always when setTime() is called */
	}
	
	@Override
	void setTime(double tt, double ut1, double utc) {
		mjd_tt = tt;
		mjd_ut1 = ut1;
		mjd_utc = utc;

		for (SkyCalculator c:calculators) {
			c.setTime(tt, ut1, utc);
		}
}

	void setTimeUTC(double utc) {
		/* TODO: improve utc-u1 conversion */
		setTime(utcToTT(utc), utc, utc);
	}

	static boolean isValid(double t) {
		return !Double.isNaN(t);
	}
	
	static double jdToMJD(double jd) {
		return jd - MJD0;
	}
	
	static double mjdToJD(double mjd) {
		return mjd + MJD0;
	}	
	
	/** Return the difference between TAI and UTC (known as leap seconds).
	    * Values from the USNO website: ftp://maia.usno.navy.mil/ser7/leapsec.dat
	    * Updated by ARP: July 2011
	    * @param mjd Modified Julian Date
	    * @return number of leaps seconds.
	    */

	   private static int tai_utc(double mjd){
	       /* most common case, so up front */
	   if (mjd >= 54832.0) return 34;
	   
	   if ((mjd >=41317.0)&&(mjd < 41499.0)) return 10;
	   if ((mjd >=41499.0)&&(mjd < 41683.0)) return 11;
	   if ((mjd >=41683.0)&&(mjd < 42048.0)) return 12;
	   if ((mjd >=42048.0)&&(mjd < 42413.0)) return 13;
	   if ((mjd >=42413.0)&&(mjd < 42778.0)) return 14;
	   if ((mjd >=42778.0)&&(mjd < 43144.0)) return 15;
	   if ((mjd >=43144.0)&&(mjd < 43509.0)) return 16;
	   if ((mjd >=43509.0)&&(mjd < 43874.0)) return 17;
	   if ((mjd >=43874.0)&&(mjd < 44239.0)) return 18;
	   if ((mjd >=44239.0)&&(mjd < 44786.0)) return 19;
	   if ((mjd >=44786.0)&&(mjd < 45151.0)) return 20;
	   if ((mjd >=45151.0)&&(mjd < 45516.0)) return 21;
	   if ((mjd >=45516.0)&&(mjd < 46247.0)) return 22;
	   if ((mjd >=46247.0)&&(mjd < 47161.0)) return 23;
	   if ((mjd >=47161.0)&&(mjd < 47892.0)) return 24;
	   if ((mjd >=47892.0)&&(mjd < 48257.0)) return 25;
	   if ((mjd >=48257.0)&&(mjd < 48804.0)) return 26;
	   if ((mjd >=48804.0)&&(mjd < 49169.0)) return 27;
	   if ((mjd >=49169.0)&&(mjd < 49534.0)) return 28;
	   if ((mjd >=49534.0)&&(mjd < 50083.0)) return 29;
	   if ((mjd >=50083.0)&&(mjd < 50630.0)) return 30;
	   if ((mjd >=50630.0)&&(mjd < 51179.0)) return 31;
	   if ((mjd >=51179.0)&&(mjd < 53736.0)) return 32;
	   if ((mjd >=53736.0)&&(mjd < 54832.0)) return 33;
	
	   if (mjd < 0.0) {
		   Log.e("tai_utc", "MJD before the beginning of the leap sec table");
	       return 0;
	   }
	
	   Log.e("tai_utc", "Input MJD out of bounds");
	       return 0;
	   }

	 public static double utcToTT(double mjd_utc){
 	        // compute the difference between TT and UTC
	     double tt_utc = (double)(tai_utc(mjd_utc) + TT_TAI);
	     double out = mjd_utc + tt_utc/86400.0;
	     return out;
	 }
}
