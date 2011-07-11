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

public class SiderealTime extends SkyCalculator {
	private PrecessionNutation precNut;
	private double eps;
	private double dpsi;
	private double omega;
	public double gmst;
	public double gst;
	private static final double JAN11997 = Time.jdToMJD(2450449.5);
	public static final int PRIORITY = PrecessionNutation.PRIORITY+1;
	public static final double UPDATE = 0.5/86400.;
	
	@Override
	public int getPriority() {
		return PRIORITY;
	}
	
	SiderealTime(PrecessionNutation pn) {
		super();
		
		precNut = pn;
	}
	
	@Override
	protected void update() {
		/* this is only needed in case precNut was updated too late */
		precNut.setTime(mjd_tt, mjd_ut1, mjd_utc); 
		
    	eps = precNut.eps();
    	dpsi = precNut.dpsi();
    	omega = precNut.omega();
    	updateGMST();
    	updateGST();
	}
	
	public double getUpdateInterval() {
		return UPDATE; /* once per second */
	}
	
	void updateGMST(){
    	// compute time since J2000 in days
		double T = mjd_ut1 - Time.MJD_J2000;
		// compute GMST in radians (CSR-02-01, p.21, MSODP Implementation)
		double gmst = 4.894961212823058751375704430 + T*(6.30038809898489355227651372 + T*(5.075209994113591478053805523E-15 - 9.25309756819433560067190688E-24*T));
		// quadrant check (return a value between 0 and 2PI)
		gmst = fixRadians(gmst);
	}
	
	/**
	 * Computes Greenwich Sidereal Time in radians
	 * Note: nutation must be called before this is called
	 */
    private void updateGST(){
    	double eqnEquinoxes = dpsi * Math.cos(eps);
    	double gst = gmst + eqnEquinoxes;
    	// if date is after Jan 1, 1997 use extra terms
    	if (mjd_tt > JAN11997){
    		double term1 = 0.00264 * Math.sin(omega);
    		double term2 = 0.000063 * Math.sin(2.0 * omega);
    		gst = gst + (term1 + term2)*ARCSEC2RAD;
    	}
    	// quadrant check
    	gst = fixRadians(gst);
    }
	
}
