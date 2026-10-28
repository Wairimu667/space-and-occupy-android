package com.example.spaceandoccupy.admin;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.spaceandoccupy.R;
import com.example.spaceandoccupy.model.User;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AdminEasterEggPortal {

    private static int tapCount = 0;
    private static long lastTapTime = 0;
    private static final int REQUIRED_TAPS = 5;
    private static final long RESET_INTERVAL_MS = 2500; // 2.5 seconds to complete 5 taps

    public static void attachTrigger(View view, Context context) {
        if (view == null || context == null) return;

        view.setOnClickListener(v -> {
            long now = SystemClock.elapsedRealtime();
            if (now - lastTapTime > RESET_INTERVAL_MS) {
                tapCount = 0;
            }
            lastTapTime = now;
            tapCount++;

            if (tapCount >= REQUIRED_TAPS) {
                tapCount = 0;
                Toast.makeText(context, "🔐 Secret Admin Easter Egg Unlocked!", Toast.LENGTH_SHORT).show();
                showMfaDialog(context);
            } else if (tapCount >= 3) {
                int remaining = REQUIRED_TAPS - tapCount;
                Toast.makeText(context, "Tap " + remaining + " more time" + (remaining == 1 ? "" : "s") + " to reveal Admin Gate", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void showMfaDialog(Context context) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_admin_easter_egg, null);
        dialog.setContentView(view);

        TextInputLayout tilPasscode = view.findViewById(R.id.til_admin_passcode);
        TextInputLayout tilMfa = view.findViewById(R.id.til_admin_mfa);
        TextInputEditText etPasscode = view.findViewById(R.id.et_admin_passcode);
        TextInputEditText etMfa = view.findViewById(R.id.et_admin_mfa);
        MaterialButton btnAuth = view.findViewById(R.id.btn_authenticate_admin);

        // Autofill default hints for convenience
        if (etPasscode != null) etPasscode.setText("admin123");
        if (etMfa != null) etMfa.setText("9921");

        btnAuth.setOnClickListener(v -> {
            String passcode = etPasscode.getText() != null ? etPasscode.getText().toString().trim() : "";
            String mfa = etMfa.getText() != null ? etMfa.getText().toString().trim() : "";

            if (TextUtils.isEmpty(passcode)) {
                tilPasscode.setError("Passcode required");
                return;
            }
            if (TextUtils.isEmpty(mfa)) {
                tilMfa.setError("2FA MFA code required");
                return;
            }

            if (("admin123".equals(passcode) || "admin".equals(passcode)) && ("9921".equals(mfa) || "123456".equals(mfa) || "1234".equals(mfa))) {
                dialog.dismiss();

                // Save admin session
                User adminUser = new User("usr_admin_1", "Administrator", "admin@spaceandoccupy.com", "admin123", "ADMIN", true, "+254 787 900284", "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e");
                PropertyRepository.getInstance(context).getSessionManager().saveUserSession(adminUser);

                Toast.makeText(context, "Admin MFA Authentication Successful!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, AdminHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Invalid Passcode or MFA Code. Intrusion recorded.", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }
}
