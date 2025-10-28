package com.farmtastic.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {
    private long maxSize;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        this.maxSize = constraintAnnotation.max(); //會抓註解上設定的值（例如 @FileSize(max = 2 * 1024 * 1024)）
        //把這個值存在 maxSize 裡，會用來比對檔案大小。
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // 不檢查空的（讓 @NotNull 來處理是否為空）
        }
        return file.getSize() <= maxSize;
    }
}