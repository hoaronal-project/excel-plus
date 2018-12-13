package io.github.biezhi.excel.plus.examples;

import io.github.biezhi.excel.plus.BaseTest;
import io.github.biezhi.excel.plus.Writer;
import io.github.biezhi.excel.plus.enums.ExcelType;
import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.model.PerformanceTestModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author biezhi
 * @date 2018-12-11
 */
@Slf4j
public class WriterExample extends BaseTest {

    @Test
    public void testWrite100wRows() throws WriterException {

        List<PerformanceTestModel> rows = readyData();

        long start = System.currentTimeMillis();
        writeTestExcel(rows);
        long end = System.currentTimeMillis();

        log.info("Write " + testCount + " rows, time consuming: " + (end - start) + "ms");

        // If you want to open the file view, please comment this line
        deleteTempFile(testFileName);
    }

    @Test
    public void testWriteSample() throws WriterException {
        String fileName = "sample_test.xlsx";

        Writer.create()
                .headerTitle("一份简单的Excel表格")
                .withRows(buildData())
                .to(new File("sample_test.xlsx"));

        deleteTempFile(fileName);
    }

    @Test
    public void testWriteCustomStyle() throws WriterException {
        String fileName = "sample_custom_style_test.xlsx";

        Writer.create()
                .headerTitle("一份自定义样式的Excel表格")
                .withRows(buildData())
                .titleStyle((wb, style) -> {
                    Font font = wb.createFont();
                    font.setFontHeightInPoints((short) 40);
                    font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                    style.setFont(font);
                })
                .headerStyle((wb, style) -> {
                    Font font = wb.createFont();
                    font.setFontHeightInPoints((short) 20);
                    font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
                    style.setFont(font);
                })
                .cellStyle((wb, style) -> {
                    Font font = wb.createFont();
                    font.setFontHeightInPoints((short) 20);
                    font.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
                    style.setFont(font);
                })
                .to(new File(fileName));

        deleteTempFile(fileName);
    }

    @Test
    public void testWriteCustom() throws WriterException {
        String fileName = "sample_custom_with_raw.xlsx";

        Writer.create()
                .withRaw()
                .createRow(sheet -> {
                    sheet.setDefaultColumnWidth(30);

                    for (int r = 0; r < 3; r++) {
                        Row row = sheet.createRow(r);
                        for (int c = 0; c < 5; c++) {
                            Cell cell = row.createCell(c);
                            cell.setCellValue("row:" + r + ", cell:" + c);
                        }
                    }
                })
                .to(new File(fileName));

        deleteTempFile(fileName);
    }

    @Test
    public void testWriteByTemplate() throws WriterException {
        String fileName = "sample_custom_with_template.xlsx";

        Writer.create()
                .withTemplate(classPath() + "/template.xls")
                .withRows(buildData())
                .to(new File(fileName));

        deleteTempFile(fileName);
    }

    @Test
    public void testWriteToOutputStream() throws FileNotFoundException, WriterException {
        String fileName = "sample_test_to_stream.xlsx";

        Writer.create()
                .headerTitle("一份简单的Excel表格")
                .withRows(buildData())
                .to(new FileOutputStream(new File(fileName)));

        deleteTempFile(fileName);
    }

    @Test
    public void testWriteByCSV() throws WriterException {
        String fileName = "write_as_csv.csv";

        Writer.create(ExcelType.CSV)
                .withRows(buildData())
                .to(new File(fileName));

        deleteTempFile(fileName);
    }

}
