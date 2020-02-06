package cn.viworks.vgenerator.generator;

import cn.viworks.vgenerator.Configure;
import cn.viworks.vgenerator.Generator;
import cn.viworks.vgenerator.Payload;
import cn.viworks.vgenerator.data.JdbcData;
import cn.viworks.vgenerator.data.pojo.Column;
import cn.viworks.vgenerator.data.pojo.Table;
import cn.viworks.vgenerator.utils.DateUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchemaGenerator extends Generator {
	private final Logger _logger = Logger.getLogger(SchemaGenerator.class);

    public void run(Payload payload) {
        JdbcData jdbcData = (JdbcData) getVgData();

        List<Table> tables = jdbcData.findAllTables();

        Map<String, List<Table>> groupMap = new HashMap<String, List<Table>>();
        String defaultGroup = "默认";
        for (Table table : tables) {
            String tableName = table.getName();
            String tableRemark = table.getRemarks();
            if (tableName.startsWith("_")) {
                continue;
            }
            
            String group = defaultGroup;
            
            if (tableRemark.indexOf("[") > -1) {
                String REG_PARSE = "^\\[([\u4E00-\u9FA5]+)\\]";
                Pattern p = Pattern.compile(REG_PARSE);
                Matcher m = p.matcher(tableRemark);
                while (m.find()) {
                    int count = m.groupCount();
                    if (count == 1) {
                        group = m.group(1);
                        if (!groupMap.containsKey(group)) {
                            groupMap.put(group, new ArrayList<Table>());
                        }
                    }
                }
            } else {
                if (!groupMap.containsKey(group)) {
                    groupMap.put(group, new ArrayList<Table>());
                }
            }
            groupMap.get(group).add(table);
        }
        writeToExcel(groupMap);
	}
	
	public String getOutputFullPath() {
        String projectHome = (String) Configure.get(Configure.PROJECT_HOME);
	    String targetFile =  "schema" + DateUtil.format(DateUtil.getSystemDate(), "yyyyMMddHHmmss");
        String path = projectHome + targetFile;
        return path;
	}
	
	public void writeToExcel(Map<String, List<Table>> groupMap) {
        String schemaFullPath = getOutputFullPath() + ".xls";
        
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        HSSFRow row;
        HSSFCell cell;
        
        int rowIndex = 0;
        int colIndex = 0;
        
        for (String group : groupMap.keySet()) {
            HSSFSheet sheet = workbook.createSheet(group);
            sheet.setColumnWidth(0, 30 * 256);
            sheet.setColumnWidth(1, 30 * 256);
            sheet.setColumnWidth(2, 20 * 256);
            sheet.setColumnWidth(3, 20 * 256);
            sheet.setColumnWidth(4, 20 * 256);
            sheet.setColumnWidth(5, 100 * 256);
            rowIndex = 0;
            
            List<Table> tables = groupMap.get(group);
            for (Table table : tables) {
                // 表名
                row = sheet.createRow(rowIndex++);
                row.setHeight((short)(24 * 20));
                colIndex = 0;

                cell = row.createCell(colIndex++);
                cell.setCellStyle(tableNameCellStyle(workbook));
                cell.setCellValue(table.getName());

                cell = row.createCell(colIndex++);
                cell.setCellStyle(tableNameCellStyle(workbook));
                cell.setCellValue(table.getRemarks());
                
                cell = row.createCell(colIndex++);
                cell.setCellStyle(tableNameCellStyle(workbook));
                cell.setCellValue("");

                cell = row.createCell(colIndex++);
                cell.setCellStyle(tableNameCellStyle(workbook));
                cell.setCellValue("");

                cell = row.createCell(colIndex++);
                cell.setCellStyle(tableNameCellStyle(workbook));
                cell.setCellValue("");
                
                cell = row.createCell(colIndex++);
                cell.setCellStyle(tableNameCellStyle(workbook));
                cell.setCellValue("");
                
                // title
                row = sheet.createRow(rowIndex++);
                colIndex = 0;
                
                cell = row.createCell(colIndex++);
                cell.setCellStyle(defaultCellStyle(workbook));
                cell.setCellValue("列名");

                cell = row.createCell(colIndex++);
                cell.setCellStyle(defaultCellStyle(workbook));
                cell.setCellValue("类型");
                
                cell = row.createCell(colIndex++);
                cell.setCellStyle(defaultCellStyle(workbook));
                cell.setCellValue("长度");

                cell = row.createCell(colIndex++);
                cell.setCellStyle(defaultCellStyle(workbook));
                cell.setCellValue("允许空值");

                cell = row.createCell(colIndex++);
                cell.setCellStyle(defaultCellStyle(workbook));
                cell.setCellValue("是否主键");
                
                cell = row.createCell(colIndex++);
                cell.setCellStyle(defaultCellStyle(workbook));
                cell.setCellValue("备注");
                
                // 列
                List<Column> columns = table.getColumnList();
                for (Column column : columns) {
                    row = sheet.createRow(rowIndex++);
                    colIndex = 0;

                    cell = row.createCell(colIndex++);
                    cell.setCellStyle(defaultCellStyle(workbook));
                    cell.setCellValue(column.getName());

                    cell = row.createCell(colIndex++);
                    cell.setCellStyle(defaultCellStyle(workbook));
                    if ("BIT".equals(column.getTypeName())) {
                        cell.setCellValue("TINYINT");
                    } else {
                        cell.setCellValue(column.getTypeName());
                    }

                    cell = row.createCell(colIndex++);
                    cell.setCellStyle(defaultCellStyle(workbook));
                    if ("BIT".equals(column.getTypeName())) {
                        cell.setCellValue(1);
                    } else if ("DATETIME".equals(column.getTypeName()) || "TIMESTAMP".equals(column.getTypeName()) || "DATE".equals(column.getTypeName())) {
                        cell.setCellValue(0);
                    } else {
                        cell.setCellValue(column.getColumnSize());
                    }

                    cell = row.createCell(colIndex++);
                    cell.setCellStyle(defaultCellStyle(workbook));
                    if ("id".equals(column.getName())) {
                        cell.setCellValue("否");
                    } else {
                        cell.setCellValue("是");
                    }
                    
                    cell = row.createCell(colIndex++);
                    cell.setCellStyle(defaultCellStyle(workbook));
                    if ("id".equals(column.getName())) {
                        cell.setCellValue("是");
                    }
                    
                    cell = row.createCell(colIndex++);
                    cell.setCellStyle(defaultCellStyle(workbook));
                    cell.setCellValue(column.getRemarks());
                }
            }
        }
        
        OutputStream outputStream = null;
        InputStream inputStream = null;
        
        // 创建一个新的字节数组输出流
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 创建一个新分配的byte字节数组。
        byte[] content = os.toByteArray();
        // 创建一个新的字节数组输出流。
        inputStream = new ByteArrayInputStream(content);
        // 返回输出流
        try {
            outputStream = new FileOutputStream(schemaFullPath);
            byte[] b = new byte[2048];
            int i = 0;
            while ((i = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, i);
            }
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                inputStream = null;
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                outputStream = null;
            }
        }
	}
	
	public HSSFCellStyle defaultCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置抬头背景颜色和相关样式
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置标题的显示位置
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        // 设置内容的显示位置
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(false);
//        style.setFillForegroundColor(HSSFColor.BLUE.index);
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // 设置字体样式 抬头字体
        HSSFFont font = workbook.createFont();
        // 设置字体颜色
        font.setColor(HSSFColor.BLACK.index);
        // 设置字体大小
        font.setFontHeightInPoints((short) 9);
        // 设置字体加粗
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        // 把字体应用
        style.setFont(font);
        return style;
	}
	
    public HSSFCellStyle titleCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置抬头背景颜色和相关样式
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置标题的显示位置
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        // 设置内容的显示位置
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // 设置字体样式 抬头字体
        HSSFFont font = workbook.createFont();
        // 设置字体颜色
        font.setColor(HSSFColor.BLACK.index);
        // 设置字体大小
        font.setFontHeightInPoints((short) 9);
        // 设置字体加粗
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        // 把字体应用
        style.setFont(font);
        return style;
    }

    public HSSFCellStyle tableNameCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置抬头背景颜色和相关样式
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置标题的显示位置
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        // 设置内容的显示位置
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
//        style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        style.setFillForegroundColor(HSSFColor.TAN.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // 设置字体样式 抬头字体
        HSSFFont font = workbook.createFont();
        // 设置字体颜色
        font.setColor(HSSFColor.BLACK.index);
        // 设置字体大小
        font.setFontHeightInPoints((short) 9);
        // 设置字体加粗
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        // 把字体应用
        style.setFont(font);
        return style;
    }
}
