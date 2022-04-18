package com.gtappdevelopers.netflixapp.photoEditorApp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.EmojiCompatConfigurationView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gtappdevelopers.netflixapp.R;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder;
import ja.burhanrashid52.photoeditor.shape.ShapeType;
import top.defaults.colorpicker.ColorPickerPopup;

public class PhotoEditorActivity extends AppCompatActivity {

    private Button addBtn;
    private LinearLayout brushLL, eraserLL, undoLL, redoLL, shapesLL, textLL,filterLL;
    private CardView shapeCV, textCV;
    private TextView saveTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_editor);
        PhotoEditorView mPhotoEditorView = findViewById(R.id.photoEditorView);
        mPhotoEditorView.getSource().setImageResource(R.drawable.img);
        Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto);
        addBtn = findViewById(R.id.idBtn);
        saveTV = findViewById(R.id.idTVSave);
        brushLL = findViewById(R.id.idLLBrush);
        eraserLL = findViewById(R.id.idLLEraser);
        filterLL = findViewById(R.id.idLLFilter);
        undoLL = findViewById(R.id.idLLUndo);
        textLL = findViewById(R.id.idLLText);
        redoLL = findViewById(R.id.idLLRedo);
        shapesLL = findViewById(R.id.idLLShape);
        shapeCV = findViewById(R.id.idCVShape);
        textCV = findViewById(R.id.idCVText);
        PhotoEditor mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .setClipSourceImage(true)
                .setDefaultTextTypeface(mTextRobotoTf)
                .build();

        eraserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                erase(mPhotoEditor);
            }
        });
        brushLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawLine(mPhotoEditor);
            }
        });
        undoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditor.undo();
            }
        });
        redoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditor.redo();
            }
        });
        shapesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addShapes(mPhotoEditor);
            }
        });
        textLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addText(mPhotoEditor);
            }
        });
        filterLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFilter(mPhotoEditor);
            }
        });

        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(PhotoEditorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                   //request for permission
                    return;
                }

                mPhotoEditor.saveAsBitmap(new OnSaveBitmap() {
                    @Override
                    public void onBitmapReady(Bitmap saveBitmap) {
                        MediaStore.Images.Media.insertImage(getContentResolver(), saveBitmap, "EditedImage", "EditedImageDescription");
                        Toast.makeText(PhotoEditorActivity.this, "Image saved..", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(PhotoEditorActivity.this, "Fail to save image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void addText(PhotoEditor photoEditor) {
        final BottomSheetDialog shapeDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View layout = LayoutInflater.from(this).inflate(R.layout.text_bottom_sheet, textCV);
        shapeDialog.setContentView(layout);
        shapeDialog.setCancelable(false);
        shapeDialog.setCanceledOnTouchOutside(true);
        shapeDialog.show();
        EditText edt = layout.findViewById(R.id.idEdttxt);
        Button btn = layout.findViewById(R.id.idBtnChooseColor);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edt.getText().toString();
                shapeDialog.dismiss();
                new ColorPickerPopup.Builder(PhotoEditorActivity.this).initialColor(Color.RED)
                        .enableBrightness(true)
                        .enableAlpha(true)
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                photoEditor.addText(msg, color);
                            }
                        });
            }
        });
    }

    private void addFilter(PhotoEditor editor) {
        final BottomSheetDialog shapeDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View layout = LayoutInflater.from(this).inflate(R.layout.filter_bottom_sheet, textCV);
        shapeDialog.setContentView(layout);
        shapeDialog.setCancelable(false);
        shapeDialog.setCanceledOnTouchOutside(true);
        shapeDialog.show();

       // PhotoEditorView filterOne = layout.findViewById(R.id.idPEVFilterOne);
        // PhotoEditorView filterTwo = layout.findViewById(R.id.idPEVFilterTwo);
        //PhotoEditorView filterThree = layout.findViewById(R.id.idPEVFilterThree);

      //  filterOne.getSource().setImageResource(R.drawable.img);
        // filterTwo.getSource().setImageResource(R.drawable.img);
        //filterThree.getSource().setImageResource(R.drawable.img);

//        PhotoEditor pe1 = new PhotoEditor.Builder(this, filterOne)
//                .setPinchTextScalable(true)
//                .setClipSourceImage(true)
//                .build();

//        PhotoEditor pe2 = new PhotoEditor.Builder(this, filterTwo)
//                .setPinchTextScalable(true)
//                .setClipSourceImage(true)
//                .build();

//        PhotoEditor pe3 = new PhotoEditor.Builder(this, filterThree)
//                .setPinchTextScalable(true)
//                .setClipSourceImage(true)
//                .build();

     //   pe1.setFilterEffect(PhotoFilter.FISH_EYE);
//        pe2.setFilterEffect(PhotoFilter.CONTRAST);
//        pe3.setFilterEffect(PhotoFilter.FISH_EYE);

//        filterOne.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editor.setFilterEffect(PhotoFilter.FISH_EYE);
//            }
//        });

//        filterTwo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editor.setFilterEffect(PhotoFilter.CONTRAST);
//            }
//        });

//        filterThree.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editor.setFilterEffect(PhotoFilter.FISH_EYE);
//            }
//        });


    }

    private void erase(PhotoEditor photoEditor) {
        photoEditor.setBrushDrawingMode(true);
        photoEditor.brushEraser();
        photoEditor.setBrushEraserSize(50);
    }

    private void addShapes(PhotoEditor photoEditor) {
        final BottomSheetDialog shapeDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View layout = LayoutInflater.from(this).inflate(R.layout.shape_bottom_sheet, shapeCV);
        shapeDialog.setContentView(layout);
        shapeDialog.setCancelable(false);
        shapeDialog.setCanceledOnTouchOutside(true);
        shapeDialog.show();

        LinearLayout ovalLL = layout.findViewById(R.id.idLLOval);
        LinearLayout rectangleLL = layout.findViewById(R.id.idLLRectangle);
        LinearLayout lineLL = layout.findViewById(R.id.idLLLine);

        ovalLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoEditor.setBrushDrawingMode(true);
                ShapeBuilder mShapeBuilder = new ShapeBuilder()
                        .withShapeOpacity(100)
                        .withShapeColor(getResources().getColor(R.color.black))
                        .withShapeType(ShapeType.OVAL)
                        .withShapeSize(50);

                photoEditor.setShape(mShapeBuilder);
                shapeDialog.dismiss();
            }
        });

        rectangleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoEditor.setBrushDrawingMode(true);
                ShapeBuilder mShapeBuilder = new ShapeBuilder()
                        .withShapeOpacity(100)
                        .withShapeColor(getResources().getColor(R.color.black))
                        .withShapeType(ShapeType.RECTANGLE)
                        .withShapeSize(50);

                photoEditor.setShape(mShapeBuilder);
                shapeDialog.dismiss();
            }
        });

        lineLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoEditor.setBrushDrawingMode(true);
                ShapeBuilder mShapeBuilder = new ShapeBuilder()
                        .withShapeOpacity(100)
                        .withShapeColor(getResources().getColor(R.color.black))
                        .withShapeType(ShapeType.LINE)
                        .withShapeSize(50);

                photoEditor.setShape(mShapeBuilder);
                shapeDialog.dismiss();
            }
        });
    }

    private void drawLine(PhotoEditor mPhotoEditor) {
        new ColorPickerPopup.Builder(PhotoEditorActivity.this).initialColor(Color.RED)
                .enableBrightness(true)
                .enableAlpha(true)
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        mPhotoEditor.setBrushDrawingMode(true);
                        ShapeBuilder mShapeBuilder = new ShapeBuilder()
                                .withShapeOpacity(100)
                                .withShapeColor(color)
                                .withShapeType(ShapeType.BRUSH)
                                .withShapeSize(30);
                        mPhotoEditor.setShape(mShapeBuilder);
                    }
                });
    }
}