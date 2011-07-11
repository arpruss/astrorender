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

public class Planet extends SolarSystemBody {
	double[] elements;
	
	static final int AA=0;
	static final int EE=1;
	static final int II=2;
	static final int LON=3;
	static final int ARG=4;
	static final int MLON=5;
	double updateInterval;
	
	public Planet(double u, double[] e) {
		elements = e.clone();
		updateInterval = u;
	}
	
	protected double getUpdateInterval() {
		return updateInterval;
	}
	
	protected void update() {
		PlanetRect();
	}

	void PlanetRect(){
		double ee;
		double[] mel = new double[6];
		double mAnom, LonPeri;
		double cv, sv, temp;
		double pP;
		int nn;
		double cw, sw;
		double cL, sL;
		double ci, si;
		
		for (nn = 0; nn < 6; nn++){
			mel[nn] = elements[2*nn] + elements[2*nn+1] * tt_cent;
		}
		
		ee   = mel[EE];
		mel[II]   *= DEG2RAD;
		mel[LON]  *= DEG2RAD;
		
		mAnom = fixRadians(((mel[MLON] - mel[ARG]) + 360) * DEG2RAD); 
		mel[ARG] *= DEG2RAD;
		LonPeri = (mel[ARG] - mel[LON]);
		
		Kepler(mAnom, ee); // This sets cosM and sinM

		pP = mel[AA] * (1 - ee * ee);
		temp = pP / (1 + ee * cosM);
		
		cv = cosM * temp; // rx      here we are saving stack space
		sv = sinM * temp; // ry      rx = cv * temp, ry = sv * temp
		
		sw = Math.sin(LonPeri);
		cw = Math.cos(LonPeri);
		
		sL = Math.sin(mel[LON]);
		cL = Math.cos(mel[LON]);

		si = Math.sin(mel[II]);
		ci = Math.cos(mel[II]);

		heliocentricXYZ = new double[] {
			cv * (cw * cL - sw * sL * ci)  
								- sv * (sw * cL + cw * sL * ci), // remember, rx,ry = cv,sv
			cv * (cw * sL + sw * cL * ci)
								- sv * (sw * sL - cw * cL * ci),
			cv * sw * si
								+ sv * cw * si
	 	};
	}
}
