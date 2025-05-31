package vn.thanhtuanle.service.impl;

import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.enums.RoleType;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.common.mapper.CouncilExportData;
import vn.thanhtuanle.common.service.ExcelExporter;
import vn.thanhtuanle.common.service.ExcelRowMapper;
import vn.thanhtuanle.entity.*;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.CouncilDTO;
import vn.thanhtuanle.model.dto.CouncilMemberDTO;
import vn.thanhtuanle.model.dto.TopicCouncilDTO;
import vn.thanhtuanle.model.dto.TopicDTO;
import vn.thanhtuanle.model.request.CreateCouncilRequest;
import vn.thanhtuanle.model.response.UserMemberResponse;
import vn.thanhtuanle.repository.*;
import vn.thanhtuanle.service.CouncilService;
import vn.thanhtuanle.service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouncilServiceImpl implements CouncilService {
    private final CouncilRepository councilRepository;
    private final ModelMapper modelMapper;
    private final CouncilMemberRepository councilMemberRepository;
    private final UserService userService;
    private final TopicRepository topicRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TopicCouncilRepository topicCouncilRepository;

    private final ExcelRowMapper<Council> councilExcelRowMapper;

    private static final List<String> EXCEL_HEADERS = Arrays.asList(
            "ID", "Tên hội đồng", "Số quyết định", "Loại hội đồng", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái", "Đã xóa", "Số thành viên"
    );

    @Override
    public Page<CouncilDTO> findAll(Pageable pageable, String query, String type, String status, Boolean delFlag, Boolean isAdmin) {
        User user = userService.getCurrentUserEntity();
        String currentUserId = user.getId();

        Page<Council> councilPage;
        if (Boolean.TRUE.equals(isAdmin)) {
            councilPage = findAllCouncils(pageable, query, type, status, delFlag);
        } else {
            councilPage = findCouncilsForUser(pageable, query, type, status, delFlag, currentUserId);
        }

        return councilPage.map(council -> {
            CouncilDTO councilDTO = modelMapper.map(council, CouncilDTO.class);

            List<CouncilMember> members = council.getCouncilMembers();
            List<CouncilMemberDTO> memberDTOs = members.stream()
                    .map(member -> {
                        CouncilMemberDTO memberDTO = modelMapper.map(member, CouncilMemberDTO.class);
                        memberDTO.setCouncil(councilDTO);
                        UserMemberResponse userResponse = modelMapper.map(member.getUser(), UserMemberResponse.class);
                        memberDTO.setUser(userResponse);
                        return memberDTO;
                    })
                    .toList();
            councilDTO.setCouncilMembers(memberDTOs);

            List<TopicCouncil> topicCouncils = council.getTopicCouncils();
            List<TopicCouncilDTO> topicCouncilDTOs = topicCouncils.stream()
                    .map(topicCouncil -> {
                        TopicCouncilDTO topicCouncilDTO = modelMapper.map(topicCouncil, TopicCouncilDTO.class);
                        topicCouncilDTO.setCouncil(councilDTO);
                        TopicDTO topicDTO = modelMapper.map(topicCouncil.getTopic(), TopicDTO.class);
                        topicCouncilDTO.setTopic(topicDTO);
                        return topicCouncilDTO;
                    })
                    .toList();
            councilDTO.setTopicCouncils(topicCouncilDTOs);

            return councilDTO;
        });
    }

    // Helper method to find all councils (for admin)
    private Page<Council> findAllCouncils(Pageable pageable, String query, String type, String status, Boolean delFlag) {
        Specification<Council> spec = Specification.where(null);

        if (query != null && !query.isEmpty()) {
            spec = spec.and((root, query1, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + query + "%"));
        }

        if (type != null && !type.isEmpty()) {
            spec = spec.and((root, query1, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type));
        }

        if (status != null && !status.isEmpty()) {
            LocalDate today = LocalDate.now();
            if ("UPCOMING".equalsIgnoreCase(status)) {
                spec = spec.and((root, query1, criteriaBuilder) ->
                        criteriaBuilder.greaterThan(root.get("startDate"), today));
            } else if ("ACTIVE".equalsIgnoreCase(status)) {
                spec = spec.and((root, query1, criteriaBuilder) ->
                        criteriaBuilder.and(
                                criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), today),
                                criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), today)
                        ));
            } else if ("ENDED".equalsIgnoreCase(status)) {
                spec = spec.and((root, query1, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get("endDate"), today));
            }
        }

        if (delFlag != null) {
            spec = spec.and((root, query1, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("delFlag"), delFlag));
        }

        return councilRepository.findAll(spec, pageable);
    }

    // Helper method to find councils for the current user
    private Page<Council> findCouncilsForUser(Pageable pageable, String query, String type, String status, Boolean delFlag, String userId) {
        Specification<Council> spec = Specification.where((root, query1, criteriaBuilder) -> {
            Join<Council, CouncilMember> memberJoin = root.join("councilMembers");
            return criteriaBuilder.equal(memberJoin.get("user").get("id"), userId);
        });

        if (query != null && !query.isEmpty()) {
            spec = spec.and((root, query1, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + query + "%"));
        }

        if (type != null && !type.isEmpty()) {
            spec = spec.and((root, query1, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type));
        }

        if (status != null && !status.isEmpty()) {
            LocalDate today = LocalDate.now();
            if ("UPCOMING".equalsIgnoreCase(status)) {
                spec = spec.and((root, query1, criteriaBuilder) ->
                        criteriaBuilder.greaterThan(root.get("startDate"), today));
            } else if ("ACTIVE".equalsIgnoreCase(status)) {
                spec = spec.and((root, query1, criteriaBuilder) ->
                        criteriaBuilder.and(
                                criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), today),
                                criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), today)
                        ));
            } else if ("ENDED".equalsIgnoreCase(status)) {
                spec = spec.and((root, query1, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get("endDate"), today));
            }
        }

        if (delFlag != null) {
            spec = spec.and((root, query1, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("delFlag"), delFlag));
        }

        return councilRepository.findAll(spec, pageable);
    }

    @Override
    public CouncilDTO createCouncil(CreateCouncilRequest req) {
        if (councilRepository.existsByName(req.getName())) {
            throw new IllegalArgumentException("Council with this name already exists");
        }

        if (councilRepository.existsByDecisionNumber(req.getDecisionNumber())) {
            throw new IllegalArgumentException("Council with this decision number already exists");
        }

        List<Topic> topics = req.getTopics().stream()
                .map(id -> topicRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Topic with ID " + id + " does not exist")))
                .toList();

        Council council = Council.builder()
                .name(req.getName())
                .decisionNumber(req.getDecisionNumber())
                .establishmentDate(req.getEstablishmentDate())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .type(req.getType())
                .notes(req.getNotes())
                .delFlag(false)
                .build();

        council.setTopicCouncils(topics.stream()
                .map(topic -> TopicCouncil.builder()
                        .topic(topic)
                        .council(council)
                        .build())
                .collect(Collectors.toList()));

        Role role = roleRepository.findByName(RoleType.COUNCIL_MEMBER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "type", RoleType.COUNCIL_MEMBER.name()));
        List<User> users = new ArrayList<>();
        List<CouncilMember> councilMembers = req.getMembers().stream()
                .map(memberRequest -> {
                    User user = userService.getUserById(memberRequest.getUserId());

                    if (user.getRoles() == null) {
                        user.setRoles(new HashSet<>());
                    }
                    user.getRoles().add(role);
                    users.add(user);

                    return CouncilMember.builder()
                            .council(council)
                            .user(user)
                            .role(memberRequest.getRole())
                            .build();
                })
                .toList();

        userRepository.saveAll(users);

        council.setDelFlag(false);
        Council savedCouncil = councilRepository.save(council);

        councilMemberRepository.saveAll(councilMembers);

        return modelMapper.map(savedCouncil, CouncilDTO.class);
    }

    @Override
    public CouncilDTO getCouncilById(Long id) {
        Council council = councilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Council", "id", id));

        CouncilDTO councilDTO = modelMapper.map(council, CouncilDTO.class);

        List<CouncilMember> members = council.getCouncilMembers();
        List<CouncilMemberDTO> memberDTOs = members.stream()
                .map(member -> {
                    CouncilMemberDTO memberDTO = modelMapper.map(member, CouncilMemberDTO.class);

                    memberDTO.setCouncil(councilDTO);

                    UserMemberResponse userResponse = modelMapper.map(member.getUser(), UserMemberResponse.class);
                    memberDTO.setUser(userResponse);

                    return memberDTO;
                })
                .toList();

        councilDTO.setCouncilMembers(memberDTOs);

        List<TopicCouncil> topicCouncils = council.getTopicCouncils();
        List<TopicCouncilDTO> topicCouncilDTOs = topicCouncils.stream()
                .map(topicCouncil -> {
                    TopicCouncilDTO topicCouncilDTO = modelMapper.map(topicCouncil, TopicCouncilDTO.class);

                    topicCouncilDTO.setCouncil(councilDTO);

                    TopicDTO topicDTO = modelMapper.map(topicCouncil.getTopic(), TopicDTO.class);
                    topicCouncilDTO.setTopic(topicDTO);

                    return topicCouncilDTO;
                })
                .toList();
        councilDTO.setTopicCouncils(topicCouncilDTOs);

        return councilDTO;
    }

    @Override
    public CouncilDTO updateCouncil(Long id, CreateCouncilRequest req) {
        Council council = councilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Council", "id", id));

        if (!req.getName().equals(council.getName()) && councilRepository.existsByName(req.getName())) {
            throw new IllegalArgumentException("Council with this name already exists");
        }

        if (!req.getDecisionNumber().equals(council.getDecisionNumber()) &&
                councilRepository.existsByDecisionNumber(req.getDecisionNumber())) {
            throw new IllegalArgumentException("Council with this decision number already exists");
        }

        council.setName(req.getName());
        council.setDecisionNumber(req.getDecisionNumber());
        council.setEstablishmentDate(req.getEstablishmentDate());
        council.setStartDate(req.getStartDate());
        council.setEndDate(req.getEndDate());
        council.setType(req.getType());
        council.setNotes(req.getNotes());

        List<Topic> newTopics = req.getTopics().stream()
                .map(topicId -> topicRepository.findById(topicId)
                        .orElseThrow(() -> new IllegalArgumentException("Topic with ID " + topicId + " does not exist")))
                .toList();

        Set<String> newTopicIds = newTopics.stream()
                .map(Topic::getId)
                .collect(Collectors.toSet());

        List<TopicCouncil> currentTopicCouncils = council.getTopicCouncils();
        currentTopicCouncils.removeIf(tc -> !newTopicIds.contains(tc.getTopic().getId()));

        for (Topic topic : newTopics) {
            if (currentTopicCouncils.stream().noneMatch(tc -> tc.getTopic().getId().equals(topic.getId()))) {
                currentTopicCouncils.add(TopicCouncil.builder()
                        .topic(topic)
                        .council(council)
                        .build());
            }
        }

        Role role = roleRepository.findByName(RoleType.COUNCIL_MEMBER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "type", RoleType.COUNCIL_MEMBER.name()));

        List<CouncilMember> currentMembers = council.getCouncilMembers();
        Set<String> newMemberUserIds = req.getMembers().stream()
                .map(memberRequest -> memberRequest.getUserId())
                .collect(Collectors.toSet());

        currentMembers.removeIf(cm -> !newMemberUserIds.contains(cm.getUser().getId()));

        List<User> usersToUpdate = new ArrayList<>();
        for (var memberRequest : req.getMembers()) {
            User user = userService.getUserById(memberRequest.getUserId());

            if (user.getRoles() == null) {
                user.setRoles(new HashSet<>());
            }
            user.getRoles().add(role);
            usersToUpdate.add(user);

            CouncilMember existingMember = currentMembers.stream()
                    .filter(cm -> cm.getUser().getId().equals(memberRequest.getUserId()))
                    .findFirst()
                    .orElse(null);

            if (existingMember != null) {
                existingMember.setRole(memberRequest.getRole());
            } else {
                currentMembers.add(CouncilMember.builder()
                        .council(council)
                        .user(user)
                        .role(memberRequest.getRole())
                        .build());
            }
        }

        userRepository.saveAll(usersToUpdate);

        Council savedCouncil = councilRepository.save(council);

        CouncilDTO councilDTO = modelMapper.map(savedCouncil, CouncilDTO.class);

        List<CouncilMemberDTO> memberDTOs = savedCouncil.getCouncilMembers().stream()
                .map(member -> {
                    CouncilMemberDTO memberDTO = modelMapper.map(member, CouncilMemberDTO.class);
                    memberDTO.setCouncil(councilDTO);
                    UserMemberResponse userResponse = modelMapper.map(member.getUser(), UserMemberResponse.class);
                    memberDTO.setUser(userResponse);
                    return memberDTO;
                })
                .toList();
        councilDTO.setCouncilMembers(memberDTOs);

        // Map topic councils
        List<TopicCouncilDTO> topicCouncilDTOs = savedCouncil.getTopicCouncils().stream()
                .map(topicCouncil -> {
                    TopicCouncilDTO topicCouncilDTO = modelMapper.map(topicCouncil, TopicCouncilDTO.class);
                    topicCouncilDTO.setCouncil(councilDTO);
                    TopicDTO topicDTO = modelMapper.map(topicCouncil.getTopic(), TopicDTO.class);
                    topicCouncilDTO.setTopic(topicDTO);
                    return topicCouncilDTO;
                })
                .toList();
        councilDTO.setTopicCouncils(topicCouncilDTOs);

        return councilDTO;
    }

    @Override
    public void softDeleteCouncil(Long id) {
        Council council = councilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Council", "id", id));

        council.setDelFlag(true);

        councilRepository.save(council);
    }

    @Override
    public byte[] exportExcel(String query, String type, String status, String sort, String order, LocalDate startDate, LocalDate endDate, Boolean delFlag, Boolean isAdmin, Boolean includeMembers, Boolean includeTopics, List<Long> selectedIds) {
        try {
            List<Council> councils;
            if (selectedIds != null && !selectedIds.isEmpty()) {
                councils = councilRepository.findAllById(selectedIds);
            } else {
                Specification<Council> spec = createSpecification(query, type, status, startDate, endDate, delFlag, isAdmin);
                Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Sort sorting = Sort.by(direction, sort);
                councils = councilRepository.findAll(spec, sorting);
            }

            // Preprocess councils to include member and topic details as strings
            List<CouncilExportData> exportData = councils.stream().map(council -> {
                String memberDetails = Boolean.TRUE.equals(includeMembers) && council.getCouncilMembers() != null
                        ? council.getCouncilMembers().stream()
                        .map(member -> member.getUser().getName() + " (" + member.getRole().name() + ")")
                        .collect(Collectors.joining(", "))
                        : "";
                String topicDetails = Boolean.TRUE.equals(includeTopics) && council.getTopicCouncils() != null
                        ? council.getTopicCouncils().stream()
                        .map(topicCouncil -> topicCouncil.getTopic().getTopicCode())
                        .collect(Collectors.joining(", "))
                        : "";
                return new CouncilExportData(council, memberDetails, topicDetails);
            }).toList();

            // Dynamically adjust headers
            List<String> headers = new ArrayList<>(EXCEL_HEADERS);
            if (Boolean.TRUE.equals(includeMembers)) {
                headers.add("Thành viên");
            }
            if (Boolean.TRUE.equals(includeTopics)) {
                headers.add("Đề tài");
            }

            ExcelExporter<Council> exporter = new ExcelExporter<>(headers, exportData.stream().map(CouncilExportData::getCouncil).toList(), councilExcelRowMapper);
            return exporter.exportToExcelWithExtras(exportData, includeMembers, includeTopics);
        } catch (IOException e) {
            throw new AppException(ErrorCode.EXCEL_EXPORT_ERROR);
        }
    }

    @Override
    public List<TopicDTO> getApprovedTopicsByCouncil(Long councilId) {
        Council council = councilRepository.findById(councilId)
                .orElseThrow(() -> new ResourceNotFoundException("Council", "id", councilId));

        return council.getTopicCouncils().stream()
                .filter(topicCouncil -> TopicStatus.IN_CATALOG.equals(topicCouncil.getTopic().getStatus()))
                .map(topicCouncil -> modelMapper.map(topicCouncil.getTopic(), TopicDTO.class))
                .toList();
    }

    @Override
    public List<TopicCouncilDTO> getTopicByCouncilMemberId(Long memberId) {
        CouncilMember councilMember = councilMemberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("CouncilMember", "id", memberId));

        List<TopicCouncil> topicCouncils = topicCouncilRepository.findByCouncil(councilMember.getCouncil());

        return topicCouncils.stream()
                .map(topicCouncil -> modelMapper.map(topicCouncil, TopicCouncilDTO.class))
                .toList();
    }

    private Specification<Council> createSpecification(String query, String type, String status, LocalDate startDate, LocalDate endDate, Boolean delFlag, Boolean isAdmin) {
        Specification<Council> spec = Specification.where(null);

        User user = userService.getCurrentUserEntity();
        String currentUserId = user.getId();

        if (Boolean.TRUE.equals(isAdmin)) {
            // No additional user-based filtering for admins
        } else {
            spec = spec.and((root, query1, criteriaBuilder) -> {
                Join<Council, CouncilMember> memberJoin = root.join("councilMembers");
                return criteriaBuilder.equal(memberJoin.get("user").get("id"), currentUserId);
            });
        }

        if (query != null && !query.isEmpty()) {
            spec = spec.and((root, query1, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + query + "%"));
        }

        if (type != null && !type.isEmpty()) {
            spec = spec.and((root, query1, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type));
        }

        if (status != null && !status.isEmpty()) {
            LocalDate today = LocalDate.now();
            if ("UPCOMING".equalsIgnoreCase(status)) {
                spec = spec.and((root, query1, criteriaBuilder) ->
                        criteriaBuilder.greaterThan(root.get("startDate"), today));
            } else if ("ACTIVE".equalsIgnoreCase(status)) {
                spec = spec.and((root, query1, criteriaBuilder) ->
                        criteriaBuilder.and(
                                criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), today),
                                criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), today)
                        ));
            } else if ("ENDED".equalsIgnoreCase(status)) {
                spec = spec.and((root, query1, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get("endDate"), today));
            }
        }

        if (startDate != null) {
            spec = spec.and((root, query1, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
        }

        if (endDate != null) {
            spec = spec.and((root, query1, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
        }

        if (delFlag != null) {
            spec = spec.and((root, query1, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("delFlag"), delFlag));
        }

        return spec;
    }
}
