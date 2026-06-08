package com.logicsoftbd.lsl.posPrinter.textparser;

import com.logicsoftbd.lsl.posPrinter.EscPosPrinterCommands;
import com.logicsoftbd.lsl.posPrinter.exceptions.EscPosConnectionException;
import com.logicsoftbd.lsl.posPrinter.exceptions.EscPosEncodingException;

public interface IPrinterTextParserElement {
    int length() throws EscPosEncodingException;
    IPrinterTextParserElement print(EscPosPrinterCommands printerSocket) throws EscPosEncodingException, EscPosConnectionException;
}
