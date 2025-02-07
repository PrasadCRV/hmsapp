package com.hmsapp.service;

import com.hmsapp.entity.Booking;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Cell;

import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;

@Service
public class PDFGenerator {

    public void generatePDF(String path, Booking booking){
        try  {
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Hello Sir/Madam, here is your booking details.").setBold().setFontSize(16));

            // Create a table with 2 columns
            float[] columnWidths = {200f, 100f}; // Column widths
            Table table = new Table(columnWidths);

            // Add table headers
            table.addHeaderCell(new Cell().add(new Paragraph("Name").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("No. of Days").setBold()));

            // Add table rows
                table.addCell(booking.getGuestName());
                table.addCell(booking.getMobile());

            // Add table to the document
            document.add(table);

            document.close();
            System.out.println("PDF created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
