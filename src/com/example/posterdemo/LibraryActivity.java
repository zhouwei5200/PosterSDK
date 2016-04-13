package com.example.posterdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LibraryActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.float_window_small);
		final MyPoster myPoster = MyPoster.getInstance();
		Button close = (Button) findViewById(R.id.close);
		myPoster.requestPoster(LibraryActivity.this);
		String version = myPoster.obtainVersion(LibraryActivity.this);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myPoster.startPost(LibraryActivity.this);

			}
		});

	}

}
