package vn.thanhtuanle.common.mapper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Component;
import vn.thanhtuanle.common.enums.CouncilType;
import vn.thanhtuanle.common.service.ExcelRowMapper;
import vn.thanhtuanle.entity.Council;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component("councilExcelRowMapper")
public class CouncilExcelRowMapper implements ExcelRowMapper<Council> {

    private static final Integer COL_ID = 0;
    private static final Integer COL_NAME = 1;
    private static final Integer COL_DECISION_NUMBER = 2;
    private static final Integer COL_TYPE = 3;
    private static final Integer COL_START_DATE = 4;
    private static final Integer COL_END_DATE = 5;
    private static final Integer COL_STATUS = 6;
    private static final Integer COL_DEL_FLAG = 7;
    private static final Integer COL_MEMBER_COUNT = 8;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void mapRow(Council item, Row row) {
        if (item.getId() != null) {
            row.createCell(COL_ID).setCellValue(item.getId());
        } else {
            row.createCell(COL_ID).setCellValue("");
        }
        row.createCell(COL_NAME).setCellValue(item.getName() != null ? item.getName() : "");
        row.createCell(COL_DECISION_NUMBER).setCellValue(item.getDecisionNumber() != null ? item.getDecisionNumber() : "");
        row.createCell(COL_TYPE).setCellValue(item.getType() != null ? item.getType().name() : "");
        row.createCell(COL_START_DATE).setCellValue(item.getStartDate() != null ? item.getStartDate().format(DATE_FORMATTER) : "");
        row.createCell(COL_END_DATE).setCellValue(item.getEndDate() != null ? item.getEndDate().format(DATE_FORMATTER) : "");

        // Calculate status based on dates
        String status = calculateStatus(item);
        row.createCell(COL_STATUS).setCellValue(status);

        row.createCell(COL_DEL_FLAG).setCellValue(item.getDelFlag() != null ? item.getDelFlag().toString() : "false");
        row.createCell(COL_MEMBER_COUNT).setCellValue(item.getCouncilMembers() != null ? item.getCouncilMembers().size() : 0);
    }

    public void addDropdowns(Sheet sheet, int fromRow, int toRow) {
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();

        // Dropdown for Council Type
        String[] types = Arrays.stream(CouncilType.values()).map(Enum::name).toArray(String[]::new);
        DataValidationConstraint typeConstraint = validationHelper.createExplicitListConstraint(types);
        CellRangeAddressList typeRange = new CellRangeAddressList(fromRow, toRow, COL_TYPE, COL_TYPE);
        DataValidation typeValidation = validationHelper.createValidation(typeConstraint, typeRange);
        sheet.addValidationData(typeValidation);

        // Dropdown for Status
        String[] statuses = new String[]{"ACTIVE", "UPCOMING", "ENDED"};
        DataValidationConstraint statusConstraint = validationHelper.createExplicitListConstraint(statuses);
        CellRangeAddressList statusRange = new CellRangeAddressList(fromRow, toRow, COL_STATUS, COL_STATUS);
        DataValidation statusValidation = validationHelper.createValidation(statusConstraint, statusRange);
        sheet.addValidationData(statusValidation);
    }

    private String calculateStatus(Council council) {
        if (council.getStartDate() == null || council.getEndDate() == null) {
            return "N/A";
        }
        java.time.LocalDate today = java.time.LocalDate.now();
        if (today.isBefore(council.getStartDate())) {
            return "UPCOMING";
        } else if (today.isAfter(council.getEndDate())) {
            return "ENDED";
        } else {
            return "ACTIVE";
        }
    }
}