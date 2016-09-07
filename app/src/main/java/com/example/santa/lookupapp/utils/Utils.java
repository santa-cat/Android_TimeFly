/*
 * Copyright (C) 2015. Jared Rummler <jared.rummler@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.santa.lookupapp.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;

import com.example.santa.lookupapp.R;

public class Utils {

  public static String getLabel(Context context, String packageName){
    PackageManager pm = context.getPackageManager();
    try {
//      Log.d("DEBUG", "package name is =" +packageName);
      ApplicationInfo AI = pm.getApplicationInfo(packageName, 0);
      return AI.loadLabel(pm).toString();
    } catch(PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Drawable getIcon(Context context, String packageName){
//    Log.d("DEBUG", "package name is =" +packageName);
    PackageManager pm = context.getPackageManager();
    try {
      ApplicationInfo AI = pm.getApplicationInfo(packageName, 0);
      return AI.loadIcon(pm);
    } catch(PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return context.getResources().getDrawable( R.drawable.ic_default);
  }


  public static int toPx(Context context, float dp) {
    Resources r = context.getResources();
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    return Math.round(px);
  }


}
