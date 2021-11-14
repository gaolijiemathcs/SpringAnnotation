package com.gao.spring.condition;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSeletor implements ImportSelector {
    // 返回值就是要导入到容器中的组件全类名
    // AnnotationMetadata 当前标注@Import注解的类的所有注解信息
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        return new String[]{"com.gao.spring.bean.Blue", "com.gao.spring.bean.Red", "com.gao.spring.bean.Yellow"};
    }
}
