package vn.thanhtuanle.common.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import vn.thanhtuanle.common.mapper.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ExcelExporter<T> {

    private final List<String> headers;
    private final List<T> data;
    private final ExcelRowMapper<T> rowMapper;

    public byte[] exportToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Data");
            createHeaderRow(sheet);
            createDataRows(sheet);

            if (rowMapper instanceof UserExcelRowMapper userMapper) {
                userMapper.addDropdowns(sheet, 1, data.size());
            }

            if(rowMapper instanceof RegistrationPeriodExcelRowMapper registrationPeriodExcelRowMapper) {
                registrationPeriodExcelRowMapper.addDropdowns(sheet, 1, data.size());
            }

            if(rowMapper instanceof DepartmentExcelRowMapper departmentExcelRowMapper) {
                departmentExcelRowMapper.addDropdowns(sheet, 1, data.size());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
        }
    }

    private void createDataRows(Sheet sheet) {
        int rowIndex = 1;
        for (T item : data) {
            Row row = sheet.createRow(rowIndex++);
            rowMapper.mapRow(item, row);
        }
    }

    public byte[] exportToExcelWithExtras(List<CouncilExportData> exportData, Boolean includeMembers, Boolean includeTopics) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Data");
            createHeaderRow(sheet);
            createDataRowsWithExtras(sheet, exportData, includeMembers, includeTopics);

            if (rowMapper instanceof CouncilExcelRowMapper councilExcelRowMapper) {
                councilExcelRowMapper.addDropdowns(sheet, 1, exportData.size());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createDataRowsWithExtras(Sheet sheet, List<CouncilExportData> exportData, Boolean includeMembers, Boolean includeTopics) {
        int rowIndex = 1;
        int memberColIndex = 8;
        int topicColIndex = includeMembers ? 9 : 8;

        for (CouncilExportData exportItem : exportData) {
            Row row = sheet.createRow(rowIndex++);
            rowMapper.mapRow((T) exportItem.getCouncil(), row);

            if (Boolean.TRUE.equals(includeMembers) && exportItem.getMemberDetails() != null) {
                Cell memberCell = row.createCell(memberColIndex);
                memberCell.setCellValue(exportItem.getMemberDetails());
            }

            if (Boolean.TRUE.equals(includeTopics) && exportItem.getTopicDetails() != null) {
                Cell topicCell = row.createCell(topicColIndex);
                topicCell.setCellValue(exportItem.getTopicDetails());
            }
        }
    }
}