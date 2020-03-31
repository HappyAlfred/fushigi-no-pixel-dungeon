/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.fushiginopixel.fushiginopixeldungeon.windows;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Looper;
import android.widget.Toast;

import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.MyApplication;
import com.fushiginopixel.fushiginopixeldungeon.SPDSettings;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.PixelScene;
import com.fushiginopixel.fushiginopixeldungeon.ui.RedButton;
import com.fushiginopixel.fushiginopixeldungeon.ui.RenderedTextMultiline;
import com.fushiginopixel.fushiginopixeldungeon.ui.Window;
import com.watabou.noosa.Game;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class WndDonate extends Window {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;

	private static final int MARGIN = 2;
	private static final int BUTTON_HEIGHT = 20;


	String[] options = new String[]{
		Messages.get(this,"alipay"),
		//Messages.get(this,"paypal")
	};
	public WndDonate() {
		super();

		int width = SPDSettings.landscape() ? WIDTH_L : WIDTH_P;

		RenderedTextMultiline tfTitle = PixelScene.renderMultiline(Messages.get(this, "title"), 9 );
		tfTitle.hardlight( TITLE_COLOR );
		tfTitle.setPos(MARGIN, MARGIN);
		tfTitle.maxWidth(width - MARGIN * 2);
		add( tfTitle );

		RenderedTextMultiline tfMesage = PixelScene.renderMultiline( 6 );
		tfMesage.text(Messages.get(this, "description"), width - MARGIN * 2);
		tfMesage.setPos( MARGIN, tfTitle.top() + tfTitle.height() + MARGIN );
		add( tfMesage );

		float pos = tfMesage.bottom() + MARGIN;
		for (int i=0; i < options.length; i++) {
			final int index = i;
			RedButton btn = new RedButton( options[i] ) {
				@Override
				protected void onClick() {
					hide();
					onSelect( index );
				}
			};
			btn.setRect( MARGIN, pos, width - MARGIN * 2, BUTTON_HEIGHT );
			add( btn );

			pos += BUTTON_HEIGHT + MARGIN;
		}

		resize( width, (int)pos );
	}

	private void onSelect( int index ) {
		switch (index){
			case (0):{
				new Thread() {
					@Override
					public void run() {
						Looper.prepare();
						openALiPay(Fushiginopixeldungeon.instance);
						Looper.loop();
					}
				}.start();
				break;
			}
			case (1):{
				Intent intent= new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse("paypal.me/HappyAlfred");
				intent.setData(content_url);
				intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
				Fushiginopixeldungeon.instance.startActivity(intent);
			}
		}
	};
	/**
	 * 判断支付宝客户端是否已安装，建议调用转账前检查
	 * @return 支付宝客户端是否已安装
	 */
	private static boolean hasInstalledAlipayClient() {
		String pkgName = "com.eg.android.AlipayGphone";
		PackageInfo packageInfo;
		try {
			packageInfo = MyApplication.getContext().getPackageManager().getPackageInfo(pkgName, 0);
		} catch (PackageManager.NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if(packageInfo == null) {
			return false;
		} else {
			return true;//true为安装了，false为未安装
		}
	}

	/***
	 * 支付宝转账
	 * @param activity
	 * **/
	public static void openALiPay(Activity activity){
		String url1="intent://platformapi/startapp?saId=10000007&" +
				"clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Ffkx07449fcoboudahuipuc5%3F_s" +
				"%3Dweb-other&_t=1472443966571#Intent;" +
				"scheme=alipayqr;package=com.eg.android.AlipayGphone;end";
		//String url1=activity.getResources().getString(R.string.alipay);
		Intent intent = null;
		Toast.makeText(MyApplication.getContext(),"感谢您的捐赠！",Toast.LENGTH_SHORT).show();
		if(hasInstalledAlipayClient()){
			try {
				intent = Intent.parseUri(url1 ,Intent.URI_INTENT_SCHEME );
				activity.startActivity(intent);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				Toast.makeText(MyApplication.getContext(),"出错啦",Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(MyApplication.getContext(),"您未安装支付宝哦！",Toast.LENGTH_SHORT).show();
		}
	}
}
