package com.nextstep.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import in.nextstep.txn.TxnLogFinder;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.*;

import java.io.ByteArrayOutputStream;

@RestController
public class Greet {

	private final String FILE_PATH = "src/main/resources/files/";
	
	private  TxnLogFinder txn ;
	
	
	

	@GetMapping("/txn/log/{txnId}")
	public ResponseEntity<String> greet(@PathVariable String txnId) {
		txn.getCCTResponse(txnId);
		return new ResponseEntity<>("Found Txn id : "+txnId, HttpStatus.OK);
	}

	@GetMapping(value = "/product/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public Product getProductJson() {
		return new Product(1, "Laptop", 50000);
	}

	@GetMapping(value = "/product/xml", produces = MediaType.APPLICATION_XML_VALUE)
	public Product getProductXml() {
		return new Product(1, "Laptop", 50000);
	}

	@GetMapping(value = "/product/html", produces = MediaType.TEXT_HTML_VALUE)
	public String getProductHtml() {
		return "<html><body>" + "<h1>Product Details</h1>" + "<p>ID: 1</p>" + "<p>Name: Laptop</p>"
				+ "<p>Price: 50000</p>" + "</body></html>";
	}

	@GetMapping(value = "/product/text", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getProductText() {
		return "ID = 1, Name = Laptop, Price = 50000";
	}

	@GetMapping(value = "/product/csv", produces = "text/csv")
	public String getProductCsv() {
		return "id,name,price\n1,Laptop,50000";
	}

	@GetMapping(value = "/product/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> getProductPdf() throws Exception {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, out);

		document.open();
		document.add(new Paragraph("Product Details"));
		document.add(new Paragraph("ID: 1"));
		document.add(new Paragraph("Name: Laptop"));
		document.add(new Paragraph("Price: 50000"));
		document.close();

		byte[] pdfBytes = out.toByteArray();

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=product.pdf")
				.contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
	}

	@GetMapping("/{fileName}")
	public ResponseEntity<?> downloadFile(@PathVariable String fileName) throws IOException {

		File file = new File(FILE_PATH + fileName);

		if (!file.exists()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + fileName);
		}

		// Read file as bytes
		byte[] fileBytes = Files.readAllBytes(file.toPath());

		// Detect content type automatically
		String contentType = Files.probeContentType(file.toPath());
		if (contentType == null) {
			contentType = "application/octet-stream"; // default
		}

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
				.contentType(MediaType.parseMediaType(contentType)).body(fileBytes);
	}

	@GetMapping(value = "/product/pdf1", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generateProductPdf() {

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			Document document = new Document(PageSize.A4);
			PdfWriter.getInstance(document, out);

			document.open();

			// -------- Title --------
			Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
			Paragraph title = new Paragraph("Product Invoice", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			title.setSpacingAfter(20);
			document.add(title);

			// -------- Date --------
			Font dateFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
			Paragraph date = new Paragraph("Date: 25-Nov-2025", dateFont);
			date.setAlignment(Element.ALIGN_RIGHT);
			date.setSpacingAfter(10);
			document.add(date);

			// -------- Table --------
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setSpacingBefore(20f);
			table.setSpacingAfter(20f);

			// Header Cells
			PdfPCell h1 = new PdfPCell(new Phrase("Field"));
			PdfPCell h2 = new PdfPCell(new Phrase("Value"));

			h1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			h2.setBackgroundColor(BaseColor.LIGHT_GRAY);

			h1.setPadding(8);
			h2.setPadding(8);

			table.addCell(h1);
			table.addCell(h2);

			// Data Rows
			addRow(table, "Product ID", "1");
			addRow(table, "Product Name", "Laptop");
			addRow(table, "Brand", "Dell");
			addRow(table, "Category", "Electronics");
			addRow(table, "Price", "₹50,000");
			addRow(table, "Tax (18% GST)", "₹9,000");
			addRow(table, "Total Amount", "₹59,000");

			document.add(table);

			// -------- Footer --------
			Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
			Paragraph footer = new Paragraph("Thank you for your purchase!", footerFont);
			footer.setAlignment(Element.ALIGN_CENTER);

			document.add(footer);

			document.close();

			byte[] pdfBytes = out.toByteArray();

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=product-invoice.pdf")
					.contentType(MediaType.APPLICATION_PDF).body(pdfBytes);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	// Helper method
	private void addRow(PdfPTable table, String key, String value) {
		PdfPCell cell1 = new PdfPCell(new Phrase(key));
		PdfPCell cell2 = new PdfPCell(new Phrase(value));

		cell1.setPadding(8);
		cell2.setPadding(8);

		table.addCell(cell1);
		table.addCell(cell2);
	}

}
