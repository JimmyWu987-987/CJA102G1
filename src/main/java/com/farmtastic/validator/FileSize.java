package com.farmtastic.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented //這個註解會被包含在 JavaDoc 文件
@Constraint(validatedBy = FileSizeValidator.class) //告訴 Jakarta Bean Validation 這個註解要使用哪個邏輯類別來驗證
@Target({ ElementType.FIELD }) // 這個驗證只能用在欄位FIELD上
@Retention(RetentionPolicy.RUNTIME) //RUNTIME這個註解會在執行階段保留，這樣驗證框架才可以使用mapping讀到它
public @interface FileSize { //這是一個自定義註解，annotation是一種特殊的interface，不是拿來implement的，是拿來標記用的*
    String message() default "檔案大小超過限制";
    long max() default 1024 * 1024; // 預設 1MB，可以彈性給值，沒設定的話就用預設
    Class<?>[] groups() default {};  //進階分組驗證（一般用不到)
    Class<? extends Payload>[] payload() default {}; //進階的用途，例如攜帶錯誤的 metadata（基本用不到）
}