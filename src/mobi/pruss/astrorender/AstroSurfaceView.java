package mobi.pruss.astrorender;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ZoomButtonsController;
import android.widget.ZoomButtonsController.OnZoomListener;

public class AstroSurfaceView extends GLSurfaceView 
	implements OnZoomListener {
	Context context;
	AstroRenderer renderer;
	private float oldX;
	private float oldY;	
	private ZoomButtonsController zoomControl;
	private long updateDelayMillis = 500;
	private Timer updateTimer;
	private Calculators calculators;
	
	AstroSurfaceView(Context c) {
		super(c);
		context = c;

		renderer = new AstroRenderer(context, new Calculators());
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        calculators = new Calculators();        
	}
	
	public void setZoomControl(ZoomButtonsController z) {
		zoomControl = z;
	}
	
	public void onVisibilityChanged(boolean visible) {		
	}
	
	public void onZoom(boolean zoomIn) {
		if (zoomIn) 
			renderer.zoomIn();
		else
			renderer.zoomOut();
		requestRender();
	}
	
    @Override public boolean onTouchEvent(MotionEvent e) {
//    	super.onTouchEvent(e);
    	
    	float x = e.getX();
    	float y = e.getY();
		
		zoomControl.setVisible(true);
    	
    	if (e.getAction()==MotionEvent.ACTION_MOVE) {
    		float scale = 180f / 320 / renderer.getZoom();
    		double modDec = Math.abs((double)renderer.getDecDecimal())-
    							180f/renderer.getZoom();
    		if (modDec < 0)
    			modDec = 0;
    		
    		renderer.adjustRaDec(-(x-oldX)*scale/(float)Math.cos(modDec*Math.PI/180.), 
    				(y-oldY)*scale);
    		requestRender();
    	}
    	
    	oldX = x;
    	oldY = y;
    	
    	return true;
    }
	
    @Override
    public void onPause() {
    	super.onPause();
    	renderer.savePrefs();
    	updateTimer.cancel();
    }

    @Override
    public void onResume() {
    	super.onResume();
        updateTimer = new Timer();
        updateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Log.v("AstroRender", "requestRender");
				requestRender();
			}
        }, 0, updateDelayMillis);        
    }
}

