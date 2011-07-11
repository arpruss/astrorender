/*****************************************************************************

    Copyright (c) 1998-2004 by Kevin S. Polk and (c) 2010-11 Alexander R. Pruss

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package mobi.pruss.astrorender;

public class SolarSystemBody extends SkyCalculator {
	public double[] equatorialXYZ;
	public double distance;
	public double brightness;
	public double[] heliocentricXYZ;
	double sinM;
	double cosM;
	double tt_cent;
	static final double TOL = 0.0000001;
	
	protected void update() {
		super.update();
		tt_cent = mjd_tt / 36525.;
	}
	
	protected boolean Kepler(double mAnom, double ee){
		int count = 0;
		double E0, E1; // Eccentric Anomaly
		double se, ce, temp;
		
		E1 = mAnom;
		do {
			E0 = E1;
			se = Math.sin(E0);
			ce = Math.cos(E0);
			E1 = E0 + (mAnom + ee * se - E0)/(1 - ee * ce);
			count++;
			} while (Math.abs(E1 - E0) > TOL && count < 10);

		se = Math.sin(E1);
		ce = Math.cos(E1);
		temp = 1 - ee * ce;
		cosM = (ce - ee) / temp;
		sinM = Math.sqrt(1 - ee * ee) * se / temp;
		
		return (count < 10); // True if solution is converged (i.e. precise).
	}
}
