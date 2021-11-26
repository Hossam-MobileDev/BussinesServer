package com.hashtagco.bussinesserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Model.ChatMessageModel;
import com.hashtagco.bussinesserver.Model.Token;
import com.hashtagco.bussinesserver.ViewHolder.ChatPictureHolder;
import com.hashtagco.bussinesserver.ViewHolder.ChatTextHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatDetailActivity extends AppCompatActivity implements IloadTimeFromFirebaseListner {

    private static final int MY_CAM_REQUST_CODE = 555;
    private static final int MY_RSULT_LAOD_TAG = 666;
    String email;
    /*@BindView(R.id.toolbar)
    MaterialToolbar materialToolbar;*/

    @BindView(R.id.image_preview)
    ImageView image_preview;

    @BindView(R.id.image_image)
    ImageView image_image;

    @BindView(R.id.image_camera)
    ImageView image_camera;

    @BindView(R.id.image_send)
    ImageView image_send;

    @BindView(R.id.txt_message)
    AppCompatEditText txt_message;


    RecyclerView recyclerViewcha;
    FirebaseAuth auth;
String roomId;
    // Token serverToken;
    LinearLayoutManager linearLayoutManager;

    Uri fileUri;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatRef, offSetRef;
    DatabaseReference admins;
    IloadTimeFromFirebaseListner listner;

    FirebaseRecyclerAdapter<ChatMessageModel, RecyclerView.ViewHolder> adapter;
    FirebaseRecyclerOptions<ChatMessageModel> options;
    StorageReference storageReference;

    @OnClick(R.id.image_send)
    void onSubmitChatClick() {

        offSetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                long offset = snapshot.getValue(Long.class);
                long estimatedServerTimeInMs = System.currentTimeMillis() + offset;
                listner.onLoadOnlyTimeSuccess(estimatedServerTimeInMs);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @OnClick(R.id.image_image)
    void onSelectImageClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, MY_RSULT_LAOD_TAG);
    }

    @OnClick(R.id.image_camera)
    void onCaptureImageClick() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        fileUri = getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, MY_CAM_REQUST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        ButterKnife.bind(this);
        recyclerViewcha = findViewById(R.id.recyclerView_chat);
        recyclerViewcha.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerViewcha.setLayoutManager(linearLayoutManager);
        listner = this;
        firebaseDatabase = FirebaseDatabase.getInstance();

        offSetRef = firebaseDatabase.getReference(".info/serverTimeOffset");
        roomId = getIntent().getStringExtra("Room_Id");
        loadChatContent();
    }
    private void loadChatContent() {
        DatabaseReference referenceTokens = FirebaseDatabase.getInstance().
                getReference("Tokens");
        Query queryData = referenceTokens.orderByChild("serverToken")
                .equalTo(true);// get All node isServerToken is True


        queryData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token serverToken = snapshot.getValue(Token.class);
                    Common.currentoken = serverToken;
                }

                chatRef = firebaseDatabase.getReference("ResturantRef")
                        .child(Common.currentoken.getToken()).child("Chat");
                Query query = chatRef.child(roomId);

                options = new FirebaseRecyclerOptions.
                        Builder<ChatMessageModel>().setQuery
                        (query, ChatMessageModel.class).build();
                adapter = new FirebaseRecyclerAdapter<ChatMessageModel,
                        RecyclerView.ViewHolder>(options) {

                    @Override
                    public int getItemViewType(int position) {
                        return adapter.getItem(position).isIspicture() ? 1 : 0;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                                    int position, @NonNull
                                                            ChatMessageModel model) {
                        if (holder instanceof ChatTextHolder) {

                            ChatTextHolder chatTextHolder = (ChatTextHolder) holder;
                            chatTextHolder.txt_email.setText(model.getName());
                            chatTextHolder.txt_chat_message.setText(model.getContent());
                            chatTextHolder.txt_time.setText(
                                    DateUtils.getRelativeTimeSpanString(model.getTimestamp(),
                                            Calendar.getInstance().getTimeInMillis(), 0).toString());
                        } else {
                            ChatPictureHolder chatPictureHolder = (ChatPictureHolder) holder;
                            chatPictureHolder.txt_email.setText(model.getName());
                            chatPictureHolder.txt_chat_message.setText(model.getContent());
                            chatPictureHolder.txt_time.setText(
                                    DateUtils.getRelativeTimeSpanString(model.getTimestamp(),
                                            Calendar.getInstance().getTimeInMillis(), 0).toString());
                            Glide.with(ChatDetailActivity.this)
                                    .load(model.getPicturlink())
                                    .into(chatPictureHolder.image_preview);
                        }
                    }

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view;
                        //Log.d("Chat", String.valueOf(viewType));
                        if (viewType == 0) {
                            view = LayoutInflater.from(viewGroup.getContext())
                                    .inflate(R.layout.layout_mssag_txt, viewGroup, false);
                            return new ChatTextHolder(view);
                        } else {
                            view = LayoutInflater.from(viewGroup.getContext())
                                    .inflate(R.layout.layout_mssag_picture, viewGroup, false);
                            return new ChatPictureHolder(view);
                        }
                    }
                };

                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        int friendlyMessageCount = adapter.getItemCount();
                        int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                        if (lastVisiblePosition == -1 ||
                                (positionStart >= (friendlyMessageCount - 1) &&
                                        lastVisiblePosition == (positionStart - 1))) {
                            recyclerViewcha.scrollToPosition(positionStart);
                        }

                    }
                });
                adapter.startListening();
                recyclerViewcha.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }


        });



    }



    @Override
    public void onLoadOnlyTimeSuccess(long estimateTimeInMs) {
        ChatMessageModel chatMessageModel = new ChatMessageModel();
        chatMessageModel.setName(Common.currentAdmin.getEmail());
        chatMessageModel.setContent(txt_message.getText().toString());
        chatMessageModel.setTimestamp(estimateTimeInMs);
        if (fileUri == null) {
            chatMessageModel.setIspicture(false);
            submitChatToFirebase(chatMessageModel, chatMessageModel.isIspicture());
        } else {
            uploadPicture(fileUri, chatMessageModel);
        }
    }



    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        File mediastoreDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES), "EatItV2");
        if (!mediastoreDir.exists()) {
            if (!mediastoreDir.mkdir())
                return null;
        }
        String time_stamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(new Date());
        File mediafile = new File(new StringBuilder
                (mediastoreDir.getPath()).append(File.separator).append("IMG_")
                .append(time_stamp).append("_")
                .append(new Random().nextInt()).append(".jpg").
                        toString());
        return mediafile;
    }


    private void uploadPicture(Uri fileUri, ChatMessageModel chatMessageModel) {

        if (fileUri != null) {
            AlertDialog dialog = new AlertDialog.Builder(ChatDetailActivity.this).
                    setCancelable(false).setMessage("Please wait ...").create();
            dialog.show();
            String fileName = Common.getFileName(getContentResolver(), fileUri);
            String path = new StringBuilder(Common.currentoken.getToken())
                    .append("/").append(fileName).toString();
            storageReference = FirebaseStorage.getInstance().getReference(path);
            UploadTask uploadTask = storageReference.putFile(fileUri);
            Task<Uri> task = uploadTask.continueWithTask(task1 -> {
                if (!task1.isSuccessful())
                    Toast.makeText(this, "Faild Uploading .."
                            , Toast.LENGTH_SHORT).show();
                return storageReference.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task2) {
                    if (task2.isSuccessful()) {
                        String url = task2.getResult().toString();
                        dialog.dismiss();
                        chatMessageModel.setPicturlink(url);
                        chatMessageModel.setIspicture(true);
                        submitChatToFirebase(chatMessageModel, chatMessageModel.isIspicture());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(ChatDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void submitChatToFirebase(ChatMessageModel chatMessageModel,
                                      boolean ispicture) {

        DatabaseReference referenceTokens = FirebaseDatabase.getInstance().
                getReference("Tokens");
        Query queryData = referenceTokens.orderByChild("serverToken")
                .equalTo(true);// get All node isServerToken is True


        queryData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token   serverToken = snapshot.getValue(Token.class);
                    Common.currentoken = serverToken;
                }
                chatRef = firebaseDatabase.getReference("ResturantRef")
                        .child(Common.currentoken.getToken()).child("Chat");
                chatRef.child(roomId)
                        .push().setValue(chatMessageModel).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(ChatDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            txt_message.setText("");
                            txt_message.requestFocus();
//                if (adapter != null) {
//                    adapter.notifyDataSetChanged();
//                    if (ispicture) {
//                        fileUri = null;
//                        //  img_
//                    }
//                }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });





    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                     Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_CAM_REQUST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                ExifInterface exifInterface = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap
                            (getContentResolver(), fileUri);
                    exifInterface = new ExifInterface(getContentResolver().openInputStream(fileUri));
                    int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION
                            , ExifInterface.ORIENTATION_UNDEFINED);
                    Bitmap rotatbitmap = null;
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatbitmap = rotatbitmap(bitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatbitmap = rotatbitmap(bitmap, 180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatbitmap = rotatbitmap(bitmap, 270);
                            break;
                        default:
                            rotatbitmap = bitmap;
                            break;
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == MY_RSULT_LAOD_TAG) {
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imagUri = data.getData();
                        InputStream inputStream = getContentResolver().openInputStream(imagUri);
                        Bitmap selctdImage = BitmapFactory.decodeStream(inputStream);
                        image_preview.setImageBitmap(selctdImage);
                        image_preview.setVisibility(View.VISIBLE);
                        fileUri = imagUri;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private Bitmap rotatbitmap(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.
                getWidth(), bitmap.getHeight(), matrix, true);
    }
}