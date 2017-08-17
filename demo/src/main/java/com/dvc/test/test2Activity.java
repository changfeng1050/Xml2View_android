package com.dvc.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dvc.xml.XmlDynamicView;
import com.dvc.xml.XmlDynamicViewId;
import com.xiaoji.gwlibrary.view.RoundButton;
import com.xiaoji.gwlibrary.view.SeekBarRelativeLayout;

public class test2Activity extends Activity implements View.OnClickListener{
	@XmlDynamicViewId(id = "btn_config_share_del_cancel")
	public Button btn_config_share_del_cancel;
	@XmlDynamicViewId(id = "btn_config_share_del_ok")
	public Button btn_config_share_del_ok;
	@XmlDynamicViewId(id = "speed_edit_decrease")
	public TextView speed_edit_decrease;
	@XmlDynamicViewId(id = "speed_edit_increase")
	public TextView speed_edit_increase;
	@XmlDynamicViewId(id = "hitspeed_edit_value_seekbar")
	public SeekBarRelativeLayout hitspeed_edit_value_seekbar;
	@XmlDynamicViewId(id = "ok_btn")
	public RoundButton ok_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View sampleView = XmlDynamicView.createView(this, "layout/example2.xml", this);
		btn_config_share_del_cancel.setOnClickListener(this);
		btn_config_share_del_ok.setOnClickListener(this);
		ok_btn.setOnClickListener(this);
		speed_edit_decrease.setOnClickListener(this);
		speed_edit_increase.setOnClickListener(this);
		hitspeed_edit_value_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				Toast.makeText(getApplicationContext(), "onProgressChanged:"+i, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		setContentView(sampleView);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == btn_config_share_del_cancel.getId()){
			Toast.makeText(getApplicationContext(), "按了btn_config_share_del_cancel", Toast.LENGTH_SHORT).show();
		}
		else if (arg0.getId() == btn_config_share_del_ok.getId()){
			Toast.makeText(getApplicationContext(), "按了btn_config_share_del_ok", Toast.LENGTH_SHORT).show();
		}else if (arg0.getId() == speed_edit_decrease.getId()){
			Toast.makeText(getApplicationContext(), "click decrease", Toast.LENGTH_SHORT).show();
		}
		else if (arg0.getId() == speed_edit_increase.getId()){
			Toast.makeText(getApplicationContext(), "click increase", Toast.LENGTH_SHORT).show();
		}
		else if (arg0.getId() == ok_btn.getId()){
			Toast.makeText(getApplicationContext(), "click ok_btn", Toast.LENGTH_SHORT).show();
		}
	}
}
