package com.cradletrial.cradlevhtapp.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cradletrial.cradlevhtapp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

public class PdfActivity extends AppCompatActivity {
    private PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_view);
        //Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.first_aid_and_cpr_2017_digital);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromAsset("first_aid_and_cpr_2017_digital.pdf").load();
    }
}
