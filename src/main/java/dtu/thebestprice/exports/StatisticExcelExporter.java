package dtu.thebestprice.exports;

import dtu.thebestprice.entities.Search;
import dtu.thebestprice.payload.response.dashboard.DashBoard;
import dtu.thebestprice.payload.response.dashboard.OverView;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.YearMonth;
import java.util.List;

@Data
public class StatisticExcelExporter {

    public static ByteArrayInputStream customersToExcel(OverView overView, DashBoard dashBoard, List<Search> searchList) throws IOException {
        try (
                XSSFWorkbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            XSSFSheet sheet = workbook.createSheet("Thống kê thebestprice");

            sheet.getPrintSetup().setLandscape(true);
            sheet.getPrintSetup().setPaperSize(PrintSetup.A4_EXTRA_PAPERSIZE);
            sheet.setDisplayGridlines(false);

            Row row;
            Cell cell;

            Font bigFont;
            Font catAndValFont;
            Font descFont;

            CellStyle bigCellStyle;
            CellStyle catCellStyle;
            CellStyle valCellStyle;
            CellStyle descCellStyle;

            //start row nav
            bigFont = workbook.createFont();
            bigFont.setBold(true);
            bigFont.setColor(IndexedColors.RED.getIndex());
            bigFont.setFontName("Times New Roman");
            bigFont.setFontHeightInPoints((short) 22);

            bigCellStyle = workbook.createCellStyle();
            bigCellStyle.setFont(bigFont);
            bigCellStyle.setAlignment(HorizontalAlignment.CENTER);

            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue("THỐNG KÊ THE BEST PRICE THÁNG " + YearMonth.now().getMonthValue() + " NĂM " + YearMonth.now().getYear());
            cell.setCellStyle(bigCellStyle);

            // end row nav

            // start Block Overview

            catCellStyle = workbook.createCellStyle();
            catAndValFont = workbook.createFont();

            catAndValFont.setFontName("Times New Roman");
            catAndValFont.setFontHeightInPoints((short) 14);

            catCellStyle.setFont(catAndValFont);
            catCellStyle.setBorderLeft(BorderStyle.THIN);
            catCellStyle.setBorderTop(BorderStyle.THIN);
            catCellStyle.setBorderRight(BorderStyle.THIN);
            catCellStyle.setBorderBottom(BorderStyle.THIN);
            catCellStyle.setAlignment(HorizontalAlignment.CENTER);
            catCellStyle.setFillForegroundColor(IndexedColors.LIME.index);
            catCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            valCellStyle = workbook.createCellStyle();
            valCellStyle.setFont(catAndValFont);
            valCellStyle.setBorderLeft(BorderStyle.THIN);
            valCellStyle.setBorderTop(BorderStyle.THIN);
            valCellStyle.setBorderRight(BorderStyle.THIN);
            valCellStyle.setBorderBottom(BorderStyle.THIN);
            valCellStyle.setAlignment(HorizontalAlignment.CENTER);
            valCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
            valCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            descFont = workbook.createFont();
            descFont.setFontName("Times New Roman");
            descFont.setFontHeightInPoints((short) 12);
            descFont.setItalic(true);


            descCellStyle = workbook.createCellStyle();
            descCellStyle.setFont(descFont);
            descCellStyle.setAlignment(HorizontalAlignment.CENTER);


            // số người truy cập
            row = sheet.createRow(3);
            cell = row.createCell(1);
            cell.setCellValue("Lượt truy cập (tháng)");
            cell.setCellStyle(catCellStyle);
            cell = row.createCell(2);
            cell.setCellValue(overView.getVisitor() + " lượt");
            cell.setCellStyle(valCellStyle);

            // lượt tìm kiếm
            row = sheet.createRow(4);
            cell = row.createCell(1);
            cell.setCellValue("Lượt tìm kiếm (tháng)");
            cell.setCellStyle(catCellStyle);
            cell = row.createCell(2);
            cell.setCellValue(overView.getSearch() + " lượt");
            cell.setCellStyle(valCellStyle);

            // người dùng
            row = sheet.createRow(5);
            cell = row.createCell(1);
            cell.setCellValue("Người dùng");
            cell.setCellStyle(catCellStyle);
            cell = row.createCell(2);
            cell.setCellValue(overView.getUser() + " người");
            cell.setCellStyle(valCellStyle);

            // chủ cửa hàng
            row = sheet.createRow(6);
            cell = row.createCell(1);
            cell.setCellValue("Chủ cửa hàng");
            cell.setCellStyle(catCellStyle);
            cell = row.createCell(2);
            cell.setCellValue(overView.getRetailer() + " người");
            cell.setCellStyle(valCellStyle);

            // sản phẩm
            row = sheet.createRow(7);
            cell = row.createCell(1);
            cell.setCellValue("Sản phẩm");
            cell.setCellStyle(catCellStyle);
            cell = row.createCell(2);
            cell.setCellValue(overView.getProduct() + " sản phẩm");
            cell.setCellStyle(valCellStyle);

            row = sheet.createRow(8);
            cell = row.createCell(1);
            cell.setCellStyle(descCellStyle);
            cell.setCellValue("Tổng quan");
            // end overview


            // tỷ lệ người dùng có tài khoản
            row = sheet.createRow(10);
            cell = row.createCell(1);

            cell.setCellValue("Người dùng có tài khoản");
            cell.setCellStyle(catCellStyle);

            cell = row.createCell(2);
            cell.setCellValue(dashBoard.getRateUser().getAuth());
            cell.setCellStyle(valCellStyle);


            row = sheet.createRow(11);
            cell = row.createCell(1);
            cell.setCellValue("Khách vãng lai");
            cell.setCellStyle(catCellStyle);

            cell = row.createCell(2);
            cell.setCellValue(dashBoard.getRateUser().getAnonymous());
            cell.setCellStyle(valCellStyle);

            ///////////////////////////////////////////////
            row = sheet.createRow(12);
            cell = row.createCell(1);
            cell.setCellStyle(descCellStyle);
            cell.setCellValue("Tỷ lệ người truy cập");
            // end tỷ le ngươi dung co tai khoan


            // vẽ hinh tỉ lệ người dùng có tài khoản

            // create drawing and anchor
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 4, 3, 8, 13);

            // create chart
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("Thống kê người dùng hệ thống");
            chart.setTitleOverlay(false);
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            XDDFDataSource<String> cat = XDDFDataSourcesFactory.fromStringCellRange(sheet,
                    new CellRangeAddress(10, 11, 1, 1));
            XDDFNumericalDataSource<Double> val = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                    new CellRangeAddress(10, 11, 2, 2));

