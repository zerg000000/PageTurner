/*
 * Copyright (C) 2011 Alex Kuiper
 * 
 * This file is part of PageTurner
 *
 * PageTurner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PageTurner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PageTurner.  If not, see <http://www.gnu.org/licenses/>.*
 */

package net.nightwhistler.pageturner.activity;

import net.nightwhistler.pageturner.Configuration;
import net.nightwhistler.pageturner.R;
import roboguice.RoboGuice;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockPreferenceActivity;

public class PageTurnerPrefsActivity extends RoboSherlockPreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme( RoboGuice.getInjector(this).getInstance(Configuration.class).getTheme() );
		super.onCreate(savedInstanceState);
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		if ( ! settings.contains("device_name") ) {
	 	   SharedPreferences.Editor editor = settings.edit();
	 	   editor.putString("device_name", Build.MODEL );
	 	   // Commit the edits!
	 	   editor.commit();			
		}
		
		addPreferencesFromResource(R.xml.pageturner_prefs);

		final PreferenceScreen screen = getPreferenceScreen();
		if(!Configuration.IS_NOOK_TOUCH) {
			// Disable Nook-specific preferences
			screen.removePreference(screen.findPreference("nook_prefs"));
		}
		else {
			// Enable only builtin fonts on Nook Touch. This is because
			// Nook Touch can't render OTF fonts (causes segfault), also most thin weighted fonts look terrible
			// because the Nook Touch libskia uses antialiasing, but then the Nook Touch screen can't adequately
			// render any antialiasing...
			final String[] font_prefs = { "font_face", "serif_font", "sans_serif_font" };
			for(String font_pref : font_prefs) {
				ListPreference pref = (ListPreference) screen.findPreference(font_pref);
				pref.setEntries(getResources().getStringArray(R.array.builtinFontLabels));
				pref.setEntryValues(getResources().getStringArray(R.array.builtinFonts));
			}
		}
	}
}
