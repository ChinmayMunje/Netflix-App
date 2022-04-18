package com.gtappdevelopers.netflixapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class MainActivity extends AppCompatActivity {

    private TextView extractedTV;
    private Button extractBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        extractedTV = findViewById(R.id.idTVText);
        extractBtn = findViewById(R.id.idBtnExtractText);
        extractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extractPDF();
            }
        });
    }

    private void extractPDF() {
        try {
            String extractedText = "";
            PdfReader reader = new PdfReader("res/raw/java.pdf");
            int n = reader.getNumberOfPages();
            for (int i = 0; i < n; i++) {
                extractedText = extractedText + PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n";
            }
            extractedTV.setText(extractedText);
            reader.close();
        } catch (Exception e) {
            extractedTV.setText("Error found is : \n" + e);
        }
    }
}