import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class AWSController {

    // 다운로드 파일이 저장된 디렉토리 경로
    private static final String DOWNLOAD_DIR = "/path/to/download/directory"; // 실제 디렉토리 경로로 변경해야 합니다.

    @GetMapping("/api/v1/rest/aws/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) throws IOException {
        // 다운로드할 파일의 전체 경로 생성
        String filePath = DOWNLOAD_DIR + "/" + fileName;
        Path file = Paths.get(filePath);

        if (Files.exists(file)) {
            // 파일이 존재하면 해당 파일을 읽어와서 응답으로 보냅니다.
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.toFile().length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            // 파일이 존재하지 않으면 404 에러 응답
            return ResponseEntity.notFound().build();
        }
    }
}
