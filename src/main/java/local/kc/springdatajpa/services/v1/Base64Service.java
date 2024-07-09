package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.utils.base64.Base64Request;
import local.kc.springdatajpa.utils.base64.Base64Response;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Random;

@Service
public class Base64Service {
    private static final String path = Paths.get("").toAbsolutePath() + "/src/main/resources/static/";

    public ResponseEntity<?> uploadFile(Base64Request base64Request) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Request.base64Data);
        String dir = base64Request.type.split("/")[0];
        String type = base64Request.type.split("/")[1];
        if (dir.equals("image")) {
            String fileName;
            String filePath;
            do {
                fileName = generateFileName() + "." + type;
                filePath = path + "images/" + fileName;
            }
            while (Files.exists(Path.of(filePath)));

            try (OutputStream outputStream = new FileOutputStream(filePath)){
                outputStream.write(decodedBytes);
            } catch (IOException e) {
                return ResponseEntity.internalServerError().build();
            }

            return ResponseEntity.ok(new Base64Response("/images/" + fileName));
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<?> getImage(String url) {
        String filePath = path + "images/" + url;
        Path path1 = Path.of(filePath);
        if (!Files.exists(path1)) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] bytes = Files.readAllBytes(path1);
            Resource resource = new ByteArrayResource(bytes);
            String contentType = Files.probeContentType(path1);
            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String generateFileName() {
        int length = 10;
        StringBuilder name = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int i1 = random.nextInt(10);
            name.append(i1);
        }
        return name.toString();
    }
}
