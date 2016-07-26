package sg.edu.nus.bombsquad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ForgetPassword extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: ForgetPassword");

        onReset();
    }

    private void onReset() {
        Button bReset = (Button) findViewById(R.id.buttonResetMyPassword);
        bReset.setOnClickListener(new View.OnClickListener() {
            EditText etUsername = (EditText) findViewById(R.id.editTextUserIdPasswordReset);
            EditText etNewPassword = (EditText) findViewById(R.id.editTextNewPassword);
            EditText etConfirmNewPassword = (EditText) findViewById(R.id.editTextConfirmNewPassword);

            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String confirmNewPassword = etConfirmNewPassword.getText().toString();

                if (username.isEmpty()) {
                    etUsername.setError("Field is empty");
                } else if (newPassword.isEmpty()) {
                    etNewPassword.setError("Field is empty");
                } else if (confirmNewPassword.isEmpty()) {
                    etConfirmNewPassword.setError("Field is empty");
                } else if (newPassword.length() < 8) {
                    etConfirmNewPassword.setText("");
                    etNewPassword.setText("");
                    etNewPassword.setError("Password must be more than 8 characters");
                } else if (!confirmNewPassword.equals(newPassword)) {
                    etConfirmNewPassword.setText("");
                    etNewPassword.setText("");
                    etConfirmNewPassword.setError("Mismatch password");
                } else {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody postData = new FormBody.Builder()
                            .add("username", username)
                            .add("password", newPassword)
                            .build();
                    Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/resetPassword.php").post(postData).build();

                    client.newCall(request)
                            .enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    System.out.println("FAIL");
                                }

                                @Override
                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                    try {
                                        JSONObject result = new JSONObject(response.body().string());

                                        boolean success = result.getBoolean("success");
                                        response.body().close();
                                        if (success) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
                                                    builder.setMessage("You have successfully reset your password")
                                                            .setPositiveButton("Back to login", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Intent back = new Intent(ForgetPassword.this, LoginPage.class);
                                                                    startActivity(back);
                                                                }
                                                            })
                                                            .create()
                                                            .show();
                                                }
                                            });
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), "Username does not exist", Toast.LENGTH_SHORT).show();
                                                    etUsername.setText("");
                                                    etNewPassword.setText("");
                                                    etConfirmNewPassword.setText("");
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        /*e.printStackTrace();*/
                                    }

                                }
                            });
                }
            }
        });
    }
}