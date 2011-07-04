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

import java.util.ArrayList;
import java.util.List;

/* The official time system for AstroObserver is MJD rather than JD
 * in order to have more significant figures;
 */

public class AstroTime {
	static final double MJD0 = 2400000.5;
	static final double MJD_J2000 = 51544.5;
	static final double INVALID_TIME = Double.NaN;
	SkyCalculator[] calculators;

	static double skyTime; /* this is the time being shown in the sky */
	
	AstroTime(SkyCalculator... c) {
		calculators = c;
	}

	void setSkyTime(double mjd) {
		skyTime = mjd;
		
		for (SkyCalculator c:calculators) {
			c.setSkyTime(mjd);
		}
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
}