            XDDFChartData chartData = chart.createData(ChartTypes.PIE, null, null);
            chartData.setVaryColors(true);
            XDDFChartData.Series series = chartData.addSeries(cat, val);
            chart.plot(chartData);

            // add data labels
            if (!chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).isSetDLbls())
                chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).addNewDLbls();
            chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls()
                    .addNewShowLegendKey().setVal(true);
            chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls()
                    .addNewShowPercent().setVal(true);
            chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls()
                    .addNewShowLeaderLines().setVal(true);
            chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls()
                    .addNewShowVal().setVal(false);
            chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls()
                    .addNewShowCatName().setVal(false);
            chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls()
                    .addNewShowSerName().setVal(false);
            chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDLbls()
                    .addNewShowBubbleSize().setVal(false);

            // do not auto delete the title; is necessary for showing title in Calc
            if (chart.getCTChart().getAutoTitleDeleted() == null) chart.getCTChart().addNewAutoTitleDeleted();
            chart.getCTChart().getAutoTitleDeleted().setVal(false);

            // data point colors; is necessary for showing data points in Calc
            int pointCount = series.getCategoryData().getPointCount();
            for (int p = 0; p < pointCount; p++) {
                chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).addNewDPt().addNewIdx().setVal(p);
                chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).getDPtArray(p)
                        .addNewSpPr().addNewSolidFill().addNewSrgbClr().setVal(DefaultIndexedColorMap.getDefaultRGB(p + 10));
            }

            // end hình tỉ lệ người dùng có tài khoản

            // start tông quan lượt truy cập
            row = sheet.createRow(14);
            cell = row.createCell(2);
            cell.setCellValue("Số lượt truy cập");
            cell.setCellStyle(catCellStyle);
            cell = row.createCell(3);
            cell.setCellValue("Số lượt tìm kiếm");
            cell.setCellStyle(catCellStyle);
            cell = row.createCell(4);
            cell.setCellValue("Số lượt xem sản phẩm");
            cell.setCellStyle(catCellStyle);

            cell = row.createCell(1);
            cell.setCellValue("Tháng");
            cell.setCellStyle(catCellStyle);

            for (int i = 0; i < dashBoard.getStatisticAccess().size(); i++) {
                row = sheet.createRow(i + 15);
                cell = row.createCell(1);
                cell.setCellValue(i + 1);
                cell.setCellStyle(valCellStyle);
                cell = row.createCell(2);
                cell.setCellValue(dashBoard.getStatisticAccess().get(i));
                cell.setCellStyle(valCellStyle);
                cell = row.createCell(3);
                cell.setCellValue(dashBoard.getStatisticSearch().get(i));
                cell.setCellStyle(valCellStyle);
                cell = row.createCell(4);
                cell.setCellValue(dashBoard.getStatisticViewCount().get(i));
                cell.setCellStyle(valCellStyle);
            }

            row = sheet.createRow(16 + dashBoard.getStatisticAccess().size());
            cell = row.createCell(2);
            cell.setCellStyle(descCellStyle);
            cell.setCellValue("Tổng quan lược truy cập");

