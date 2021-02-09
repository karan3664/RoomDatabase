package com.karan_brahmaxatriya.roomdatabasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.karan_brahmaxatriya.roomdatabasedemo.database.DatabaseClient;
import com.karan_brahmaxatriya.roomdatabasedemo.database.Task;
import com.karan_brahmaxatriya.roomdatabasedemo.lib.BitmapManager;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {
    private EditText edtFullName, edtMobile, edtEmail, edtDateofBirth;
    RadioGroup radioGroupSex;
    Spinner spinner;
    RadioButton radioButton;
    RadioButton rdMale, edFemale;
    String[] Devlopers = {"Android", "PHP", "Java Developer", "Laravel", "Other"};
    ArrayAdapter arrayAdapter;
    Button delete, save;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ImageView image;
    public static final int CAMERA_REQUEST_CODE = 101;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        edtFullName = findViewById(R.id.edtFullName);
        edtMobile = findViewById(R.id.edtMobile);
        edtEmail = findViewById(R.id.edtEmail);
        edtDateofBirth = findViewById(R.id.edtDateofBirth);
        spinner = findViewById(R.id.spinner);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        image = findViewById(R.id.image);

        rdMale = findViewById(R.id.radioMale);
        edFemale = findViewById(R.id.radioFemale);
        delete = findViewById(R.id.button_delete);
        save = findViewById(R.id.button_save);
        // find the radiobutton by returned id

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE);
            }
        });

        //Creating the ArrayAdapter instance having the country list
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Devlopers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(arrayAdapter);

        edtDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edtDateofBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        final Task task = (Task) getIntent().getSerializableExtra("task");
        if (task != null) {
            delete.setVisibility(View.VISIBLE);
            save.setText("Update");
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
                    builder.setTitle("Are you sure?");
                    builder.setMessage("You want to Delete this Employee?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteTask(task);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    AlertDialog ad = builder.create();
                    ad.show();
                }
            });
            loadTask(task);
            findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateTask(task);
                }
            });
        } else {
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTask();
                }
            });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap) data.getExtras().get("data");
//        bitmap=BitmapManager.drawString(bitmap,"Hi,\nI am Asif Mohammad Mollah,\nHow are you ?",0,0,10);
        image.setImageBitmap(bitmap);
//        int lastUID = 0;
//        try {
//            lastUID = db.myImageDao().last().getUid();
//        } catch (Exception e) {
//        }
//        String title = "Image " + String.valueOf(lastUID + 1);


    }

    private void deleteTask(Task task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(AddTaskActivity.this).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(AddTaskActivity.this, "Deleted", Toast.LENGTH_LONG).show();
                finish();
//                startActivity(new Intent(AddTaskActivity.this, MainActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

    private void saveTask() {
        int selectedId = radioGroupSex.getCheckedRadioButtonId();

        radioButton = (RadioButton) findViewById(selectedId);


        final String f_name = edtFullName.getText().toString().trim();
        final String mobile = edtMobile.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String dob = edtDateofBirth.getText().toString().trim();
        final String spin_value = spinner.getSelectedItem().toString();

        final String gender = radioButton.getText().toString();
        if (image == null) {
//            edtFullName.setError("Image required")
            Toast.makeText(this, "Image required", Toast.LENGTH_SHORT).show();
            image.requestFocus();
            return;
        } else if (f_name.isEmpty()) {
            edtFullName.setError("Full Name required");
            edtFullName.requestFocus();
            return;
        } else if (mobile.isEmpty()) {
            edtMobile.setError("Mobile required");
            edtMobile.requestFocus();
            return;
        } else if (email.isEmpty()) {
            edtEmail.setError("Email required");
            edtEmail.requestFocus();
            return;
        } else if (gender.matches("")) {
//            radioButton.getText().toString(
            Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show();
//            gender.requestFocus();
            return;
        } else if (dob.isEmpty()) {
            edtDateofBirth.setError("Date of Birth required");
            edtDateofBirth.requestFocus();
            return;
        } else if (spin_value.isEmpty()) {
//            spinner.setError("Date of Birth required");
//            edtDateofBirth.requestFocus();
            return;
        }
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Task task = new Task();
                task.setFullname(f_name);
                task.setMobile(mobile);
                task.setEmail(email);
                task.setDob(dob);
                task.setGender(gender);
                task.setSpin_value(spin_value);
                task.setPosition(spinner.getSelectedItemPosition());


                if (bitmap != null) {
                    byte[] image = BitmapManager.bitmapToByte(bitmap);
                    task.setImage(image);
                }
//                task.setFinished(false);

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
//                startActivity(new Intent(AddTaskActivity.this, MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    private void updateTask(final Task task) {
        int selectedId = radioGroupSex.getCheckedRadioButtonId();

        radioButton = (RadioButton) findViewById(selectedId);


        final String f_name = edtFullName.getText().toString().trim();
        final String mobile = edtMobile.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String dob = edtDateofBirth.getText().toString().trim();
        final String spin_value = spinner.getSelectedItem().toString();
        final String gender = radioButton.getText().toString();
        if (image == null) {
//            edtFullName.setError("Image required")
            Toast.makeText(this, "Image required", Toast.LENGTH_SHORT).show();
            image.requestFocus();
            return;
        } else if (f_name.isEmpty()) {
            edtFullName.setError("Full Name required");
            edtFullName.requestFocus();
            return;
        } else if (mobile.isEmpty()) {
            edtMobile.setError("Mobile required");
            edtMobile.requestFocus();
            return;
        } else if (email.isEmpty()) {
            edtEmail.setError("Email required");
            edtEmail.requestFocus();
            return;
        } else if (gender.matches("")) {

//            radioGroupSex.toString().setError("Email required");
//            gender.requestFocus();
            return;
        } else if (dob.isEmpty()) {
            edtDateofBirth.setError("Date of Birth required");
            edtDateofBirth.requestFocus();
            return;
        } else if (spin_value.isEmpty()) {
//            spinner.setError("Date of Birth required");
//            edtDateofBirth.requestFocus();
            return;
        }
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
//                Task task = new Task();
                task.setFullname(f_name);
                task.setMobile(mobile);
                task.setEmail(email);
                task.setDob(dob);
                task.setGender(gender);
                task.setSpin_value(spin_value);
                task.setPosition(spinner.getSelectedItemPosition());
                if (bitmap != null) {
                    byte[] image = BitmapManager.bitmapToByte(bitmap);
                    task.setImage(image);
                }

//                task.setFinished(false);

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
//                startActivity(new Intent(AddTaskActivity.this, MainActivity.class));
            }
        }

        UpdateTask st = new UpdateTask();
        st.execute();
    }

    private void loadTask(Task task) {
        edtFullName.setText(task.getFullname());
        edtMobile.setText(task.getMobile());
        edtEmail.setText(task.getEmail());
        edtDateofBirth.setText(task.getDob());
//        spinner.setSelected(task.getDob());
//        radioButton.setText(task.getDob());
        if (task.getImage()!=null){
            Bitmap images = BitmapManager.byteToBitmap(task.getImage());
            image.setImageBitmap(images);
        }

        spinner.setSelection(task.getPosition());
//        spinner.setSelection(((ArrayAdapter<String>) spinner.getAdapter()).getPosition(task.getPosition()));

        if (task.getGender().matches("Male")) {
            rdMale.setChecked(true);
        } else {
            edFemale.setChecked(true);
        }

    }
}