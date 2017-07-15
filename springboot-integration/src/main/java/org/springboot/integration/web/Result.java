package org.springboot.integration.web;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Result implements Serializable {
    public static final Result OK = Result.ok();
    private boolean ok;
    private String code;
    private String message;
    private Object content;
    private String token;

    private Result() {
    }

    /**
     * 创建一个“成功”类型的反馈信息。
     *
     * @return 类型为“成功”的反馈信息对象。
     */
    public static Result ok() {
        Result result = new Result();
        result.ok = true;
        return result;
    }

    /**
     * 创建一个“成功”类型的反馈信息。
     *
     * @param content 处理成功时返回的数据内容
     * @return 类型为“成功”的反馈信息对象。
     */
    public static Result ok(Object content) {
        Result result = new Result();
        result.ok = true;
        result.content = content;
        return result;
    }

    public static Result ok(String message, Object content) {
        Result result = new Result();
        result.ok = true;
        result.message = message;
        result.content = content;
        return result;
    }

    public static Result fail(String code, String message, Object content) {
        Result result = new Result();
        result.ok = false;
        result.code = code;
        result.message = message;
        result.content = content;
        return result;
    }

    public static Result fail(String message) {
        return fail(null, message, null);
    }
    
    public static Result fail(String code, String message) {
        return fail(code, message, null);
    }

    public static Result fail(Throwable ex) {
        String code = getCodeBy(ex);
        return Result.fail(code, ex.getMessage());
    }

    private static String getCodeBy(Throwable ex) {
        String code = getCodeByMethod(ex);
        if (code == null) {
            code = getCodeByField(ex);
        }
        return code;
    }

    private static String getCodeByMethod(Throwable ex) {
        try {
            Method codeMethod = ex.getClass().getDeclaredMethod("getCode");
            boolean isAccessible = codeMethod.isAccessible();
            codeMethod.setAccessible(true);
            String code = String.valueOf(codeMethod.invoke(ex));
            codeMethod.setAccessible(isAccessible);
            return code;
        } catch (Exception e) {
            return null;
        }
    }

    private static String getCodeByField(Throwable ex) {
        try {
            Field codeField = ex.getClass().getField("code");
            boolean isAccessible = codeField.isAccessible();
            codeField.setAccessible(true);
            String code = String.valueOf(codeField.get(ex));
            codeField.setAccessible(isAccessible);
            return code;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取反馈结果类别。其值可能为：{@code success}，{@code failed} 和 {@code pended}
     *
     * @return 反馈结果类别。
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * 获取错误代码。只有 {@link #isOk()} 返回 {@code false} 时有效。
     *
     * @return 错误代码。
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取描述信息。失败时是错误的描述，成功时是成功的描述。
     *
     * @return 描述信息。
     */
    public String getMessage() {
        return message;
    }

    /**
     * 获取详细内容。如果成功，则是成功的信息，如果失败，则是失败的详细内容。
     *
     * @return 详细内容。如果成功，则是成功的信息，如果失败，则是失败的详细内容。
     */
    public Object getContent() {
        return content;
    }

    /**
     * 获取反馈对应请求的token
     *
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置请求方的创建的与本次请求相关的token。
     *
     * @param token 待设置的token。
     */
    public Result token(String token) {
        this.token = token;
        return this;
    }
}
