package com.logicsoftbd.lsl.ui.process.greyroll;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.utils.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FinishFabricQrCodePrintActivity extends AppCompatActivity {
    ArrayList<FinishFabricQrCodeResponses.ResultSet>sewingRequest;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_fabric_qr_code_print);
        requestPermission();
        Log.e("CommonUtils","CommonUtils"+new Gson().toJson(CommonUtils.sewingRequest));
        createPdfFile(Common.getAppPath(FinishFabricQrCodePrintActivity.this)+"test_pdf.pdf");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //CommonUtils.sewingRequest.clear();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdfFile(String path) {
        if (new File(path).exists())
            new File(path).delete();
        Rectangle pagesize = new Rectangle(512, 861);
        Document document=new Document(pagesize,0,0,50,0);
        try {
            PdfWriter.getInstance(document,new FileOutputStream(path));
            document.open();
            //  document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Evan");
            document.addCreator("Khan");
           ;
            //  Chunk glue = new Chunk(new VerticalPositionMark());


            for(FinishFabricQrCodeResponses.ResultSet detailsPart:CommonUtils.sewingRequest){
                addNewItem(document,detailsPart, Element.ALIGN_LEFT);
                Paragraph paragraph1 = new Paragraph(" ");
                Paragraph paragraph2 = new Paragraph(" ");
                Paragraph paragraph3 = new Paragraph(" ");
                Paragraph paragraph4 = new Paragraph(" ");
                Paragraph paragraph5 = new Paragraph(" ");
                document.add(paragraph1);
                document.add(paragraph2);
                document.newPage();
//                document.add(paragraph3);
//                document.add(paragraph4);
//                document.add(paragraph5);
            }

//            Paragraph p = new Paragraph();
//            p.add("Text to the left");
//            p.add(glue);
//            p.add("Text to the right");
//
//            document.add(p);
            //document.setMargins(-100,0,200,0);
            document.close();

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

            printPDF();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPDF() {
        PrintManager printManager=(PrintManager)getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printDocumentAdapter=new PDFDocumentAdapter(FinishFabricQrCodePrintActivity.this,Common.getAppPath(FinishFabricQrCodePrintActivity.this)+"test_pdf.pdf");
        printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());

    }

    private void addLineSeparator(Document document) {

        LineSeparator lineSeparator=new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        try {
            document.add(new Chunk(lineSeparator));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        addLineSpace(document);
    }

    private void addLineSpace(Document document) {
        try {
            document.add(new Paragraph(""));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void addNewItem(Document document, FinishFabricQrCodeResponses.ResultSet finish, int alignCenter) throws com.google.zxing.WriterException {

        PdfPTable table1 = new PdfPTable(2);
        PdfPTable table = new PdfPTable(1);

        table1.setWidthPercentage(50);
        table1.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        table1.getDefaultCell().setBorderColor(BaseColor.WHITE);
        table.getDefaultCell().setBorderColor(BaseColor.WHITE);
        Image myImg=null;
        Bitmap bitmap=null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        table1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(finish.getBARCODE_NO(), BarcodeFormat.QR_CODE, 1080, 912);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        try {
            myImg = Image.getInstance(stream.toByteArray());
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM hh:mm aa");
        Date date1 = new Date(System.currentTimeMillis());
        String currentDate = formatter.format(date1);
        PdfPCell reportWorking = new PdfPCell(getParaReportFormat(currentDate+"\n"+finish.getBARCODE_NO()+"\n"+finish.getBUYER_NAME()+"\n"+finish.getBATCH_NO()+"\n"+finish.getQNTY()+" Kg", 12));
        reportWorking.setBorderColor(BaseColor.WHITE);
        table1.addCell(myImg);
        table1.addCell(reportWorking);
        try {
            table.addCell(getParaReportFormat(finish.getFILE_NO()+" and "+finish.getINTERNAL_REF()+",R. Dia-"+finish.getWIDTH()+"R. GSM-"+finish.getGSM(), 12));
            table.addCell(getParaReportFormat(finish.getCOLOR_NAME(), 12));
            table.addCell(getParaReportFormat("Roll: "+finish.getROLL_NO()+",Lot "+finish.getYARN_LOT(), 12));
            table.addCell(getParaReportFormat(finish.getBODY_PART_NAME(), 12));
            table.addCell(getParaReportFormat(finish.getITEM_DESCRIPTION(), 12));

            document.add(table1);
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    private Paragraph getParaReportFormat(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD);
        Paragraph p = new Paragraph(content, font);
        return p;
    }
}
