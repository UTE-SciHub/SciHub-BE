package vn.thanhtuanle.common.mapper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Component;
import vn.thanhtuanle.common.enums.Gender;
import vn.thanhtuanle.common.enums.UserStatus;
import vn.thanhtuanle.common.service.ExcelRowMapper;
import vn.thanhtuanle.entity.User;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component("userExcelRowMapper")
public class UserExcelRowMapper implements ExcelRowMapper<User> {

    private static final Integer COL_ID = 0;
    private static final Integer COL_EMAIL = 1;
    private static final Integer COL_NAME = 2;
    private static final Integer COL_PHONE_NUMBER = 3;
    private static final Integer COL_STATUS = 4;
    private static final Integer COL_GENDER = 5;
    private static final Integer COL_DOB = 6;
    private static final Integer COL_LAST_LOGIN = 7;
    private static final Integer COL_CREATED_BY = 8;
    private static final Integer COL_CREATED_AT = 9;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void mapRow(User user, Row row) {
        row.createCell(COL_ID).setCellValue(user.getId() != null ? user.getId() : "");
        row.createCell(COL_EMAIL).setCellValue(user.getEmail() != null ? user.getEmail() : "");
        row.createCell(COL_NAME).setCellValue(user.getName() != null ? user.getName() : "");
        row.createCell(COL_PHONE_NUMBER).setCellValue(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
        row.createCell(COL_STATUS).setCellValue(user.getStatus() != null ? user.getStatus().name() : "");
        row.createCell(COL_GENDER).setCellValue(user.getGender() != null ? user.getGender().name() : "");
        row.createCell(COL_DOB).setCellValue(user.getDob() != null ? user.getDob().format(DATE_FORMATTER) : "");
        row.createCell(COL_LAST_LOGIN).setCellValue(user.getLastLogin() != null ? user.getLastLogin().format(DATE_FORMATTER) : "");
        row.createCell(COL_CREATED_BY).setCellValue(user.getCreatedBy() != null ? user.getCreatedBy() : "");
        row.createCell(COL_CREATED_AT).setCellValue(user.getCreatedAt() != null ? user.getCreatedAt().format(DATE_FORMATTER) : "");
    }

    public void addDropdowns(Sheet sheet, int fromRow, int toRow) {
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();

        String[] genders = Arrays.stream(Gender.values()).map(Enum::name).toArray(String[]::new);
        DataValidationConstraint genderConstraint = validationHelper.createExplicitListConstraint(genders);
        CellRangeAddressList genderRange = new CellRangeAddressList(fromRow, toRow, COL_GENDER, COL_GENDER);
        DataValidation genderValidation = validationHelper.createValidation(genderConstraint, genderRange);
        sheet.addValidationData(genderValidation);

        String[] statuses = Arrays.stream(UserStatus.values()).map(Enum::name).toArray(String[]::new);
        DataValidationConstraint statusConstraint = validationHelper.createExplicitListConstraint(statuses);
        CellRangeAddressList statusRange = new CellRangeAddressList(fromRow, toRow, COL_STATUS, COL_STATUS);
        DataValidation statusValidation = validationHelper.createValidation(statusConstraint, statusRange);
        sheet.addValidationData(statusValidation);
    }
}