//             end tổng quan lượt truy cập


            XSSFDrawing drawing2 = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor2 = drawing2.createAnchor(0, 0, 0, 0, 1, 16 + dashBoard.getStatisticAccess().size(), 5, 28 + dashBoard.getStatisticAccess().size());

            XSSFChart chart2 = drawing2.createChart(anchor2);
            chart2.setTitleText("Tổng quan lược truy cập");
            chart2.setTitleOverlay(false);

            XDDFChartLegend legend2 = chart2.getOrAddLegend();
            legend2.setPosition(LegendPosition.TOP_RIGHT);

            XDDFCategoryAxis bottomAxis = chart2.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("Tháng");
            XDDFValueAxis leftAxis = chart2.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("Lượt");

            XDDFNumericalDataSource<Double> thang = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                    new CellRangeAddress(15, dashBoard.getStatisticAccess().size() + 14, 1, 1));

            XDDFNumericalDataSource<Double> luotTruyCap = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                    new CellRangeAddress(15, dashBoard.getStatisticAccess().size() + 14, 2, 2));

            XDDFNumericalDataSource<Double> luotTimKiem = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                    new CellRangeAddress(15, dashBoard.getStatisticSearch().size() + 14, 3, 3));

            XDDFNumericalDataSource<Double> luotXemSanPham = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                    new CellRangeAddress(15, dashBoard.getStatisticViewCount().size() + 14, 4, 4));

            XDDFLineChartData data = (XDDFLineChartData) chart2.createData(ChartTypes.LINE, bottomAxis, leftAxis);


            XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(thang, luotTruyCap);
            String color = "#0a1931";
            lineSeriesColor(series1, XDDFColor.from(hex2Rgb(color)));
            series1.setTitle("Lượt truy cập", null);
            series1.setSmooth(true);
            series1.setMarkerStyle(MarkerStyle.CIRCLE);

            XDDFLineChartData.Series series2 = (XDDFLineChartData.Series) data.addSeries(thang, luotTimKiem);
            color = "#185adb";
            lineSeriesColor(series2, XDDFColor.from(hex2Rgb(color)));
            series2.setTitle("Lượt tìm kiếm", null);
            series2.setSmooth(true);
            series2.setMarkerStyle(MarkerStyle.CIRCLE);


            XDDFLineChartData.Series series3 = (XDDFLineChartData.Series) data.addSeries(thang, luotXemSanPham);
            color = "#ffc947";
            lineSeriesColor(series3, XDDFColor.from(hex2Rgb(color)));
            series3.setTitle("Lượt xem sản phẩm", null);
            series3.setSmooth(true);
            series3.setMarkerStyle(MarkerStyle.CIRCLE);


            chart2.plot(data);


            // start thống kê từ khóa được tìm kiếm nhiều nhất
//
            row = sheet.createRow(30 + dashBoard.getStatisticAccess().size());
            cell = row.createCell(1);
            cell.setCellValue("Từ khóa");
            cell.setCellStyle(catCellStyle);
            cell = row.createCell(2);
            cell.setCellValue("Số lần được tìm kiếm");
            cell.setCellStyle(catCellStyle);

            for (int i = 0; i < searchList.size(); i++) {
                row = sheet.createRow(i + 31 + dashBoard.getStatisticAccess().size());
                cell = row.createCell(1);
                cell.setCellValue(searchList.get(i).getKeyword());
                cell.setCellStyle(valCellStyle);

                cell = row.createCell(2);
                cell.setCellValue(searchList.get(i).getNumberOfSearch());
                cell.setCellStyle(valCellStyle);
            }

            row = sheet.createRow(31 + dashBoard.getStatisticAccess().size() + searchList.size());
            cell = row.createCell(1);

            cell.setCellStyle(descCellStyle);
            cell.setCellValue("Thống kê từ khóa được tìm kiếm nhiều nhất");

            // end thống kê từ khóa được tìm kiếm nhiều nhất

            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
            sheet.addMergedRegion(new CellRangeAddress(8, 8, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(12, 12, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(16 + dashBoard.getStatisticAccess().size(), 16 + dashBoard.getStatisticAccess().size(), 2, 3));
            sheet.addMergedRegion(new CellRangeAddress(31 + dashBoard.getStatisticAccess().size() + searchList.size(), 31 + dashBoard.getStatisticAccess().size() + searchList.size(), 1, 2));


//            auto with
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private static void lineSeriesColor(XDDFChartData.Series series, XDDFColor color) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(color);
        XDDFLineProperties line = new XDDFLineProperties();
        line.setFillProperties(fill);
        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        }
        properties.setLineProperties(line);
        series.setShapeProperties(properties);
    }

    private static byte[] hex2Rgb(String colorStr) {
        int r = Integer.valueOf(colorStr.substring(1, 3), 16);
        int g = Integer.valueOf(colorStr.substring(3, 5), 16);
        int b = Integer.valueOf(colorStr.substring(5, 7), 16);
        return new byte[]{(byte) r, (byte) g, (byte) b};
    }
}
