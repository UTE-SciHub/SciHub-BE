package vn.thanhtuanle.common.mapper;

import org.springframework.stereotype.Component;
import vn.thanhtuanle.common.service.ExcelExporter;
import vn.thanhtuanle.common.service.ExcelRowMapper;

import java.util.List;

@Component
public class ExcelExporterFactory {

    public <T> ExcelExporter<T> create(List<String> headers, List<T> data, ExcelRowMapper<T> rowMapper) {
        return new ExcelExporter<>(headers, data, rowMapper);
    }
}

