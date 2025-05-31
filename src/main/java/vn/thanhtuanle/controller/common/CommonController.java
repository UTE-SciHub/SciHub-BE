package vn.thanhtuanle.controller.common;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.thanhtuanle.common.service.FileUtil;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Common Controller")
@RequiredArgsConstructor
public class CommonController {

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName) {
        try {
            byte[] bytes = FileUtil.readFileContent(fileName);

            MediaType mediaType = FileUtil.getMediaTypeForFileName(fileName);
            return ResponseEntity.ok().contentType(mediaType).body(bytes);

        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
}
