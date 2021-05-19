package dtu.thebestprice.exports;

import dtu.thebestprice.payload.response.dashboard.DashBoard;
import dtu.thebestprice.payload.response.dashboard.OverView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

@Data
public class StatisticExcelExporter {
    public static ByteArrayInputStream customersToExcel(OverView overView, DashBoard dashBoard) throws IOException {
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

            //start row nav
            Row navRow = sheet.createRow(1);
            Cell navCell = navRow.createCell(1);

            Font navFont = workbook.createFont();
            navFont.setBold(true);
            navFont.setColor(IndexedColors.RED.getIndex());
            navFont.setFontName("Times New Roman");
            navFont.setFontHeightInPoints((short) 22);

            CellStyle navCellStyle = workbook.createCellStyle();
            navCellStyle.setFont(navFont);
            navCellStyle.setAlignment(HorizontalAlignment.CENTER);


//            CellStyle borderCellStyle = workbook.createCellStyle();
//            borderCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
//            borderCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//            borderCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
//
//            borderCellStyle.setBorderTop(BorderStyle.THIN);
//            borderCellStyle.setBorderLeft(BorderStyle.THIN);
//            borderCellStyle.setBorderRight(BorderStyle.THIN);
//
//            for (int i = 1; i <= 14; i++) {
//                Cell tempCell = navRow.createCell(i);
//                tempCell.setCellStyle(borderCellStyle);
//            }

            navCell.setCellValue("THỐNG KÊ THE BEST PRICE NGÀY " + LocalDate.now().getDayOfMonth() + " THÁNG " + YearMonth.now().getMonthValue() + " Năm " + YearMonth.now().getYear());
            navCell.setCellStyle(navCellStyle);
            sheet.addMergedRegion(new CellRangeAddress(
                    1,
                    1,
                    1,
                    14
            ));
            // end row nav

            // start Block Overview

            CellStyle overViewHeaderStyle = workbook.createCellStyle();
            CellStyle overViewValueStyle = workbook.createCellStyle();

            Font overViewHeaderFont = workbook.createFont();
            Font overViewValueFont = workbook.createFont();

            overViewHeaderFont.setFontHeightInPoints((short) 14);
            overViewHeaderFont.setFontName("Times New Roman");
            overViewHeaderFont.setBold(true);

            overViewValueFont.setFontName("Times New Roman");
            overViewValueFont.setFontHeightInPoints((short) 14);


            overViewHeaderStyle.setFont(overViewHeaderFont);
            overViewValueStyle.setFont(overViewValueFont);

            // số người truy cập
            Row row3 = sheet.createRow(3);
            Cell cellHeader3 = row3.createCell(1);
            cellHeader3.setCellValue("Lượt truy cập");
            cellHeader3.setCellStyle(overViewHeaderStyle);
            Cell cellValue3 = row3.createCell(3);
            cellValue3.setCellValue(overView.getVisitor());
            cellValue3.setCellStyle(overViewValueStyle);

            // lượt tìm kiếm
            Row row4 = sheet.createRow(4);
            Cell cellHeader4 = row4.createCell(1);
            cellHeader4.setCellValue("Lượt tìm kiếm");
            cellHeader4.setCellStyle(overViewHeaderStyle);
            Cell cellValue4 = row4.createCell(3);
            cellValue4.setCellValue(overView.getSearch());
            cellValue4.setCellStyle(overViewValueStyle);

            // người dùng
            Row row5 = sheet.createRow(5);
            Cell cellHeader5 = row5.createCell(1);
            cellHeader5.setCellValue("Người dùng");
            cellHeader5.setCellStyle(overViewHeaderStyle);
            Cell cellValue5 = row5.createCell(3);
            cellValue5.setCellValue(overView.getUser());
            cellValue5.setCellStyle(overViewValueStyle);

            // chủ cửa hàng
            Row row6 = sheet.createRow(6);
            Cell cellHeader6 = row6.createCell(1);
            cellHeader6.setCellValue("Chủ cửa hàng");
            cellHeader6.setCellStyle(overViewHeaderStyle);
            Cell cellValue6 = row6.createCell(3);
            cellValue6.setCellValue(overView.getRetailer());
            cellValue6.setCellStyle(overViewValueStyle);

            // sản phẩm
            Row row7 = sheet.createRow(7);
            Cell cellHeader7 = row7.createCell(1);
            cellHeader7.setCellValue("Sản phẩm");
            cellHeader7.setCellStyle(overViewHeaderStyle);
            Cell cellValue7 = row7.createCell(3);
            cellValue7.setCellValue(overView.getProduct());
            cellValue7.setCellStyle(overViewValueStyle);

            for (int i = 3; i <= 7; i++) {
                sheet.addMergedRegion(new CellRangeAddress(
                        i,
                        i,
                        1,
                        2
                ));
            }




            XSSFSheet  sheet1 = (XSSFSheet) workbook.createSheet("Sheet1");
            Row row;
            Cell cell;
            row = sheet1.createRow(0);
            row.createCell(0).setCellValue("Months");
            row.createCell(1).setCellValue("Mountain View");
            row.createCell(2).setCellValue("New York");
            row.createCell(3).setCellValue("Washington");
            row.createCell(4).setCellValue("England");
            row.createCell(5).setCellValue("New Zeland");
            row.createCell(6).setCellValue("Australia");
            for (int r = 1; r < 13; r++) {
                row = sheet1.createRow(r);
                for (int c = 0; c < 7; c++) {
                    cell = row.createCell(c);
                    if (c == 0) cell.setCellValue(r);
                    else cell.setCellFormula("RANDBETWEEN(50, 99)");
                }
            }

            // create data sources
//            XDDFDataSource<Double> months = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, 0, 0));
            XDDFDataSource<Long> months = XDDFDataSourcesFactory.fromArray(dashBoard.getStatisticAccess().toArray(new Long[0]));

            int c = 1;
            XDDFNumericalDataSource<Double> mView = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));
            XDDFNumericalDataSource<Double> nYork = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));
            XDDFNumericalDataSource<Double> washingt = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));
