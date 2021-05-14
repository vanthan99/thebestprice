package dtu.thebestprice.exports;

import dtu.thebestprice.payload.response.dashboard.OverView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Data
public class StatisticExcelExporter {
    public static ByteArrayInputStream customersToExcel(OverView overView) throws IOException {
        String[] COLUMNs = {"Lượt truy cập", "Lượt tìm kiếm", "Người dùng", "Chủ cửa hàng", "Tổng sản phẩm"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Thống kê thebestprice");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            // CellStyle for Age
            CellStyle ageCellStyle = workbook.createCellStyle();
            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

            int rowIdx = 1;

            Row row = sheet.createRow(1);

            row.createCell(0).setCellValue(overView.getVisitor());
            row.createCell(1).setCellValue(overView.getSearch());
            row.createCell(2).setCellValue(overView.getUser());
            row.createCell(4).setCellValue(overView.getProduct());

            Cell ageCell = row.createCell(3);
            ageCell.setCellValue(overView.getRetailer());
            ageCell.setCellStyle(ageCellStyle);


            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
