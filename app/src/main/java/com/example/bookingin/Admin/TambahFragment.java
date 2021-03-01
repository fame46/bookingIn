package com.example.bookingin.Admin;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookingin.R;
import com.example.bookingin.Utils.Preferences;
import com.example.bookingin.Utils.URLs;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class TambahFragment extends Fragment {

    Spinner spinner;
    ImageView imageView;
    TextView ambilGambar;
    EditText etNama, etHarga;
    Button save;
    ProgressBar loading;

    private int GALLERY = 1;
    private static final int RESULT_CANCELED = 0;
    private static final String IMAGE_DIRECTORY = "/javabarricade";
    Bitmap bitmap;
    String indexS;

    JSONObject jsonObject;
    RequestQueue rQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tambah, container, false);

        requestMultiplePermissions();

        spinner = v.findViewById(R.id.spinner);
        ambilGambar = v.findViewById(R.id.btnImport);
        imageView = v.findViewById(R.id.imageView);
        etNama = v.findViewById(R.id.etNamaMenu);
        etHarga = v.findViewById(R.id.etHargaMenu);
        save = v.findViewById(R.id.btnSaveMenu);
        loading = v.findViewById(R.id.loading);

        final String[] choice = new String[]{
                "Mobil",
                "Kamera",
                "Alat Camping"
        };
        final ArrayAdapter<String> spinnerArray2 = new ArrayAdapter<String>(
                getContext(),R.layout.list_textview,choice
        );
        spinnerArray2.setDropDownViewResource(R.layout.list_textview);
        spinner.setAdapter(spinnerArray2);

        ambilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNama.length() < 1){
                    etNama.setError("Nama Tidak Boleh Kosong");
                }else if(etHarga.length() < 1){
                    etHarga.setError("Harga Tidak Boleh Kosong");
                }else {
//                    Toast.makeText(getContext(), "nama = "+etNama.length()+" harga = "+etHarga.length(), Toast.LENGTH_SHORT).show();
                    int index = spinner.getSelectedItemPosition();
                    if(index == 0){
                        indexS = "Mobil";
                        uploadMenu(bitmap, indexS);
                    }else if(index == 1){
                        indexS = "Kamera";
                        uploadMenu(bitmap, indexS);
                    }else if(index == 2){
                        indexS = "Alat Camping";
                        uploadMenu(bitmap, indexS);
                    }
                }
            }
        });


        return v;
    }

    private void showFileChooser() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getActivity().getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void uploadMenu(Bitmap bitmap, String kategori){

        loading.setVisibility(View.VISIBLE);
        save.setVisibility(View.GONE);

        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//        Log.d("TAG", "uploadMenu: "+encodedImage);
        try {
            jsonObject = new JSONObject();
            String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            jsonObject.put("nama_menu", etNama.getText().toString());
            jsonObject.put("harga",etHarga.getText().toString());
            jsonObject.put("kategori",kategori);
            //  Log.e("Image name", etxtUpload.getText().toString().trim());
            jsonObject.put("image", encodedImage);
            // jsonObject.put("aa", "aa");
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLs.CREATEMENU, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("aaaaaaa", jsonObject.toString());
                        String nama = etNama.getText().toString().trim();
                        rQueue.getCache().clear();
                        Toast.makeText(getContext(), nama+" Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        save.setVisibility(View.VISIBLE);
                        startActivity(new Intent(getContext(),HomeAdmin.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("aaaaaaa", volleyError.toString());
                loading.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);

            }
        });

        rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(jsonObjectRequest);
    }
}