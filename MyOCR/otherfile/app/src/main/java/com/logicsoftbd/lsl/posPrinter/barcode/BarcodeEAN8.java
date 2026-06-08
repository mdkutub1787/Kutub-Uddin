package com.logicsoftbd.lsl.posPrinter.barcode;


import com.logicsoftbd.lsl.posPrinter.EscPosPrinterCommands;
import com.logicsoftbd.lsl.posPrinter.EscPosPrinterSize;
import com.logicsoftbd.lsl.posPrinter.exceptions.EscPosBarcodeException;

public class BarcodeEAN8 extends BarcodeNumber {
    public BarcodeEAN8(EscPosPrinterSize printerSize, String code, float widthMM, float heightMM, int textPosition) throws EscPosBarcodeException {
        super(printerSize, EscPosPrinterCommands.BARCODE_TYPE_EAN8, code, widthMM, heightMM, textPosition);
    }

    @Override
    public int getCodeLength() {
        return 8;
    }
}
