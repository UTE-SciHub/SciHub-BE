package vn.thanhtuanle.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.ContractStatus;
import vn.thanhtuanle.model.dto.ContractDTO;
import vn.thanhtuanle.model.request.ContractFilterParams;
import vn.thanhtuanle.model.request.ContractRequest;

import java.io.IOException;
import java.util.List;

public interface ContractService {
    ContractDTO createContract(ContractRequest req, MultipartFile contractFile) throws IOException;
    ContractDTO getContractById(Integer id);
    void deleteContract(Integer id);
    Page<ContractDTO> findAll(ContractFilterParams params);
    List<ContractDTO> findAllContractsByTopicId(String topicId);
    ContractDTO update(ContractRequest req, MultipartFile file) throws IOException;
}
