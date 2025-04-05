package com.ecommerce.project.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    private String ResourceName;
    private String field;
    private String fieldName;
    private long fieldId;
    private String Message;

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s",resourceName, field, fieldName));
        ResourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, long fieldId) {
        super(String.format("%s not found with %s: %d",resourceName, field, fieldId));
        ResourceName = resourceName;
        this.field= field;
        this.fieldId = fieldId;
    }




    public ResourceNotFoundException() {
    }
}
