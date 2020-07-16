package com.cyc.poi.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 快速读取/导出Excel文档工具类,基于Apache-Poi
 *
 * @author lihuasheng
 * @since 2020/6/3 22:12
 */
public class SimpleExcelUtils {

    private static final Logger logger = LoggerFactory.getLogger(SimpleExcelUtils.class);
    private static final String LOG_PREFIX = "【SimpleExcelUtils】-";
    //读取excel的哪个sheet,默认为0
    private static final Integer SHEET_INDEX = 0;
    //标题行在第几行(从0开始)
    private static final Integer TITLE_LINE = 0;


    /**
     * 读取Excel文件
     *
     * @param file 上传的excel文件
     * @return List
     */
    public static List<Map<String, String>> readExcel(MultipartFile file) {
        List<Map<String, String>> result = new ArrayList<>();
        List<String> title = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
            for (Row row : sheet) {
                //标题行以上的都忽略
                if (row.getRowNum() < TITLE_LINE) {
                    continue;
                }
                //标题行,提取标题(作为Map中的key)
                if (row.getRowNum() == TITLE_LINE) {
                    title = getRowContent(row);
                    continue;
                }
                //内容行(直接使用String来读取)
                List<String> rowContent = getRowContent(row);
                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int i = 0; i < title.size(); i++) {
                    if(rowContent.size() < title.size()){
                        continue;
                    }
                    rowMap.put(title.get(i), rowContent.get(i));
                }
                result.add(rowMap);
            }
        } catch (Exception e) {
            logger.info(LOG_PREFIX + "Excel文件读取失败", e);
        }
        return result;
    }

    /**
     * 导出Excel文件
     *
     * @param response  HttpServletResponse
     * @param list      数据List
     * @param titleLink 有序的Map,按顺序读取标题(key)与数据实体字段名(value)的关系,例:{"姓名",name}
     * @throws Exception 异常
     */
    public static <T> void writeExcel(HttpServletResponse response, List<T> list, LinkedHashMap<String, String> titleLink) throws Exception {
        if (list.size() == 0) {
            logger.info(LOG_PREFIX + "数据列表为空");
            return;
        }
        Class<?> tClass = list.get(0).getClass();
        Workbook workBook = new SXSSFWorkbook();
        //创建Sheet
        Sheet sheet = workBook.createSheet("Sheet1");
        //设置Sheet的基本属性
        setSheet(sheet);
        //生成表头字段列
        List<String> title = new ArrayList<>(titleLink.keySet());
        Row row = sheet.createRow(0);
        for (int i = 0; i < titleLink.size(); i++) {
            //列宽默认,可修改
            sheet.setColumnWidth(i, title.get(i).length() * 255 * 5);
            Cell cell = row.createCell(i);
            cell.setCellValue(title.get(i));
            cell.setCellStyle(initTitleCellStyle(workBook));
        }
        //生成数据列,使用反射获取对象的值
        for (int i = 0; i < list.size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            for (int j = 0; j < title.size(); j++) {
                Cell cell = dataRow.createCell(j);
                String content = title.get(j);
                String Field = titleLink.get(content);
                Method method = tClass.getMethod("get" + StringUtils.capitalize(Field));
                cell.setCellValue(String.valueOf(method.invoke(list.get(i))));
                cell.setCellStyle(initContentCellStyle(workBook));
            }
        }
        //导出下载.文件名使用时间.可修改
        downLoadFile(response, null, workBook);
    }


    /**
     * 获取Excel行每个单元格的内容
     *
     * @param row 列数
     * @return 当前行的List
     */
    private static List<String> getRowContent(Row row) {

        int columnNum = row.getLastCellNum() - row.getFirstCellNum();
        String[] singleRow = new String[columnNum];
        for (int i = 0; i < columnNum; i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            switch (cell.getCellTypeEnum()) {
                case BOOLEAN:
                    singleRow[i] = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        singleRow[i] = format.format(cell.getDateCellValue());
                    } else {
                        HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
                        singleRow[i] = dataFormatter.formatCellValue(cell);
                    }
                    break;
                case STRING:
                    singleRow[i] = cell.getStringCellValue().trim();
                    break;
                case FORMULA:
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    singleRow[i] = cell.getStringCellValue();
                    if (singleRow[i] != null) {
                        singleRow[i] = singleRow[i].replaceAll("#N/A", "").trim();
                    }
                    break;
                case BLANK:
                case ERROR:
                default:
                    singleRow[i] = "";
                    break;
            }
        }
        return new ArrayList<>(Arrays.asList(singleRow));
    }

    /**
     * 设置列的一些属性
     *
     * @param sheet sheet
     */
    private static void setSheet(Sheet sheet) {
        //设置垂直居中
        sheet.setVerticallyCenter(true);
        //合并行,必须在创建之前,(起始行,起始行,终止列,终止列)
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titles.length - 1));
        //sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, titles.length - 1));
    }

    /**
     * 初始化列样式(可自定义修改)
     */
    private static CellStyle initTitleCellStyle(Workbook workBook) {
        CellStyle headerStyle = workBook.createCellStyle();// 创建标题样式
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 设置垂直居中
        headerStyle.setAlignment(HorizontalAlignment.CENTER); // 设置水平居中
        headerStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        headerStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        headerStyle.setBorderTop(BorderStyle.THIN);// 上边框
        headerStyle.setBorderRight(BorderStyle.THIN);// 右边框
        Font headerFont = workBook.createFont(); // 创建字体样式
        headerFont.setBold(true); // 字体加粗
        headerFont.setFontName("微软雅黑"); // 设置字体类型
        headerFont.setFontHeightInPoints((short) 10); // 设置字体大小
        headerStyle.setFont(headerFont); // 为标题样式设置字体样式
        return headerStyle;
    }

    /**
     * 初始化excel内容表格样式(可自定义修改)
     */
    private static CellStyle initContentCellStyle(Workbook workBook) {
        CellStyle cell_Style = workBook.createCellStyle();// 设置字体样式
        cell_Style.setAlignment(HorizontalAlignment.CENTER);
        cell_Style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直对齐居中
        cell_Style.setWrapText(true); // 设置为自动换行
        Font cell_Font = workBook.createFont();
        cell_Font.setFontName("微软雅黑");
        cell_Font.setFontHeightInPoints((short) 10);
        cell_Style.setFont(cell_Font);
        cell_Style.setBorderBottom(BorderStyle.THIN); // 下边框
        cell_Style.setBorderLeft(BorderStyle.THIN);// 左边框
        cell_Style.setBorderTop(BorderStyle.THIN);// 上边框
        cell_Style.setBorderRight(BorderStyle.THIN);// 右边框
        return cell_Style;
    }

    /**
     * 导出下载
     *
     * @param response HttpServletResponse
     * @param fileName 文件名
     * @param workBook Workbook
     * @throws Exception 异常
     */
    public static void downLoadFile(HttpServletResponse response, String fileName, Workbook workBook) throws Exception {
        try (OutputStream os = response.getOutputStream()) {
            if (StringUtils.isBlank(fileName)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
                fileName = formatter.format(new Date());
            }
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20") + ".xlsx");
            workBook.write(os);
            os.flush();
        } catch (Exception e) {
            logger.info(LOG_PREFIX + "workBook 释放失败");
            throw e;
        }
    }

    /**
     * 提供验证文件结尾是否为xls或者xlsx
     *
     * @param fileName 文件名
     * @return 文件名是否是excel文件
     */
    public static boolean suffixCheck(String fileName) {
        Pattern pattern = Pattern.compile(".*(.xls|.xlsx|.XLS|.XLSX)$");
        Matcher matcher = pattern.matcher(fileName);
        return matcher.matches();
    }

}