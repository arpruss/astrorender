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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.*;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ZoomButtonsController;
import android.widget.ZoomButtonsController.OnZoomListener;

public class AstroRender extends Activity {
	private AstroSurfaceView asv;
	public ZoomButtonsController zoomControl;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
	    return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.license:
    		Intent i = new Intent(this, License.class);
    		startActivity(i);
    		
    		return true;
    	}
    	
    	return false;
    }

    		@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        asv = new AstroSurfaceView(this);
        zoomControl = new ZoomButtonsController(asv);
        asv.setZoomControl(zoomControl);
        zoomControl.setOnZoomListener(asv);
		zoomControl.setFocusable(true);
		zoomControl.setZoomSpeed(500);
        this.setContentView(asv);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
    	int action = event.getAction();
    	
    	Log.v("key", ""+event.getKeyCode());
    
    	switch (event.getKeyCode()) {
    	case KeyEvent.KEYCODE_VOLUME_UP:
        	if (action != KeyEvent.ACTION_UP) 
        		asv.renderer.zoomIn();
    		asv.requestRender();
    		return true;
    	case KeyEvent.KEYCODE_VOLUME_DOWN:
        	if (action != KeyEvent.ACTION_UP) 
        		asv.renderer.zoomOut();
    		asv.requestRender();
    		return true;
    	}
    	
    	return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	asv.onPause();
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	asv.onResume();
    }
    
    @Override
	public void onDetachedFromWindow() {
    	zoomControl.setVisible(false);
    }    
}

