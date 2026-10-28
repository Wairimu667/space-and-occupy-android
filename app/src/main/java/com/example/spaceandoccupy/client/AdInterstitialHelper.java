package com.example.spaceandoccupy.client;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spaceandoccupy.R;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

public class AdInterstitialHelper {

    private static int videosScrolled = 0;
    private static final int AD_TRIGGER_INTERVAL = 4; // trigger ad every 4 videos

    public interface AdDismissListener {
        void onDismissed();
    }

    public static void onVideoSwiped(Context context, AdDismissListener listener) {
        videosScrolled++;
        if (videosScrolled >= AD_TRIGGER_INTERVAL) {
            videosScrolled = 0;
            showAdInterstitial(context, listener);
        } else if (listener != null) {
            listener.onDismissed();
        }
    }

    public static void showAdInterstitial(Context context, AdDismissListener listener) {
        // Increment ad impression in repository
        PropertyRepository.getInstance(context).recordAdImpression();

        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ad_interstitial, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvTimer = view.findViewById(R.id.tv_ad_timer);
        MaterialButton btnSkip = view.findViewById(R.id.btn_skip_ad);

        btnSkip.setEnabled(false);

        CountDownTimer timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                if (tvTimer != null) {
                    tvTimer.setText("Mandatory Ad • " + seconds + "s remaining");
                }
                if (btnSkip != null) {
                    btnSkip.setText("Skip Ad (" + seconds + "s)");
                }
            }

            @Override
            public void onFinish() {
                if (tvTimer != null) {
                    tvTimer.setText("You can now skip the ad");
                }
                if (btnSkip != null) {
                    btnSkip.setEnabled(true);
                    btnSkip.setText("Skip Ad ▶");
                }
            }
        };

        timer.start();

        btnSkip.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(context, "Returning to Property Feed", Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.onDismissed();
            }
        });

        dialog.show();
    }
}
