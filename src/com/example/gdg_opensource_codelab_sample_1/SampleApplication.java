package com.example.gdg_opensource_codelab_sample_1;

import android.app.Application;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;

public class SampleApplication extends Application {

    private static ImageManager sImageManager;

    @Override
    public void onCreate() {
        super.onCreate();
        LoaderSettings settings = new LoaderSettings.SettingsBuilder().withDisconnectOnEveryCall(true).build(this);
        sImageManager = new ImageManager(this, settings);        
    }

    public static final ImageManager getImageManager() {
        return sImageManager;
    }    

}//end of class
