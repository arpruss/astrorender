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

public class SkyCalculator {
	double skyTime;
	static final double DEG2RAD = Math.PI/180.;
	static final double RAD2DEG = 180./Math.PI;
	static final double ARCSEC2RAD = Math.PI/(180.*60.*60.);
	
	SkyCalculator() {
		skyTime =  AstroTime.INVALID_TIME;
	}
	
	double getUpdateInterval() {
		return 1/86400.;
	}
	
	void setSkyTime(double mjd) {
		boolean needUpdate;
		needUpdate = ! AstroTime.isValid(skyTime) || 
			Math.abs(skyTime - mjd) >= getUpdateInterval(); 
		skyTime = mjd;
		if (needUpdate) {
			update();
		}
	}

	protected void update() {
	}
}
