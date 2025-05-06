package vn.thanhtuanle.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.repository.CouncilRepository;
import vn.thanhtuanle.service.CouncilService;

@Service
@RequiredArgsConstructor
public class CouncilServiceImpl implements CouncilService {
    private final CouncilRepository councilRepository;
    private final ModelMapper modelMapper;
}
