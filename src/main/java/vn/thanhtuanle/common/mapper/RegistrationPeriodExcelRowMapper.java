package vn.thanhtuanle.common.mapper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Component;
import vn.thanhtuanle.common.enums.Gender;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.common.enums.UserStatus;
import vn.thanhtuanle.common.service.ExcelRowMapper;
import vn.thanhtuanle.entity.RegistrationPeriod;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component("registrationPeriodExcelRowMapper")
public class RegistrationPeriodExcelRowMapper implements ExcelRowMapper<RegistrationPeriod> {

    private static final Integer COL_ID = 0;
    private static final Integer COL_DECISION_NUMBER = 1;
    private static final Integer COL_TITLE = 2;
    private static final Integer COL_START_DATE = 3;
    private static final Integer COL_END_DATE = 4;
    private static final Integer COL_STATUS = 5;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void mapRow(RegistrationPeriod item, Row row) {
        row.createCell(COL_ID).setCellValue(item.getId() != null ? item.getId() : "");
        row.createCell(COL_DECISION_NUMBER).setCellValue(item.getDecisionNumber() != null ? item.getDecisionNumber() : "");
        row.createCell(COL_TITLE).setCellValue(item.getTitle() != null ? item.getTitle() : "");
        row.createCell(COL_START_DATE).setCellValue(item.getStartDate() != null ? item.getStartDate().format(DATE_FORMATTER) : "");
        row.createCell(COL_END_DATE).setCellValue(item.getEndDate() != null ? item.getEndDate().format(DATE_FORMATTER) : "");
        row.createCell(COL_STATUS).setCellValue(item.getStatus() != null ? item.getStatus().name() : "");
    }

    public void addDropdowns(Sheet sheet, int fromRow, int toRow) {
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();

        String[] status = Arrays.stream(RegistrationPeriodsStatus.values()).map(Enum::name).toArray(String[]::new);
        DataValidationConstraint statusConstraint = validationHelper.createExplicitListConstraint(status);
        CellRangeAddressList statusRange = new CellRangeAddressList(fromRow, toRow, COL_STATUS, COL_STATUS);
        DataValidation statusValidation = validationHelper.createValidation(statusConstraint, statusRange);
        sheet.addValidationData(statusValidation);
    }
}
