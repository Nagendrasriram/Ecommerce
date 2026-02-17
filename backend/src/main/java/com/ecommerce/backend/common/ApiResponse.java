package com.ecommerce.backend.common;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse(boolean success,String messgae,T data)
    {
        this.success=success;
        this.message=messgae;
        this.data=data;
    }
    public boolean isSuccess()
    {
        return success;
    }
    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
