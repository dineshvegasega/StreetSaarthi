package com.streetsaarthi.nasvi.test;

import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.common.IntentSenderForResultStarter;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.streetsaarthi.nasvi.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AA extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test);

        inAppUpdates();
    }


    private final int APP_UPDATE_REQUEST_CODE=1230;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    private void inAppUpdates() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            Log.e("inAppUpdates","appUpdateInfo.updateAvailability() "+ appUpdateInfo.updateAvailability());
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            this,
                            APP_UPDATE_REQUEST_CODE
                    );
                } catch (IntentSender.SendIntentException e) {
                    Log.e("inAppUpdates","IntentSender.SendIntentException"+ e);
                }
            }
        });

        installStateUpdatedListener = state -> {
            Log.e("inAppUpdates",String.valueOf(state.installStatus()));
            if (state.installStatus() == InstallStatus.DOWNLOADING) {
                long bytesDownloaded = state.bytesDownloaded();
                long totalBytesToDownload = state.totalBytesToDownload();
                Log.e("inAppUpdates","bytesDownloaded "+ bytesDownloaded+" / "+totalBytesToDownload);
                // Update UI to show download progress.
            } else if (state.installStatus() == InstallStatus.DOWNLOADED) {
                Log.e("inAppUpdates","Update is downloaded and ready to install ");

                // Notify the user and request installation.
            } else if (state.installStatus() == InstallStatus.INSTALLING) {
                Log.e("inAppUpdates","Update is being installed");

                // Update UI to show installation progress.
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                Log.e("inAppUpdates","Update is installed");

                // Notify the user and perform any necessary actions.
            } else if (state.installStatus() == InstallStatus.FAILED) {
                Log.e("inAppUpdates","Update failed to install");
                // Notify the user and handle the error.
            }
        };
        appUpdateManager.registerListener(installStateUpdatedListener);

    }

    @Override
    protected void onDestroy() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
        super.onDestroy();
    }
}
