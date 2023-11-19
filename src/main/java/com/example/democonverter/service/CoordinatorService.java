package com.example.democonverter.service;

import com.example.democonverter.data.*;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoordinatorService {

    private final FileStoreService fileStoreService;

    private final ConversionService conversionService;

    private final DBService dbService;

    public AbstractDTO addFile(MultipartFile file) {

        log.debug(String.format("Recieved file: %s", file.getOriginalFilename()));

        String fileName;
        try {
            fileName = fileStoreService.storeFile(file);
        } catch (Exception e) {
            return new ErrorDTO(String.format("Cannot save the file %s", file.getOriginalFilename()));
        }

        log.debug(String.format("File %s is saved on FS, send to DB", file.getOriginalFilename()));

        FileEntity fileEntity = FileEntity.builder()
                .filename(fileName)
                .isProcessing(false)
                .lastOperationSuccess(null)
                .build();
        dbService.persistFile(fileEntity);
        return new UploadDTO(fileEntity.getId());
    }

    public AbstractDTO convert(UUID fileId, int newWidth, int newHeight) {
        /*
        To check:
        1. Dimensions evenness
        2. File presence
        3. Active converting absence
         */
        if (newWidth%2 != 0 || newHeight%2 != 0) return new ErrorDTO("Width and height must be even");

        FileEntity fileEntity = dbService.getFileEntity(fileId);
        if (fileEntity == null) return new ErrorDTO(String.format("File %s not found", fileId));

        String name = fileEntity.getFilename();
        boolean isProcessing = fileEntity.getIsProcessing();

        if (isProcessing) return new ErrorDTO(String.format("File %s is in progress", name));

        log.debug(String.format("File name %s is found, trying to convert", name));

        try {
            // Transaction must complete to save value into db
            dbService.setInProgress(fileId, true);
            Completable.fromRunnable(conversionService.convert(name, newWidth, newHeight))
                    .subscribeOn(Schedulers.computation())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            log.debug(">>>>>>>>>>> ON COMPLETE");
                            completeConversion(fileEntity);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            log.debug(">>>>>>>>>>> ON ERROR");
                            failConversion(fileEntity, e);
                        }
                    });
        } catch (Exception ex) {
            log.debug(">>>>>>>>>>> CATCH BLOCK");
            failConversion(fileEntity, ex);
        }
        return new ResultDTO(true);
    }

    private void completeConversion(FileEntity fileEntity) {
        fileEntity.setLastOperationSuccess(true);
        fileEntity.setIsProcessing(false);

        // Temporary file is merging with original
        fileStoreService.merge(fileEntity.getFilename());
        dbService.persistFile(fileEntity);
    }

    private void failConversion(FileEntity fileEntity, Throwable e) {
        fileEntity.setLastOperationSuccess(false);
        fileEntity.setIsProcessing(false);
        dbService.persistFile(fileEntity);
    }

    public FileDTO info(UUID fileId) {
        FileEntity fileEntity = dbService.getFileEntity(fileId);
        return new FileDTO(fileEntity);
    }

    public AbstractDTO delete(UUID fileId) {
        boolean result = false;
        FileEntity fileEntity = dbService.getFileEntity(fileId);
        if (fileEntity == null) return new ErrorDTO(String.format("File + %s not found", fileId));
        try {
            if (!fileStoreService.removeFile(fileEntity.getFilename())) return new ResultDTO(result);
            result = dbService.delete(fileId);
        } catch (Exception ex) {
            return new ResultDTO(result);
        }
        return new ResultDTO(result);
    }
}