//            XDDFNumericalDataSource<Double> engl = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));
//            XDDFNumericalDataSource<Double> nZeal = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));
//            XDDFNumericalDataSource<Double> austral = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));

            // create sheets drawing
            XSSFDrawing drawing = sheet1.createDrawingPatriarch();

            // needed objeccts for the charts
            XSSFClientAnchor anchor;
            XSSFChart chart;
            XDDFChartLegend legend;
            XDDFCategoryAxis bottomAxis;
            XDDFValueAxis leftAxis;
            XDDFChartData data;
            XDDFChartData.Series series;

//======first line chart============================================================
            // create anchor
            anchor = drawing.createAnchor(0, 0, 0, 0, 8, 0, 14, 12); // anchor col8,row0 to col14,row12
            // create chart
            chart = drawing.createChart(anchor);
            legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM);

            // create the axes
            bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            // create chart data
            data = chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
            // create series
            series = data.addSeries(months, mView);
            series.setTitle("Mountain View", new CellReference(sheet.getSheetName(), 0, 1, true, true));
            series = data.addSeries(months, nYork);
            series.setTitle("New York", new CellReference(sheet.getSheetName(), 0, 2, true, true));
            series = data.addSeries(months, washingt);
            series.setTitle("Washington", new CellReference(sheet.getSheetName(), 0, 3, true, true));
            chart.plot(data);

            solidLineSeries(data, 0, PresetColor.BLUE);
            solidLineSeries(data, 1, PresetColor.RED);
            solidLineSeries(data, 2, PresetColor.GREEN);

////======second line chart============================================================
//            // create anchor
//            anchor = drawing.createAnchor(0, 0, 0, 0, 8, 13, 14, 25); // anchor col8,row13 to col14,row25
//            // create chart
//            chart = drawing.createChart(anchor);
//            legend = chart.getOrAddLegend();
//            legend.setPosition(LegendPosition.BOTTOM);
//
//            // create the axes
//            bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
//            leftAxis = chart.createValueAxis(AxisPosition.LEFT);
//            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
//
//            // create chart data
//            data = chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
//            // create series
//            series = data.addSeries(months, engl);
//            series.setTitle("England", new CellReference(sheet.getSheetName(), 0, 4, true, true));
//            series = data.addSeries(months, nZeal);
//            series.setTitle("New Zeland", new CellReference(sheet.getSheetName(), 0, 5, true, true));
//            series = data.addSeries(months, austral);
//            series.setTitle("Australia", new CellReference(sheet.getSheetName(), 0, 6, true, true));
//            chart.plot(data);
//
//            solidLineSeries(data, 0, PresetColor.BLUE);
//            solidLineSeries(data, 1, PresetColor.RED);
//            solidLineSeries(data, 2, PresetColor.GREEN);


















            // end block overview

            // CellStyle for Age
            CellStyle ageCellStyle = workbook.createCellStyle();
            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));


            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private static void solidLineSeries(XDDFChartData data, int index, PresetColor color) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
        XDDFLineProperties line = new XDDFLineProperties();
        line.setFillProperties(fill);
        XDDFChartData.Series series = data.getSeries().get(index);
        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        }
        properties.setLineProperties(line);
        series.setShapeProperties(properties);
    }
}
