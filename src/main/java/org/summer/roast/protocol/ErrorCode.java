package org.summer.roast.protocol;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {
    REQUEST_HANDLER_NOT_FOUND(1, "request handler not found");
    ;
    private int code;
    private String desc;

    private ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("errorCode", code);
        map.put("errorDesc", desc);
        return map;
    }

}
