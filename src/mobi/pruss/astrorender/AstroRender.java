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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.*;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class AstroRender extends Activity {
	private AstroSurfaceView asv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        asv = new AstroSurfaceView(this);
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
    
}

class AstroSurfaceView extends GLSurfaceView {
	Context context;
	AstroRenderer renderer;
	private float oldX;
	private float oldY;	
	
	AstroSurfaceView(Context c) {
		super(c);
		context = c;

		renderer = new AstroRenderer(context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
    @Override public boolean onTouchEvent(MotionEvent e) {
    	float x = e.getX();
    	float y = e.getY();
    	
    	if (e.getAction()==MotionEvent.ACTION_MOVE) {
    		float scale = 180f / 320 / renderer.getZoom();
    		
    		renderer.adjustRaDec(-(x-oldX)*scale, (y-oldY)*scale);
    		requestRender();
    	}
    	
    	oldX = x;
    	oldY = y;
    	
    	return true;
    }
	
}

class AstroRenderer implements GLSurfaceView.Renderer {
    private FloatBuffer vertexBuffer;
    private FloatBuffer texVertexBuffer;
    private float width;
    private float height;
    private int COUNT = 0;
    private static float angle = 0;
    private Context context;
    private int star;
    private float[] stars;
    private final float MAXZOOM = 512 * 1024;
    private final float TWEAK = 1.01f;
    private float ra = 0;
    private float dec = 80;
    private float zoom = 2;
    
    public AstroRenderer(Context c) {
    	context = c;
    }
    
    public float getZoom() {
    	return zoom;
    }
    
    public void zoomIn() {
    	zoom *= 2;
    	if (zoom>MAXZOOM) {
    		zoom = MAXZOOM;
    	}
    }
    
    public void zoomOut() {
    	zoom /= 2;
    	if (zoom<1) {
    		zoom = 1;
    	}
    }
    
    public void adjustRaDec(float deltaRa, float deltaDec) {
    	ra += deltaRa;
    	ra = ra % 360f;
    	dec += deltaDec;
    	if (dec>90)
    		dec = 90;
    	if (dec<-90)
    		dec = -90;
    }

    private void cross(float[] out, float[] a, float[] b) {
    	out[0] = a[1]*b[2]-a[2]*b[1];
    	out[1] = a[2]*b[0]-a[0]*b[2];
    	out[2] = a[0]*b[1]-a[1]*b[0];
    }

	private void makeTriangle(float x, float y, float z, float r) {
		float v[] = new float[3];
		float basis1[] = new float[3];
		float basis2[] = new float[3];
		
		float rxy = FloatMath.sqrt(x*x+y*y);
		float ryz = FloatMath.sqrt(y*y+z*z);
		
		if (rxy == 0) {
			basis1[0] = 1;
			basis1[1] = 0;
			basis1[2] = 0;
			basis2[0] = 0;
			basis2[1] = 1;
			basis2[2] = 0;
		}
		else if (ryz == 0) {
			basis1[0] = 0;
			basis1[1] = 1;
			basis1[2] = 0;
			basis2[0] = 0;
			basis2[1] = 0;
			basis2[2] = 1;
		}
		else {
		    basis1[0] = -y / rxy;
		    basis1[1] = x /rxy;
		    basis1[2] = 0;
		    
		    float in[] = {x,y,z};
		    cross(basis2, basis1, in);
		}
		
		for (int i=0; i<3; i++) {
			float a = (float)(r*Math.cos(2*Math.PI*i/3));
			float b = (float)(r*Math.sin(2*Math.PI*i/3));
			
			v[0] = x+basis1[0]*a + basis2[0]*b; 
			v[1] = y+basis1[1]*a + basis2[1]*b; 
			v[2] = z+basis1[2]*a + basis2[2]*b; 
			
			vertexBuffer.put(v);
		}
	}

	private void loadStars(int id) {
		InputStream in = context.getResources().openRawResource(id);
		byte[] b; 
	
		try {
			COUNT = in.available() / 16;
			b = new byte[COUNT*16];
			in.read(b);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			COUNT = 0;
			return;
		}
		ByteBuffer bb = ByteBuffer.wrap(b);
		bb.order(ByteOrder.BIG_ENDIAN);
		FloatBuffer fbb = bb.asFloatBuffer();
		stars = new float[COUNT*4];
		fbb.get(stars);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
        vertexBuffer.position(0);
        texVertexBuffer.position(0);
		
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        if (angle < 0.001f && -0.001f <angle) {
//        	Log.v("angle", "t="+System.currentTimeMillis());
//        }

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glScalef(zoom, zoom, zoom);
        gl.glRotatef(90-dec, 1f, 0f, 0f);
        gl.glRotatef(ra, 0f, 0f, 1f); 
//        gl.glRotatef(-60, 1f, 0f, 0f);
//        gl.glRotatef(angle, 0f, 0f, 1f);
//        angle += .1;

//        if (360<=angle)
//        	angle = 0;

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texVertexBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, COUNT*3);

    	gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    	gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	

	@Override
	public void onSurfaceChanged(GL10 gl, int w, int h) {
	      width = (float)w;
	      height = (float)h;
	   
	      // Set the viewport (display area) to cover the entire window
	      gl.glViewport(0, 0, w, h);
	  
		  gl.glMatrixMode(GL10.GL_MODELVIEW);  
	      gl.glLoadIdentity();                 

	      gl.glMatrixMode(GL10.GL_PROJECTION);
	      gl.glLoadIdentity();                
	      if (width<height) {
	    	    float aspect = height/width;
				gl.glOrthof(-TWEAK, TWEAK, aspect*TWEAK, -aspect*TWEAK, 0f, -TWEAK*MAXZOOM);
	      }
	      else {
	    	  	float aspect = width/height;
				gl.glOrthof(-aspect*TWEAK, aspect*TWEAK, TWEAK, -TWEAK, 0f, -TWEAK*MAXZOOM);
	      }
	}

	// Get a new texture id.  Public domain code from
	// http://blog.poweredbytoast.com/loading-opengl-textures-in-android
	private static int newTextureID(GL10 gl) {
	    int[] temp = new int[1];
	    gl.glGenTextures(1, temp, 0);
	    return temp[0];
	}

	// Will load a texture out of a drawable resource file, and return an OpenGL texture ID.
	// Public domain code from
	// http://blog.poweredbytoast.com/loading-opengl-textures-in-android
	private int loadTexture(GL10 gl, int resource) {
	    
	    // In which ID will we be storing this texture?
	    int id = newTextureID(gl);
	    
	    // We need to flip the textures vertically:
	    android.graphics.Matrix flip = new android.graphics.Matrix();
	    flip.postScale(1f, -1f);
	    
	    // This will tell the BitmapFactory to not scale based on the device's pixel density:
	    // (Thanks to Matthew Marshall for this bit)
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inScaled = false;
	    
	    // Load up, and flip the texture:
	    Bitmap temp = BitmapFactory.decodeResource(context.getResources(), resource, opts);
	    Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), flip, true);
	    temp.recycle();
	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, id);
	    
	    // Set all of our texture parameters:
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

	    // Generate, and load up all of the mipmaps:
	    for(int level=0, height = bmp.getHeight(), width = bmp.getWidth(); true; level++) {
	        // Push the bitmap onto the GPU:
	    	Log.v("scale","w="+width);
	        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bmp, 0);

	        // We need to stop when the texture is 1x1:
	        if(height==1 && width==1) break;

	        // Resize, and let's go again:
	        width >>= 1; height >>= 1;
	        if(width<1)  width = 1;
	        if(height<1) height = 1;

	        Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, width, height, true);
	        bmp.recycle();
	        bmp = bmp2;
	    }

	    bmp.recycle();

	    return id;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.v("AR","onSurfaceCreated");

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepthf(MAXZOOM*TWEAK);

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        loadStars(R.raw.xyz);

        ByteBuffer vv = ByteBuffer.allocateDirect(3 * COUNT * 3 * 4);
        vv.order(ByteOrder.nativeOrder());
        vertexBuffer = vv.asFloatBuffer();

        ByteBuffer tv = ByteBuffer.allocateDirect(3 * COUNT * 2 * 4);
        tv.order(ByteOrder.nativeOrder());
        texVertexBuffer = tv.asFloatBuffer();

		for (int i=0; i<COUNT; i++) {
			for (int j = 0; j<3; j++) {
				float x = (float) (.5 + .5*Math.cos(2*Math.PI/3 * j));
				float y = (float) (.5 + .5*Math.sin(2*Math.PI/3 * j));
				texVertexBuffer.put(x);
				texVertexBuffer.put(y);
			}
		}

		for(int i = 0; i<COUNT;i++) {
			makeTriangle(stars[4*i],stars[4*i+1],stars[4*i+2],
					(float) (Math.pow(1.5849, -stars[4*i+3])*.11));
		}

        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        star = loadTexture(gl, R.drawable.star_128_10_16_4);
        
        Log.v("Star", "id="+star);
	}	
}
