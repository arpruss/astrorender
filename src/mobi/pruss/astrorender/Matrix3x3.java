package mobi.pruss.astrorender;

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
//


public class Matrix3x3 {
    double[][] m;

    /* By default do identity matrix */
    public Matrix3x3() {
    	m = new double[3][3];
    }
    
    public Matrix3x3(int axis, double phi) {
    	m = new double[3][3];
    	setRotation(axis, phi);
    }
    
    public Matrix3x3(double phi, double theta) {
    	m = new double[3][3];
    	setRotation(phi, theta);
    }
    
    public void copyTo(Matrix3x3 m1) {
    	for (int i=0; i<3; i++) {
    		for (int j=0; j<3; j++) {
    			m1.m[i][j] = m[i][j];
    		}
    	}
    }

    public void setIdentity() {
    	for (int i=0; i<3; i++) {
    		for (int j=0; j<3; j++) {
    			m[i][j] = (i == j) ? 1. : 0.;
    		}
    	}    	
    }
    
    public Matrix3x3 times(double x) {
    	Matrix3x3 out = new Matrix3x3();
    	for (int i=0; i<3; i++) {
    		for (int j=0; j<3; j++) {
    			out.m[i][j] = x * m[i][j];
    		}
    	}    	
    	
    	return out;
    }
    
    public double[] times(double[] v) {
    	double[] v1 = new double[3];
    	
    	for (int i=0; i<3; i++) {
    		v1[i] = m[i][0]*v[0]+m[i][1]*v[1]+m[i][2]*v[2];
    	}
    	
    	return v1;
    }
    
    public void times(double[] vOut, double[] v) {
    	for (int i=0; i<3; i++) {
    		vOut[i] = m[i][0]*v[0]+m[i][1]*v[1]+m[i][2]*v[2];
    	}
    }
    
    public void times(double[] vOut, float[] v) {
    	for (int i=0; i<3; i++) {
    		vOut[i] = m[i][0]*(double)v[0]+m[i][1]*(double)v[1]+m[i][2]*(double)v[2];
    	}
    }
    
    public Matrix3x3 times(Matrix3x3 m1) {
    	Matrix3x3 out = new Matrix3x3();
    	
    	for (int i=0; i<3; i++) {
    		for (int j=0; j<3; j++) {
    			out.m[i][j] = m[i][0]*m1.m[0][j]+m[i][1]*m1.m[1][j]+m[i][2]*m1.m[2][j];
    		}
    	}
    	
    	return out;
    }
    
    public void setRotation(double phi, double theta) {
    	double cp = Math.cos(phi);
    	double sp = Math.sin(phi);
    	double ct = Math.cos(theta);
    	double st = Math.sin(theta);
    	
    	m[0][0]=ct;  		m[0][1]=-st; 	m[0][2]=0;
    	m[1][0]=cp*st;	 	m[1][1]=cp*ct;	m[1][2]=sp;
    	m[2][0]=-sp*st;		m[2][1]=-sp*ct; m[2][2]=cp;
    }
    
    public void setRotation(int axis, double phi) {
        double cos_phi = Math.cos(phi);
        double sin_phi = Math.sin(phi);
        double msin_phi = -1.0 * sin_phi;
        
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                m[i][j] = 0.0;
            }
            m[i][i] = cos_phi;
        }
        switch (axis) {
            case 1:      // Rotate about 1st axis
                m[0][0] = 1.0;
                m[1][2] = sin_phi;
                m[2][1] = msin_phi;
                break;
            case 2:      // Rotate about 2nd axis
                m[1][1] = 1.0;
                m[0][2] = msin_phi;
                m[2][0] = sin_phi;
                break;
            case 3:      // Rotate about 3rd axis
                m[2][2] = 1.0;
                m[0][1] = sin_phi;
                m[1][0] = msin_phi;
                break;
            default:
            	break;
        }
    }
    
    public void toGLMatrix(float[] f, int offset) {
		f[offset+0]=(float)m[0][0]; f[offset+4]=(float)m[0][1]; f[offset+8]=(float)m[0][2]; f[offset+12]=0f;
		f[offset+1]=(float)m[1][0]; f[offset+5]=(float)m[1][1]; f[offset+9]=(float)m[1][2]; f[offset+13]=0f;
		f[offset+2]=(float)m[2][0]; f[offset+6]=(float)m[2][1]; f[offset+10]=(float)m[2][2]; f[offset+14]=0f;
		f[offset+3]=0f;		 f[offset+7]=0f;	  f[offset+11]=0f;		f[offset+15]=0f;
    }
}
