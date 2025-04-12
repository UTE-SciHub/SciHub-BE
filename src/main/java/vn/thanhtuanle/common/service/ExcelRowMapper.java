package vn.thanhtuanle.common.service;

import org.apache.poi.ss.usermodel.Row;

@FunctionalInterface
public interface ExcelRowMapper<T> {
    void mapRow(T item, Row row);
}