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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;

public class License extends Activity {

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.html);
		WebView w = (WebView)findViewById(R.id.html);
		w.loadUrl("file:///android_asset/license.html");
	}
}
