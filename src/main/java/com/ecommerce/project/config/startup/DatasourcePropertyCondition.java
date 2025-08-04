package com.ecommerce.project.config.startup;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class DatasourcePropertyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String dataSourceUrl=context.getEnvironment().getProperty("spring.datasource.url");
        return dataSourceUrl !=null && !dataSourceUrl.isEmpty();
    }
}
