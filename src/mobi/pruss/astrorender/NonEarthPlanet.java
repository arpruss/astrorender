/*****************************************************************************

    Copyright (c) 2011 Alexander R. Pruss

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

public class NonEarthPlanet extends Planet {
	public double[] equatorialXYZ;
	public double distance;
	private Planet earth;
	public final int PRIORITY = Earth.PRIORITY+1;
	
	public int getPriority() {
		return PRIORITY;
	}
	
	public NonEarthPlanet(Planet e, double updateInterval, double[] el) {
		super(updateInterval, el);
		earth = e;
	}
	
	protected void update() {
		super.update();
		earth.update(); /* TODO: may be removed if safe calculation order is used */
		double x = heliocentricXYZ[0] - earth.heliocentricXYZ[0]; 	
		double y = heliocentricXYZ[1] - earth.heliocentricXYZ[1]; 	
		double z = heliocentricXYZ[2] - earth.heliocentricXYZ[2];
		
		distance = Math.sqrt(x*x+y*y+z*z);
		
		equatorialXYZ = new double[] {
			x / distance, y / distance, z / distance
		};
	}
}
