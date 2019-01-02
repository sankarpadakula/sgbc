package com.sp.sgbc.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.html2pdf.HtmlConverter;
import com.sp.sgbc.model.Transaction;

public class Helper {
  private static final Logger LOGGER = LoggerFactory.getLogger(Helper.class);

  static final String[] CSV_HEADERS = new String[] { "Transaction Num", "Transaction date", "Transaction amount", "Ref Num" };

  static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

  public static String readFile(String filename) {
    StringBuffer records = new StringBuffer();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line;
      while ((line = reader.readLine()) != null) {
        records.append(line);
      }
      reader.close();
      return records.toString();
    } catch (Exception e) {
      System.err.format("Exception occurred trying to read '%s'.", filename);
      e.printStackTrace();
      return null;
    }
  }

  public static String getFileExtension(String fileName) {
    String extension = "";
    try {
      if (fileName != null) {
        extension = fileName.substring(fileName.lastIndexOf("."));
      }
    } catch (Exception e) {
      extension = "";
    }
    return extension;

  }
  public static byte[] createPdf(String content) throws IOException {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    HtmlConverter.convertToPdf(content, stream);
    return stream.toByteArray();
  }

  public static List<Transaction> readCSV(String filename) throws IOException {
    List<Transaction> transactions = new ArrayList<Transaction>();
    try (Reader reader = Files.newBufferedReader(Paths.get(filename));
        CSVParser csvParser = new CSVParser(reader,
            CSVFormat.DEFAULT.withHeader(CSV_HEADERS).withIgnoreHeaderCase().withTrim());) {
      for (CSVRecord csvRecord : csvParser) {
        if (csvRecord.getRecordNumber() == 1)
          continue;
        Transaction transaction = new Transaction();
        transactions.add(transaction);
        Map<String, String> record = csvRecord.toMap();
        String num = record.get(CSV_HEADERS[0]);
        String date = record.get(CSV_HEADERS[1]);
        String amount = record.get(CSV_HEADERS[2]);
        String applicantId = record.get(CSV_HEADERS[3]);
        transaction.setApplicantId(Long.parseLong(applicantId.replaceAll(",|-", "")));
        transaction.setAmountPaid(Double.parseDouble(amount.replaceAll(",|-", "")));
        transaction.setTransactionId(num);
        try {
          transaction.setTransactionDate(DATE_FORMAT.parse(date));
        } catch (ParseException e) {
          transaction.setTransactionDate(new Date());
        }
      }
    } catch (IOException e) {
      LOGGER.error("Reading CSV failed" + filename, e);
      throw e;
    }
    return transactions;
  }

  public static List<Transaction> readExcel(String filePath) throws IOException {
    List<Transaction> transactions = new ArrayList<Transaction>();
    int maxDataCount = 0;
    FileInputStream inputStream = null;
    ;
    Workbook workbook = null;
    try {
      inputStream = new FileInputStream(new File(filePath));
      workbook = new XSSFWorkbook(inputStream);

      Sheet firstSheet = workbook.getSheetAt(0);
      Iterator<Row> iterator = firstSheet.iterator();

      while (iterator.hasNext()) {
        Row nextRow = iterator.next();
        // Skip the first row beacause it will be header
        if (nextRow.getRowNum() == 0) {
          maxDataCount = nextRow.getLastCellNum();
          continue;
        }
        if (isRowEmpty(nextRow, maxDataCount)) {
          continue;
        }
        Transaction transaction = new Transaction();
        transactions.add(transaction);
        Cell num = nextRow.getCell(0);
        Cell date = nextRow.getCell(1);
        Cell amount = nextRow.getCell(2);
        Cell applicantId = nextRow.getCell(3);
        transaction.setTransactionId(num.getStringCellValue());
        transaction.setTransactionDate(date.getDateCellValue());
        transaction.setAmountPaid(amount.getNumericCellValue());
        transaction.setApplicantId(Long.parseLong(applicantId.getStringCellValue().replaceAll(",|-", "")));
      }
    } catch (IOException e) {
      LOGGER.error("Excel parsing failed " + filePath, e);
      throw e;
    } finally {
      try {
        workbook.close();
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return transactions;
  }

  public static void archive(File file) {
    File archive = new File(file.getParent() + File.pathSeparator + "Archive");
    archive.mkdirs();
    file.renameTo(new File(archive.getAbsolutePath() + File.pathSeparator + file.getName()));
  }

  public static boolean isRowEmpty(Row row, int lastcellno) {
    for (int c = row.getFirstCellNum(); c < lastcellno; c++) {
      Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
      if (cell != null && cell.getCellTypeEnum() != CellType.BLANK)
        return false;
    }
    return true;
  }

  public static Date getNextMonthStartDate() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.DATE, 1);
    return cal.getTime();
  }

  public static File convertToFile(String fileName, byte[] data) {
    File convFile = new File(fileName);
    try {
      FileOutputStream fos = new FileOutputStream(convFile);
      fos.write(data);
      fos.close();
    } catch (IOException e) {
      LOGGER.error("File conversion failed" + fileName, e);
    }
    return convFile;
  }

  public static int monthsDiffFromCurrent(Date startDate) {
    Calendar startCalendar = new GregorianCalendar();
    startCalendar.setTime(startDate);
    Calendar endCalendar = new GregorianCalendar();
    endCalendar.setTime(new Date());

    int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
    int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    return diffMonth;
  }

  public static void main(String[] args) throws FileNotFoundException, IOException {
    String amount = "1,212.5";
    System.out.println(Double.parseDouble(amount.replaceAll(",|-", "")));
    /*
     * String content = readFile(
     * "C:\\Turner\\codebase\\mysamples\\spring-web\\gbc-web\\src\\main\\resources\\templates\\email\\pdfTemplate.html")
     * ; ByteArrayOutputStream stream = new ByteArrayOutputStream(); HtmlConverter.convertToPdf(content, stream);
     * 
     * try (OutputStream outputStream = new FileOutputStream(
     * "C:\\Turner\\codebase\\mysamples\\spring-web\\gbc-web\\src\\main\\resources\\templates\\email\\pdfTemplate.pdf"))
     * { stream.writeTo(outputStream); }
     */

  }

}
