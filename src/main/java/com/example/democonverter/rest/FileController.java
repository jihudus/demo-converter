package com.example.democonverter.rest;

import com.example.democonverter.data.AbstractDTO;
import com.example.democonverter.service.CoordinatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("file/")
@RequiredArgsConstructor
public class FileController {

    private final CoordinatorService coordinatorService;

    @PostMapping
    public ResponseEntity<AbstractDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(coordinatorService.addFile(file));
    }

    @PatchMapping("{fileId}/")
    public ResponseEntity<AbstractDTO> resample(@PathVariable UUID fileId,
                                              @RequestBody Map<String, Integer> dimensions) {
        AbstractDTO result = coordinatorService.convert(fileId, dimensions.get("width"), dimensions.get(
                "height"));
        return ResponseEntity.ok(result);
    }

    @GetMapping("{fileId}/")
    public ResponseEntity<AbstractDTO> getInfo(@PathVariable UUID fileId) {
        return ResponseEntity.ok(coordinatorService.info(fileId));
    }

    @DeleteMapping("{fileId}/")
    public ResponseEntity<AbstractDTO> deleteFile(@PathVariable UUID fileId) {
        return ResponseEntity.ok(coordinatorService.delete(fileId));
    }
}
