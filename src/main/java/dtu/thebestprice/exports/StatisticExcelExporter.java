package dtu.thebestprice.exports;

import dtu.thebestprice.entities.Search;
import dtu.thebestprice.payload.response.dashboard.DashBoard;
import dtu.thebestprice.payload.response.dashboard.OverView;
import dtu.thebestprice.repositories.SearchRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.formula.functions.Columns;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

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

    public static ByteArrayInputStream customersToExcel(OverView overView, DashBoard dashBoard,List<Search> searchList) throws IOException {
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            Sheet sheet = workbook.createSheet("Thống kê thebestprice");

            sheet.getPrintSetup().setLandscape(true);
            sheet.getPrintSetup().setPaperSize(PrintSetup.A4_EXTRA_PAPERSIZE);

            Row row;
            Cell cell;
            Font font;
            CellStyle cellStyle;

            //start row nav
            font = workbook.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.RED.getIndex());
            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short) 22);

            cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue("THỐNG KÊ THE BEST PRICE THÁNG " + YearMonth.now().getMonthValue() + " Năm " + YearMonth.now().getYear());
            cell.setCellStyle(cellStyle);

            // end row nav

            // start Block Overview

            cellStyle = workbook.createCellStyle();
            font = workbook.createFont();

            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short) 14);

            cellStyle.setFont(font);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);

            // số người truy cập
            row = sheet.createRow(3);
            cell = row.createCell(1);
            cell.setCellValue("Lượt truy cập (tháng)");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellValue(overView.getVisitor() + " lượt");
            cell.setCellStyle(cellStyle);

            // lượt tìm kiếm
            row = sheet.createRow(4);
            cell = row.createCell(1);
            cell.setCellValue("Lượt tìm kiếm (tháng)");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellValue(overView.getSearch() + " lượt");
            cell.setCellStyle(cellStyle);

            // người dùng
            row = sheet.createRow(5);
            cell = row.createCell(1);
            cell.setCellValue("Người dùng");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellValue(overView.getUser() + " người");
            cell.setCellStyle(cellStyle);

            // chủ cửa hàng
            row = sheet.createRow(6);
            cell = row.createCell(1);
            cell.setCellValue("Chủ cửa hàng");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellValue(overView.getRetailer() + " người");
            cell.setCellStyle(cellStyle);

            // sản phẩm
            row = sheet.createRow(7);
            cell = row.createCell(1);
            cell.setCellValue("Sản phẩm");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellValue(overView.getProduct() + " sản phẩm");
            cell.setCellStyle(cellStyle);


            row = sheet.createRow(8);
            cell = row.createCell(1);
            font = workbook.createFont();
            cellStyle = workbook.createCellStyle();

            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short) 12);
            font.setItalic(true);

            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            cell.setCellStyle(cellStyle);
            cell.setCellValue("Tổng quan");
            // end overview


            // tỷ lệ người dùng có tài khoản
            row = sheet.getRow(3);
            cell = row.createCell(4);
            font = workbook.createFont();
            cellStyle = workbook.createCellStyle();

            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short) 14);

            cellStyle.setFont(font);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            cell.setCellValue("Người dùng có tài khoản");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(5);
            cell.setCellValue(dashBoard.getRateUser().getAuth() + " %");
            cell.setCellStyle(cellStyle);


            row = sheet.getRow(4);
            cell = row.createCell(4);
            cell.setCellValue("Khách vãng lai");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(5);
            cell.setCellValue(dashBoard.getRateUser().getAnonymous() + " %");
            cell.setCellStyle(cellStyle);

            ///////////////////////////////////////////////
            row = sheet.getRow(5);
            cell = row.createCell(4);
            font = workbook.createFont();
            cellStyle = workbook.createCellStyle();

            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short) 12);
            font.setItalic(true);

            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            cell.setCellStyle(cellStyle);
            cell.setCellValue("Tỷ lệ người truy cập");
            // end tỷ le ngươi dung co tai khoan


            // start tông quan lượt truy cập
            font = workbook.createFont();
            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short) 14);

            cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);


            row = sheet.createRow(10);
            cell = row.createCell(2);
            cell.setCellValue("Số lượt truy cập");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            cell.setCellValue("Số lượt tìm kiếm");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            cell.setCellValue("Số lượt xem sản phẩm");
            cell.setCellStyle(cellStyle);

            for (int i = 1; i <= dashBoard.getStatisticAccess().size(); i++) {
                row = sheet.createRow(i + 10);
                cell = row.createCell(1);
                cell.setCellValue("Tháng " + i);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(2);
                cell.setCellValue(dashBoard.getStatisticAccess().get(i - 1));
                cell.setCellStyle(cellStyle);
                cell = row.createCell(3);
                cell.setCellValue(dashBoard.getStatisticSearch().get(i - 1));
                cell.setCellStyle(cellStyle);
                cell = row.createCell(4);
                cell.setCellValue(dashBoard.getStatisticViewCount().get(i - 1));
                cell.setCellStyle(cellStyle);
            }

            row = sheet.createRow(16);
            cell = row.createCell(2);
            font = workbook.createFont();
            cellStyle = workbook.createCellStyle();

            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short) 12);
            font.setItalic(true);

            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            cell.setCellStyle(cellStyle);
            cell.setCellValue("Tổng quan lược truy cập");

