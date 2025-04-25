package vn.thanhtuanle.common.mapper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Component;
import vn.thanhtuanle.common.service.ExcelRowMapper;
import vn.thanhtuanle.entity.Department;

@Component("departmentExcelRowMapper")
public class DepartmentExcelRowMapper implements ExcelRowMapper<Department> {
    private static final Integer COL_ID = 0;
    private static final Integer COL_NAME = 1;
    private static final Integer COL_PHONE_NUMBER = 2;
    private static final Integer COL_EMAIL = 3;
    private static final Integer COL_DEL_FLAG = 4;

    @Override
    public void mapRow(Department department, Row row) {
        if (department.getId() != null) {
            row.createCell(COL_ID).setCellValue(department.getId());
        } else {
            row.createCell(COL_ID).setCellValue("");
        }
        row.createCell(COL_NAME).setCellValue(department.getName() != null ? department.getName() : "");
        row.createCell(COL_PHONE_NUMBER).setCellValue(department.getPhoneNumber() != null ? department.getPhoneNumber() : "");
        row.createCell(COL_EMAIL).setCellValue(department.getEmail() != null ? department.getEmail() : "");
        String delFlagText = "Chưa rõ";
        if (department.getDelFlag() != null) {
            delFlagText = department.getDelFlag() ? "Đã dừng hoạt động" : "Đang hoạt động";
        }
        row.createCell(COL_DEL_FLAG).setCellValue(delFlagText);

    }

    public void addDropdowns(Sheet sheet, int fromRow, int toRow) {
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(
                new String[] {"Đang hoạt động", "Đã dừng hoạt động"}
        );
        CellRangeAddressList addressList = new CellRangeAddressList(fromRow, toRow, COL_DEL_FLAG, COL_DEL_FLAG);
        DataValidation validation = validationHelper.createValidation(constraint, addressList);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }

}