//             end tổng quan lượt truy cập

            // start thống kê từ khóa được tìm kiếm nhiều nhất

            row = sheet.createRow(18);
            font = workbook.createFont();
            cellStyle = workbook.createCellStyle();

            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short) 14);

            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);

            cell = row.createCell(1);
            cell.setCellValue("Từ khóa");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellValue("Số lần được tìm kiếm");
            cell.setCellStyle(cellStyle);

            for (int i = 0; i <searchList.size(); i++) {
                row = sheet.createRow(i+19);
                cell = row.createCell(1);
                cell.setCellValue(searchList.get(i).getKeyword());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(2);
                cell.setCellValue(searchList.get(i).getNumberOfSearch());
                cell.setCellStyle(cellStyle);
            }

            row = sheet.createRow(19 + searchList.size());
            cell = row.createCell(1);
            font = workbook.createFont();
            cellStyle = workbook.createCellStyle();

            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short) 12);
            font.setItalic(true);

            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            cell.setCellStyle(cellStyle);
            cell.setCellValue("Thống kê từ khóa được tìm kiếm nhiều nhất");



            // end thống kê từ khóa được tìm kiếm nhiều nhất


            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 4, 5));
            sheet.addMergedRegion(new CellRangeAddress(8, 8, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(16, 16, 2, 3));
            sheet.addMergedRegion(new CellRangeAddress(19 +searchList.size(), 19+searchList.size(), 1, 2));


//            auto with
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);


//            XSSFSheet  sheet1 = (XSSFSheet) workbook.createSheet("Sheet1");
//            Row row;
//            Cell cell;
//            row = sheet1.createRow(0);
//            row.createCell(0).setCellValue("Months");
//            row.createCell(1).setCellValue("Mountain View");
//            row.createCell(2).setCellValue("New York");
//            row.createCell(3).setCellValue("Washington");
//            row.createCell(4).setCellValue("England");
//            row.createCell(5).setCellValue("New Zeland");
//            row.createCell(6).setCellValue("Australia");
//            for (int r = 1; r < 13; r++) {
//                row = sheet1.createRow(r);
//                for (int c = 0; c < 7; c++) {
//                    cell = row.createCell(c);
//                    if (c == 0) cell.setCellValue(r);
//                    else cell.setCellFormula("RANDBETWEEN(50, 99)");
//                }
//            }
//
//            // create data sources
////            XDDFDataSource<Double> months = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, 0, 0));
//            XDDFDataSource<Long> months = XDDFDataSourcesFactory.fromArray(dashBoard.getStatisticAccess().toArray(new Long[0]));
//
//            int c = 1;
//            XDDFNumericalDataSource<Double> mView = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));
//            XDDFNumericalDataSource<Double> nYork = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));
//            XDDFNumericalDataSource<Double> washingt = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));
////            XDDFNumericalDataSource<Double> engl = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));
////            XDDFNumericalDataSource<Double> nZeal = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));
////            XDDFNumericalDataSource<Double> austral = XDDFDataSourcesFactory.fromNumericCellRange(sheet1, new CellRangeAddress(1, 12, c, c++));
//
//            // create sheets drawing
//            XSSFDrawing drawing = sheet1.createDrawingPatriarch();
//
//            // needed objeccts for the charts
//            XSSFClientAnchor anchor;
//            XSSFChart chart;
//            XDDFChartLegend legend;
//            XDDFCategoryAxis bottomAxis;
//            XDDFValueAxis leftAxis;
//            XDDFChartData data;
//            XDDFChartData.Series series;
//
////======first line chart============================================================
//            // create anchor
//            anchor = drawing.createAnchor(0, 0, 0, 0, 8, 0, 14, 12); // anchor col8,row0 to col14,row12
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
//            series = data.addSeries(months, mView);
//            series.setTitle("Mountain View", new CellReference(sheet.getSheetName(), 0, 1, true, true));
//            series = data.addSeries(months, nYork);
//            series.setTitle("New York", new CellReference(sheet.getSheetName(), 0, 2, true, true));
//            series = data.addSeries(months, washingt);
//            series.setTitle("Washington", new CellReference(sheet.getSheetName(), 0, 3, true, true));
//            chart.plot(data);
//
//            solidLineSeries(data, 0, PresetColor.BLUE);
//            solidLineSeries(data, 1, PresetColor.RED);
//            solidLineSeries(data, 2, PresetColor.GREEN);

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
